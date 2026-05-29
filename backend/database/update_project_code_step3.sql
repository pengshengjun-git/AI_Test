-- =====================================================
-- AI测试管理平台 - 添加项目编码字段更新脚本 (步骤3: 添加约束)
-- =====================================================

USE ai_test_platform;

-- =====================================================
-- 1. 将字段改为 NOT NULL
-- =====================================================
ALTER TABLE `project` MODIFY COLUMN `code` VARCHAR(64) NOT NULL COMMENT '项目编码';

-- =====================================================
-- 2. 添加唯一索引
-- =====================================================
SET @dbname = DATABASE();
SET @tablename = 'project';
SET @columnname = 'code';
SET @indexname = 'uk_project_code';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE
      (table_schema = @dbname)
      AND (table_name = @tablename)
      AND (index_name = @indexname)
  ) > 0,
  'SELECT 1',
  CONCAT(
    'ALTER TABLE `', @tablename, '` ',
    'ADD UNIQUE KEY `', @indexname, '` (`', @columnname, '`)'
  )
));
PREPARE addIndexIfNotExists FROM @preparedStatement;
EXECUTE addIndexIfNotExists;
DEALLOCATE PREPARE addIndexIfNotExists;

SELECT '步骤3完成：已添加NOT NULL约束和唯一索引' AS message;
