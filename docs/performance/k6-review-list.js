import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    review_list_latest: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 10 },
        { duration: '40s', target: 30 },
        { duration: '20s', target: 0 },
      ],
      exec: 'latestScenario',
    },
    review_list_rating: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 5 },
        { duration: '40s', target: 15 },
        { duration: '20s', target: 0 },
      ],
      exec: 'ratingScenario',
      startTime: '5s',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<2000'],
    'http_req_duration{scenario:latest}': ['p(95)<2000'],
    'http_req_duration{scenario:rating}': ['p(95)<2000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const PLACE_ID = __ENV.PLACE_ID || '1';
const SIZE = __ENV.SIZE || '10';

export function latestScenario() {
  const url = `${BASE_URL}/review/${PLACE_ID}?sort=LATEST&size=${SIZE}`;
  const res = http.get(url, { tags: { scenario: 'latest' } });

  check(res, {
    'latest status is 2xx': (r) => r.status >= 200 && r.status < 300,
  });

  sleep(0.5);
}

export function ratingScenario() {
  const url = `${BASE_URL}/review/${PLACE_ID}?sort=RATING_DESC&size=${SIZE}`;
  const res = http.get(url, { tags: { scenario: 'rating' } });

  check(res, {
    'rating status is 2xx': (r) => r.status >= 200 && r.status < 300,
  });

  sleep(0.5);
}
