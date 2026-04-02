import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';
const placeId = __ENV.PLACE_ID || '2';
const vus = Number(__ENV.VUS || 30);
const duration = __ENV.DURATION || '30s';
const runMode = (__ENV.RUN_MODE || 'public').toLowerCase();
const accessToken = __ENV.ACCESS_TOKEN || '';
const runLabel = __ENV.RUN_LABEL || 'review-load-test-after-index';

export const options = {
  vus,
  duration,
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<800', 'p(99)<1500'],
  },
};

function buildHeaders() {
  if (!accessToken) {
    return {};
  }

  return {
    Authorization: `Bearer ${accessToken}`,
  };
}

function reviewListParams(sort, cursorId, cursorScore) {
  const query = [`sort=${encodeURIComponent(sort)}`, 'size=10'];

  if (cursorId) {
    query.push(`cursorId=${encodeURIComponent(String(cursorId))}`);
  }
  if (cursorScore !== undefined && cursorScore !== null) {
    query.push(`cursorScore=${encodeURIComponent(String(cursorScore))}`);
  }

  return `${baseUrl}/review/${placeId}?${query.join('&')}`;
}

function listAndNextPage(sort) {
  const headers = buildHeaders();
  const first = http.get(reviewListParams(sort), { headers });

  check(first, {
    [`${sort} first page status is 200`]: (res) => res.status === 200,
  });

  if (first.status !== 200) {
    return;
  }

  const firstBody = first.json();
  const data = firstBody?.data;
  if (!data?.hasNext || !data?.nextId) {
    return;
  }

  const nextUrl =
    sort === 'RATING_DESC'
      ? reviewListParams(sort, data.nextId, data.nextScore)
      : reviewListParams(sort, data.nextId);

  const second = http.get(nextUrl, { headers });
  check(second, {
    [`${sort} second page status is 200`]: (res) => res.status === 200,
  });
}

function myPage() {
  const response = http.get(`${baseUrl}/review/mypage?page=0&size=5`, {
    headers: buildHeaders(),
  });

  check(response, {
    'mypage status is 200': (res) => res.status === 200,
  });
}

export default function () {
  if (runMode === 'mypage') {
    myPage();
    sleep(1);
    return;
  }

  listAndNextPage('LATEST');
  listAndNextPage('RATING_DESC');
  sleep(1);
}

export function handleSummary(data) {
  return {
    stdout: JSON.stringify(data, null, 2),
    [`summary-${runLabel}.json`]: JSON.stringify(data, null, 2),
  };
}

