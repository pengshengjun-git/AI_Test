-- Requirement table field migration script
-- Date: 2026-05-29
-- Description: Add missing fields to requirement table

USE ai_test_platform;

-- Add missing fields to requirement table

-- Add proposer field
ALTER TABLE `requirement` ADD COLUMN `proposer` VARCHAR(100) COMMENT 'Proposer';

-- Add effective_version field
ALTER TABLE `requirement` ADD COLUMN `effective_version` VARCHAR(50) COMMENT 'Effective Version';

-- Add acceptance_criteria field
ALTER TABLE `requirement` ADD COLUMN `acceptance_criteria` TEXT COMMENT 'Acceptance Criteria';

-- Add owner field
ALTER TABLE `requirement` ADD COLUMN `owner` VARCHAR(100) COMMENT 'Owner';

-- Add reviewer field
ALTER TABLE `requirement` ADD COLUMN `reviewer` VARCHAR(100) COMMENT 'Reviewer';

-- Add permission_scope field
ALTER TABLE `requirement` ADD COLUMN `permission_scope` VARCHAR(32) DEFAULT 'public' COMMENT 'Permission Scope';

-- Add review_result field
ALTER TABLE `requirement` ADD COLUMN `review_result` VARCHAR(32) COMMENT 'Review Result';

-- Add review_comments field
ALTER TABLE `requirement` ADD COLUMN `review_comments` TEXT COMMENT 'Review Comments';

-- Add online_time field
ALTER TABLE `requirement` ADD COLUMN `online_time` DATETIME COMMENT 'Online Time';

-- Add close_reason field
ALTER TABLE `requirement` ADD COLUMN `close_reason` VARCHAR(200) COMMENT 'Close Reason';

SELECT 'Requirement table fields migration completed' AS message;
