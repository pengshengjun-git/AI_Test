-- =====================================================
-- AI测试管理平台 - 添加项目编码字段更新脚本
-- 执行时间: 2026-05-29
-- =====================================================

USE ai_test_platform;

-- =====================================================
-- 1. 检查并添加 code 字段
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
    'ADD COLUMN `', @columnname, '` VARCHAR(64) NOT NULL COMMENT ''项目编码'' ',
    'AFTER `id`'
  )
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 2. 检查并添加唯一索引
-- =====================================================
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

-- =====================================================
-- 3. 为现有项目更新编码
-- =====================================================
UPDATE `project` SET `code` = 'PRJ-001' WHERE `id` = 1 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-002' WHERE `id` = 2 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-003' WHERE `id` = 3 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-004' WHERE `id` = 4 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-005' WHERE `id` = 5 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-006' WHERE `id` = 6 AND (`code` IS NULL OR `code` = '');

SELECT '数据库更新完成！' AS message;
