/**
 * Fixed-rate load test for review list endpoints.
 *
 * Uses constant-arrival-rate executor so the RPS stays the same
 * regardless of server response time — enabling a fair before/after comparison.
 *
 * Usage (PowerShell):
 *   $env:BASE_URL="http://localhost:8080"
 *   $env:PLACE_ID="2"
 *   $env:RATE="4"          # iterations/s  (4 iter × 4 req = ~16 RPS)
 *   $env:DURATION="30s"
 *   $env:RUN_LABEL="fixed-rate-after-index"
 *   k6 run .\perf\review_test\review-list-fixed-rate.js
 *
 * Matching the before-index baseline (~15.75 RPS = ~3.94 iter/s):
 *   set RATE=4 to approximate the baseline load.
 */

import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl  = __ENV.BASE_URL  || 'http://localhost:8080';
const placeId  = __ENV.PLACE_ID  || '2';
const rate     = Number(__ENV.RATE     || 4);    // iterations per second
const duration = __ENV.DURATION || '30s';
const runLabel = __ENV.RUN_LABEL || 'fixed-rate-review-load-test';

export const options = {
  scenarios: {
    fixed_rate: {
      executor:        'constant-arrival-rate',
      rate,            // iterations to start per timeUnit
      timeUnit:        '1s',
      duration,
      preAllocatedVUs: 30,
      maxVUs:          60, // allow headroom if server is slow
    },
  },
  thresholds: {
    http_req_failed:   ['rate<0.01'],
    http_req_duration: ['p(95)<800', 'p(99)<1500'],
  },
};

function reviewListUrl(sort, cursorId, cursorScore) {
  const q = [`sort=${encodeURIComponent(sort)}`, 'size=10'];
  if (cursorId)    q.push(`cursorId=${encodeURIComponent(String(cursorId))}`);
  if (cursorScore != null) q.push(`cursorScore=${encodeURIComponent(String(cursorScore))}`);
  return `${baseUrl}/review/${placeId}?${q.join('&')}`;
}

function listAndNextPage(sort) {
  const first = http.get(reviewListUrl(sort));
  check(first, { [`${sort} first page status is 200`]: r => r.status === 200 });
  if (first.status !== 200) return;

  const data = first.json()?.data;
  if (!data?.hasNext || !data?.nextId) return;

  const nextUrl = sort === 'RATING_DESC'
    ? reviewListUrl(sort, data.nextId, data.nextScore)
    : reviewListUrl(sort, data.nextId);

  const second = http.get(nextUrl);
  check(second, { [`${sort} second page status is 200`]: r => r.status === 200 });
}

export default function () {
  listAndNextPage('LATEST');
  listAndNextPage('RATING_DESC');
  // no sleep — arrival rate is controlled by the executor, not by VU pacing
}

export function handleSummary(data) {
  return {
    stdout: JSON.stringify(data, null, 2),
    [`summary-${runLabel}.json`]: JSON.stringify(data, null, 2),
  };
}
