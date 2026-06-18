-- 리뷰 조회 부하테스트용 대량 데이터 적재 스크립트 (MySQL 8)
-- 실행 예:
-- mysql -h 127.0.0.1 -P 3307 -u perf -pperf hiddencountry_perf < docs/performance/review-loadtest/sql/seed-review-data.sql

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE review_tag;
TRUNCATE TABLE review_image;
TRUNCATE TABLE review;
TRUNCATE TABLE place_country;
TRUNCATE TABLE place;
TRUNCATE TABLE `user`;

SET FOREIGN_KEY_CHECKS = 1;

-- 1) 사용자 1,000명 생성
CREATE TEMPORARY TABLE seq_1000 AS
SELECT d0.n + d1.n * 10 + d2.n * 100 + 1 AS n
FROM
  (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
   SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d0
CROSS JOIN
  (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
   SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d1
CROSS JOIN
  (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
   SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d2
WHERE d0.n + d1.n * 10 + d2.n * 100 < 1000;

INSERT INTO `user` (kakao_id, nickname, profile_image, created_at, updated_at)
SELECT
  900000000 + n,
  CONCAT('perf_user_', LPAD(n, 4, '0')),
  CONCAT('https://example.com/u/', n, '.png'),
  NOW(), NOW()
FROM seq_1000;

-- 2) 테스트용 place 1건 생성 (id는 auto increment)
INSERT INTO place (
  addr1, addr2, cat1, content_id, content_type, area_code,
  first_image, first_image2, mapx, mapy, title, r_season,
  review_count, review_score_average, view_count, is_review_image
) VALUES (
  '서울특별시 중구 세종대로 110',
  '성능테스트용 장소',
  'A01',
  1000001,
  12,
  '1',
  'https://example.com/place/1.jpg',
  'https://example.com/place/1_2.jpg',
  126.9780,
  37.5665,
  '성능테스트 장소',
  'ALL',
  0,
  0,
  0,
  0
);

-- 3) 리뷰 대량 생성
-- 기본 200,000건. 필요 시 아래 값을 변경하세요.
SET @review_count := 200000;
SET @place_id := (SELECT id FROM place ORDER BY id DESC LIMIT 1);
SET @max_user_id := (SELECT MAX(id) FROM `user`);
SET @row_num := 0;

INSERT INTO review (content, score, place_id, user_id, created_at, updated_at)
SELECT
  CONCAT('부하테스트 리뷰 #', @row_num := @row_num + 1),
  1 + ((@row_num - 1) MOD 5),
  @place_id,
  1 + ((@row_num - 1) MOD @max_user_id),
  NOW() - INTERVAL ((@row_num - 1) MOD 100000) SECOND,
  NOW() - INTERVAL ((@row_num - 1) MOD 100000) SECOND
FROM seq_1000 a
CROSS JOIN seq_1000 b
LIMIT @review_count;

-- 4) 이미지 데이터 일부 생성 (리뷰의 약 30%)
INSERT INTO review_image (url, review_id, created_at, updated_at)
SELECT
  CONCAT('https://example.com/review/', r.id, '.jpg'),
  r.id,
  NOW(), NOW()
FROM review r
WHERE MOD(r.id, 10) < 3;

-- 5) place 집계 컬럼 보정
UPDATE place p
SET
  review_count = (SELECT COUNT(*) FROM review r WHERE r.place_id = p.id),
  review_score_average = (SELECT IFNULL(AVG(r.score), 0) FROM review r WHERE r.place_id = p.id)
WHERE p.id = @place_id;

DROP TEMPORARY TABLE IF EXISTS seq_1000;

SELECT
  @place_id AS target_place_id,
  (SELECT COUNT(*) FROM review WHERE place_id = @place_id) AS review_count,
  (SELECT COUNT(*) FROM review_image ri JOIN review r ON ri.review_id = r.id WHERE r.place_id = @place_id) AS image_count;
