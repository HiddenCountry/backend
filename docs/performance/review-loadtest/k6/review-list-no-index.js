import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const PLACE_ID = __ENV.PLACE_ID || '1';
const PAGE_SIZE = __ENV.PAGE_SIZE || '20';
const THINK_TIME = Number(__ENV.THINK_TIME || '0.2');

function fetchPage(sort, cursorId, cursorScore) {
  let url = `${BASE_URL}/review/${PLACE_ID}?sort=${sort}&size=${PAGE_SIZE}`;

  if (cursorId) {
    url += `&cursorId=${cursorId}`;
  }
  if (cursorScore !== null && cursorScore !== undefined) {
    url += `&cursorScore=${cursorScore}`;
  }

  return http.get(url, {
    tags: { endpoint: 'review_list', sort },
  });
}

function runCursorFlow(sort) {
  let cursorId = null;
  let cursorScore = null;

  for (let i = 0; i < 3; i++) {
    const res = fetchPage(sort, cursorId, cursorScore);

    const ok = check(res, {
      [`${sort} status is 2xx`]: (r) => r.status >= 200 && r.status < 300,
      [`${sort} body has data`]: (r) => {
        const data = r.json('data');
        return data !== null && data !== undefined;
      },
    });

    if (!ok) {
      return;
    }

    const hasNext = res.json('data.hasNext');
    cursorId = res.json('data.nextId');

    if (sort === 'RATING_DESC') {
      cursorScore = res.json('data.nextScore');
    }

    if (!hasNext || !cursorId) {
      return;
    }

    sleep(THINK_TIME);
  }
}

export const options = {
  scenarios: {
    latest_no_index: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 60 },
        { duration: '30s', target: 0 },
      ],
      exec: 'latestScenario',
    },
    rating_no_index: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '30s', target: 10 },
        { duration: '1m', target: 30 },
        { duration: '30s', target: 0 },
      ],
      exec: 'ratingScenario',
      startTime: '10s',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<3000'],
    'http_req_duration{sort:LATEST}': ['p(95)<3000'],
    'http_req_duration{sort:RATING_DESC}': ['p(95)<3000'],
  },
};

export function latestScenario() {
  runCursorFlow('LATEST');
}

export function ratingScenario() {
  runCursorFlow('RATING_DESC');
}
