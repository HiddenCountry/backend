import http from 'k6/http';
import { check } from 'k6';

// 구 방식: 서버가 S3 업로드를 트랜잭션 안에서 직접 처리
// 측정 대상: POST /review/{placeId} (multipart/form-data)
// 실행 전: git stash 으로 변경 전 코드 복원 필요

const BASE_URL    = __ENV.BASE_URL     || 'http://localhost:8080';
const PLACE_ID    = __ENV.PLACE_ID     || '2';
const ACCESS_TOKEN = __ENV.ACCESS_TOKEN || '';
const VUS         = Number(__ENV.VUS   || 5);
const DURATION    = __ENV.DURATION     || '30s';
const RUN_LABEL   = __ENV.RUN_LABEL    || 'review-create-before';

const imageData = open('./test-image.jpg', 'b');

export const options = {
  vus: VUS,
  duration: DURATION,
  thresholds: {
    http_req_failed:   ['rate<0.05'],
    http_req_duration: ['p(95)<10000'],
  },
};

export default function () {
  const headers = ACCESS_TOKEN
    ? { Authorization: `Bearer ${ACCESS_TOKEN}` }
    : {};

  const formData = {
    request: http.file(
      JSON.stringify({ score: 4, content: '성능 테스트용 리뷰입니다.', tags: ['CLEAN', 'KIND'] }),
      'request',
      'application/json'
    ),
    images: http.file(imageData, 'test-image.jpg', 'image/jpeg'),
  };

  // 이 요청 시간 = DB 저장 + S3 업로드 시간 합산
  const res = http.post(`${BASE_URL}/review/${PLACE_ID}`, formData, { headers });

  check(res, { 'status is 200': (r) => r.status === 200 });
}

export function handleSummary(data) {
  return {
    stdout: JSON.stringify(data, null, 2),
    [`summary-${RUN_LABEL}.json`]: JSON.stringify(data, null, 2),
  };
}
