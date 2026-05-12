import http from 'k6/http';
import { check } from 'k6';
import { Trend } from 'k6/metrics';

// 신 방식: 클라이언트가 S3에 직접 업로드 (Presigned URL)
// 측정 대상:
//   - serverDuration  : 서버 응답 시간만 (presigned URL 발급 + 리뷰 저장)
//   - s3UploadDuration: S3 직접 업로드 시간
//   - totalDuration   : 전체 흐름 시간 (= serverDuration + s3UploadDuration)

const BASE_URL     = __ENV.BASE_URL     || 'http://localhost:8080';
const PLACE_ID     = __ENV.PLACE_ID     || '2';
const ACCESS_TOKEN = __ENV.ACCESS_TOKEN || '';
const VUS          = Number(__ENV.VUS   || 5);
const DURATION     = __ENV.DURATION     || '30s';
const RUN_LABEL    = __ENV.RUN_LABEL    || 'review-create-after';

const imageData = open('./test-image.jpg', 'b');

export const options = {
  vus: VUS,
  duration: DURATION,
  thresholds: {
    http_req_failed:   ['rate<0.05'],
    http_req_duration: ['p(95)<10000'],
  },
};

const serverDuration   = new Trend('server_duration',    true);
const s3UploadDuration = new Trend('s3_upload_duration', true);
const totalDuration    = new Trend('total_duration',     true);

export default function () {
  const headers = {
    ...(ACCESS_TOKEN ? { Authorization: `Bearer ${ACCESS_TOKEN}` } : {}),
    'Content-Type': 'application/json',
  };

  const flowStart = Date.now();

  // Step 1: Presigned URL 발급
  const presignedRes = http.post(
    `${BASE_URL}/review/${PLACE_ID}/presigned-urls`,
    JSON.stringify({ count: 1 }),
    { headers }
  );
  check(presignedRes, { 'presigned url 200': (r) => r.status === 200 });
  if (presignedRes.status !== 200) return;

  const urls = presignedRes.json('data');
  if (!urls || urls.length === 0) return;
  const { presignedUrl, s3Url } = urls[0];

  // Step 2: S3 직접 업로드 (서버 미경유)
  const s3Start = Date.now();
  const uploadRes = http.put(presignedUrl, imageData, {
    headers: { 'Content-Type': 'image/jpeg' },
  });
  s3UploadDuration.add(Date.now() - s3Start);

  check(uploadRes, { 'S3 upload 200': (r) => r.status === 200 });
  if (uploadRes.status !== 200) return;

  // Step 3: 리뷰 등록 (JSON, S3 URL만 전달)
  const reviewRes = http.post(
    `${BASE_URL}/review/${PLACE_ID}`,
    JSON.stringify({
      score: 4,
      content: '성능 테스트용 리뷰입니다.',
      tags: ['CLEAN', 'KIND'],
      imageUrls: [s3Url],
    }),
    { headers }
  );
  check(reviewRes, { 'review create 200': (r) => r.status === 200 });

  // 서버 시간 = presigned URL 발급 + 리뷰 저장 (S3 제외)
  serverDuration.add(presignedRes.timings.duration + reviewRes.timings.duration);
  totalDuration.add(Date.now() - flowStart);
}

export function handleSummary(data) {
  return {
    stdout: JSON.stringify(data, null, 2),
    [`summary-${RUN_LABEL}.json`]: JSON.stringify(data, null, 2),
  };
}
