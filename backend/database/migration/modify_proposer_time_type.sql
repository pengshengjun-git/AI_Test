-- Modify proposer_time field type from DATETIME to VARCHAR
USE ai_test_platform;
ALTER TABLE `requirement` MODIFY COLUMN `proposer_time` VARCHAR(50) COMMENT 'Proposer Time';
SELECT 'proposer_time field type modified to VARCHAR' AS message;
