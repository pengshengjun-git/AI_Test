-- =====================================================
-- AI测试管理平台 - 添加项目编码字段更新脚本 (步骤2: 更新数据)
-- =====================================================

USE ai_test_platform;

-- =====================================================
-- 1. 为现有项目更新编码
-- =====================================================
UPDATE `project` SET `code` = 'PRJ-001' WHERE `id` = 1 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-002' WHERE `id` = 2 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-003' WHERE `id` = 3 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-004' WHERE `id` = 4 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-005' WHERE `id` = 5 AND (`code` IS NULL OR `code` = '');
UPDATE `project` SET `code` = 'PRJ-006' WHERE `id` = 6 AND (`code` IS NULL OR `code` = '');

-- =====================================================
-- 2. 为其他没有编码的项目自动生成编码
-- =====================================================
UPDATE `project` 
SET `code` = CONCAT('PRJ-', LPAD(`id`, 3, '0')) 
WHERE `code` IS NULL OR `code` = '';

SELECT '步骤2完成：已更新所有项目编码' AS message;
