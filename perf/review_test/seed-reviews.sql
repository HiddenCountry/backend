-- MySQL 8+
-- Load-test seed for review endpoints.
-- Replace @target_place_id with an existing place.id before running.

SET SESSION cte_max_recursion_depth = 50000;

SET @target_place_id = 2;
SET @review_count = 20000;
SET @user_count = 200;
SET @kakao_seed = 910000000000;
SET @nickname_prefix = 'loadtester_';
SET @content_prefix = '[LOADTEST] review ';

-- Safety check: target place must exist.
SELECT id, title
FROM place
WHERE id = @target_place_id;

-- 1) Create synthetic users if they do not already exist.
INSERT INTO `user` (kakao_id, nickname, profile_image, created_at, updated_at)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < @user_count
)
SELECT
    @kakao_seed + n,
    CONCAT(@nickname_prefix, LPAD(n, 4, '0')),
    CONCAT('https://example.com/profile/', n, '.png'),
    NOW(),
    NOW()
FROM seq
WHERE NOT EXISTS (
    SELECT 1
    FROM `user` u
    WHERE u.kakao_id = @kakao_seed + n
);

-- 2) Insert reviews concentrated on one place.
INSERT INTO review (content, score, place_id, user_id, created_at, updated_at)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < @review_count
)
SELECT
    CONCAT(@content_prefix, LPAD(n, 6, '0')),
    ((n - 1) % 5) + 1,
    @target_place_id,
    (
        SELECT u.id
        FROM `user` u
        WHERE u.kakao_id = @kakao_seed + (((n - 1) % @user_count) + 1)
    ),
    NOW() - INTERVAL n MINUTE,
    NOW() - INTERVAL n MINUTE
FROM seq;

-- 3) Add one image to roughly 40% of generated reviews.
INSERT INTO review_image (url, review_id, created_at, updated_at)
SELECT
    CONCAT('https://example.com/reviews/', r.id, '.jpg'),
    r.id,
    r.created_at,
    r.updated_at
FROM review r
WHERE r.place_id = @target_place_id
  AND r.content LIKE CONCAT(@content_prefix, '%')
  AND MOD(r.id, 5) IN (0, 1);

-- 4) Add two tags per generated review.
INSERT INTO review_tag (tag_name, review_id, created_at, updated_at)
SELECT
    CASE MOD(r.id, 10)
        WHEN 0 THEN 'TASTE_GOOD'
        WHEN 1 THEN 'LOCAL_STYLE'
        WHEN 2 THEN 'WORTH_PRICE'
        WHEN 3 THEN 'CLEAN_FACILITY'
        WHEN 4 THEN 'COZY'
        WHEN 5 THEN 'GOOD_PHOTO'
        WHEN 6 THEN 'NICE_VIEW'
        WHEN 7 THEN 'EASY_PARKING'
        WHEN 8 THEN 'GOOD_FOR_DATE'
        ELSE 'CLEAN'
    END,
    r.id,
    r.created_at,
    r.updated_at
FROM review r
WHERE r.place_id = @target_place_id
  AND r.content LIKE CONCAT(@content_prefix, '%');

INSERT INTO review_tag (tag_name, review_id, created_at, updated_at)
SELECT
    CASE MOD(r.id + 3, 10)
        WHEN 0 THEN 'TASTE_GOOD'
        WHEN 1 THEN 'LOCAL_STYLE'
        WHEN 2 THEN 'WORTH_PRICE'
        WHEN 3 THEN 'CLEAN_FACILITY'
        WHEN 4 THEN 'COZY'
        WHEN 5 THEN 'GOOD_PHOTO'
        WHEN 6 THEN 'NICE_VIEW'
        WHEN 7 THEN 'EASY_PARKING'
        WHEN 8 THEN 'GOOD_FOR_DATE'
        ELSE 'CLEAN'
    END,
    r.id,
    r.created_at,
    r.updated_at
FROM review r
WHERE r.place_id = @target_place_id
  AND r.content LIKE CONCAT(@content_prefix, '%');

-- 5) Refresh denormalized place stats for consistency.
UPDATE place p
SET
    p.review_count = (
        SELECT COUNT(*)
        FROM review r
        WHERE r.place_id = p.id
    ),
    p.review_score_average = COALESCE((
        SELECT AVG(r.score)
        FROM review r
        WHERE r.place_id = p.id
          AND r.score IS NOT NULL
    ), 0)
WHERE p.id = @target_place_id;

-- 6) Quick sanity check.
SELECT COUNT(*) AS seeded_reviews
FROM review
WHERE place_id = @target_place_id
  AND content LIKE CONCAT(@content_prefix, '%');

SELECT score, COUNT(*) AS cnt
FROM review
WHERE place_id = @target_place_id
  AND content LIKE CONCAT(@content_prefix, '%')
GROUP BY score
ORDER BY score;
