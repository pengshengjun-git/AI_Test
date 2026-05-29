-- Add missing proposer_time field
USE ai_test_platform;
ALTER TABLE `requirement` ADD COLUMN `proposer_time` DATETIME COMMENT 'Proposer Time';
SELECT 'proposer_time field added' AS message;
