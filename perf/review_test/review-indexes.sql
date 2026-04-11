-- Review feature candidate indexes for before/after comparison.
-- Apply only the ADD INDEX section for the "after" test.
-- Apply only the DROP INDEX section when you want to revert to baseline.

-- ADD INDEX
ALTER TABLE review
    ADD INDEX idx_review_place_id_id_desc (place_id, id DESC);

ALTER TABLE review
    ADD INDEX idx_review_place_score_id_desc (place_id, score DESC, id DESC);

ALTER TABLE review
    ADD INDEX idx_review_user_id_id_desc (user_id, id DESC);

-- DROP INDEX
-- ALTER TABLE review DROP INDEX idx_review_place_id_id_desc;
-- ALTER TABLE review DROP INDEX idx_review_place_score_id_desc;
-- ALTER TABLE review DROP INDEX idx_review_user_id_id_desc;
