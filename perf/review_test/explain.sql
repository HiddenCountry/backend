-- Run on MySQL 8+
-- Replace the sample literal values with your actual place/user/cursor values.

-- BASELINE OR AFTER-INDEX: latest first page
EXPLAIN ANALYZE
SELECT r.id, r.place_id, r.user_id, r.score
FROM review r
WHERE r.place_id = 2
ORDER BY r.id DESC
LIMIT 10;

-- BASELINE OR AFTER-INDEX: latest next page
EXPLAIN ANALYZE
SELECT r.id, r.place_id, r.user_id, r.score
FROM review r
WHERE r.place_id = 2
  AND r.id < 900000
ORDER BY r.id DESC
LIMIT 10;

-- BASELINE OR AFTER-INDEX: rating first page
EXPLAIN ANALYZE
SELECT r.id, r.place_id, r.user_id, r.score
FROM review r
WHERE r.place_id = 2
ORDER BY r.score DESC, r.id DESC
LIMIT 10;

-- BASELINE OR AFTER-INDEX: rating next page
EXPLAIN ANALYZE
SELECT r.id, r.place_id, r.user_id, r.score
FROM review r
WHERE r.place_id = 2
  AND (r.score < 4 OR (r.score = 4 AND r.id < 900000))
ORDER BY r.score DESC, r.id DESC
LIMIT 10;

-- BASELINE OR AFTER-INDEX: mypage
EXPLAIN ANALYZE
SELECT r.id, r.place_id, r.user_id, r.score
FROM review r
WHERE r.user_id = 100
ORDER BY r.id DESC
LIMIT 5 OFFSET 0;
