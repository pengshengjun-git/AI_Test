-- =====================================================
-- AI测试管理平台 - 添加项目编码字段更新脚本 (步骤1: 添加字段)
-- =====================================================

USE ai_test_platform;

-- =====================================================
-- 1. 检查并添加 code 字段（允许NULL）
-- =====================================================
SET @dbname = DATABASE();
SET @tablename = 'project';
SET @columnname = 'code';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_schema = @dbname)
      AND (table_name = @tablename)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT(
    'ALTER TABLE `', @tablename, '` ',
    'ADD COLUMN `', @columnname, '` VARCHAR(64) NULL COMMENT ''项目编码'' ',
    'AFTER `id`'
  )
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SELECT '步骤1完成：已添加code字段（允许NULL）' AS message;
