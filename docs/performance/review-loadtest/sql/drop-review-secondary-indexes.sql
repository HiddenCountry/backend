-- 리뷰 조회 테스트를 '인덱스 없는 상태'로 맞추기 위한 스크립트
-- PK는 유지하고, review 테이블의 보조 인덱스만 제거합니다.
-- 실행 예:
-- mysql -h 127.0.0.1 -P 3307 -u perf -pperf hiddencountry_perf < docs/performance/review-loadtest/sql/drop-review-secondary-indexes.sql

SET @db := DATABASE();

SELECT index_name
FROM information_schema.statistics
WHERE table_schema = @db
  AND table_name = 'review'
  AND index_name <> 'PRIMARY';

SET @drop_sql = (
  SELECT IFNULL(
    GROUP_CONCAT(CONCAT('DROP INDEX `', index_name, '` ON `review`') SEPARATOR '; '),
    'SELECT "no secondary index"'
  )
  FROM (
    SELECT DISTINCT index_name
    FROM information_schema.statistics
    WHERE table_schema = @db
      AND table_name = 'review'
      AND index_name <> 'PRIMARY'
  ) t
);

PREPARE stmt FROM @drop_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SHOW INDEX FROM review;
