import http from "k6/http";
import { check, sleep } from "k6";
const baseUrl = __ENV.BASE_URL || "http://localhost:8080";
const placeId = __ENV.PLACE_ID || "2";
export const options = { vus: Number(__ENV.VUS || 30), duration: __ENV.DURATION || "30s", thresholds: { http_req_failed: ["rate<0.01"], http_req_duration: ["p(95)<800", "p(99)<1500"] } };
export default function () {
  const res = http.get(`${baseUrl}/review/${placeId}?sort=RATING_DESC&size=10`);
  check(res, { "rating status is 200": (r) => r.status === 200 });
  sleep(1);
}
export function handleSummary(data) { return { stdout: JSON.stringify(data, null, 2), ["summary-rating-only-20260325.json"]: JSON.stringify(data, null, 2) }; }
