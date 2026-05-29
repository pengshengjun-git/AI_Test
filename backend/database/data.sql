-- =====================================================
-- AI测试管理平台 - 数据库初始化数据脚本
-- 数据库版本: MySQL 8.0+
-- 创建日期: 2026-05-28
-- 所有测试用户密码统一为: Admin@123
-- 密码BCrypt哈希值: $2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q
-- =====================================================

USE ai_test_platform;

-- =====================================================
-- 1. 角色数据
-- =====================================================
INSERT INTO `role` (`id`, `name`, `code`, `description`, `status`, `deleted`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 1, 0),
(2, '管理员', 'ADMIN', '系统管理员', 1, 0),
(3, '测试经理', 'TEST_MANAGER', '测试经理', 1, 0),
(4, '测试工程师', 'TEST_ENGINEER', '测试工程师', 1, 0),
(5, '开发人员', 'DEVELOPER', '开发人员', 1, 0),
(6, '访客', 'GUEST', '访客', 1, 0)
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), description=VALUES(description), status=VALUES(status);

-- =====================================================
-- 2. 部门数据
-- =====================================================
INSERT INTO `department` (`id`, `parent_id`, `name`, `code`, `leader_id`, `sort_order`, `status`, `description`) VALUES
(1, 0, '总公司', 'HEADQUARTERS', NULL, 1, 1, '总公司'),
(2, 1, '技术部', 'TECHNOLOGY', NULL, 1, 1, '技术部'),
(3, 1, '质量部', 'QUALITY', NULL, 2, 1, '质量部'),
(4, 2, '开发组', 'DEVELOPMENT', NULL, 1, 1, '开发组'),
(5, 2, '测试组', 'TESTING', NULL, 2, 1, '测试组')
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), description=VALUES(description);

-- =====================================================
-- 3. 用户数据
-- =====================================================
INSERT INTO `user` (`id`, `username`, `password_hash`, `email`, `phone`, `real_name`, `status`, `department_id`) VALUES
(1, 'admin', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'admin@aitest.com', '13800000001', '系统管理员', 1, 2),
(2, 'manager', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'manager@aitest.com', '13800000002', '项目经理张', 1, 3),
(3, 'tester1', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'tester1@aitest.com', '13800000003', '测试工程师李', 1, 5),
(4, 'tester2', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'tester2@aitest.com', '13800000004', '测试工程师王', 1, 5),
(5, 'user1', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'user1@aitest.com', '13800000005', '普通用户赵', 1, 1),
(6, 'user2', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'user2@aitest.com', '13800000006', '开发人员钱', 1, 4),
(7, 'admin2', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'admin2@aitest.com', '13800000007', '管理员孙', 1, 2)
ON DUPLICATE KEY UPDATE 
    password_hash=VALUES(password_hash), email=VALUES(email), phone=VALUES(phone), real_name=VALUES(real_name), status=VALUES(status), department_id=VALUES(department_id);

-- =====================================================
-- 4. 权限数据
-- =====================================================
INSERT INTO `permission` (`id`, `name`, `code`, `type`, `description`, `deleted`) VALUES
(1, '用户管理', 'user:manage', 'menu', '用户管理菜单', 0),
(2, '查看用户', 'user:view', 'api', '查看用户', 0),
(3, '创建用户', 'user:create', 'api', '创建用户', 0),
(4, '编辑用户', 'user:edit', 'api', '编辑用户', 0),
(5, '删除用户', 'user:delete', 'api', '删除用户', 0),
(6, '角色管理', 'role:manage', 'menu', '角色管理菜单', 0),
(7, '查看角色', 'role:view', 'api', '查看角色', 0),
(8, '创建角色', 'role:create', 'api', '创建角色', 0),
(9, '编辑角色', 'role:edit', 'api', '编辑角色', 0),
(10, '删除角色', 'role:delete', 'api', '删除角色', 0),
(11, '项目管理', 'project:manage', 'menu', '项目管理菜单', 0),
(12, '查看项目', 'project:view', 'api', '查看项目', 0),
(13, '创建项目', 'project:create', 'api', '创建项目', 0),
(14, '编辑项目', 'project:edit', 'api', '编辑项目', 0),
(15, '删除项目', 'project:delete', 'api', '删除项目', 0),
(16, '需求管理', 'requirement:manage', 'menu', '需求管理菜单', 0),
(17, '查看需求', 'requirement:view', 'api', '查看需求', 0),
(18, '创建需求', 'requirement:create', 'api', '创建需求', 0),
(19, '编辑需求', 'requirement:edit', 'api', '编辑需求', 0),
(20, '删除需求', 'requirement:delete', 'api', '删除需求', 0),
(21, '测试用例管理', 'testcase:manage', 'menu', '测试用例管理菜单', 0),
(22, '查看用例', 'testcase:view', 'api', '查看用例', 0),
(23, '创建用例', 'testcase:create', 'api', '创建用例', 0),
(24, '编辑用例', 'testcase:edit', 'api', '编辑用例', 0),
(25, '删除用例', 'testcase:delete', 'api', '删除用例', 0),
(26, '缺陷管理', 'defect:manage', 'menu', '缺陷管理菜单', 0),
(27, '查看缺陷', 'defect:view', 'api', '查看缺陷', 0),
(28, '创建缺陷', 'defect:create', 'api', '创建缺陷', 0),
(29, '编辑缺陷', 'defect:edit', 'api', '编辑缺陷', 0),
(30, '删除缺陷', 'defect:delete', 'api', '删除缺陷', 0),
(31, 'AI中心', 'ai:center', 'menu', 'AI中心菜单', 0),
(32, '使用AI', 'ai:use', 'api', '使用AI功能', 0),
(33, '测试计划管理', 'testplan:manage', 'menu', '测试计划管理菜单', 0),
(34, '查看测试计划', 'testplan:view', 'api', '查看测试计划', 0),
(35, '创建测试计划', 'testplan:create', 'api', '创建测试计划', 0),
(36, '编辑测试计划', 'testplan:edit', 'api', '编辑测试计划', 0),
(37, '删除测试计划', 'testplan:delete', 'api', '删除测试计划', 0),
(38, '覆盖率管理', 'coverage:manage', 'menu', '覆盖率管理菜单', 0),
(39, '查看覆盖率', 'coverage:view', 'api', '查看覆盖率', 0),
(40, '创建覆盖率', 'coverage:create', 'api', '创建覆盖率', 0),
(41, '编辑覆盖率', 'coverage:edit', 'api', '编辑覆盖率', 0),
(42, '删除覆盖率', 'coverage:delete', 'api', '删除覆盖率', 0),
(43, '策略管理', 'strategy:manage', 'menu', '策略管理菜单', 0),
(44, '查看策略', 'strategy:view', 'api', '查看策略', 0),
(45, '创建策略', 'strategy:create', 'api', '创建策略', 0),
(46, '编辑策略', 'strategy:edit', 'api', '编辑策略', 0),
(47, '删除策略', 'strategy:delete', 'api', '删除策略', 0),
(48, '部门管理', 'department:manage', 'menu', '部门管理菜单', 0),
(49, '查看部门', 'department:view', 'api', '查看部门', 0),
(50, '创建部门', 'department:create', 'api', '创建部门', 0),
(51, '编辑部门', 'department:edit', 'api', '编辑部门', 0),
(52, '删除部门', 'department:delete', 'api', '删除部门', 0),
(53, '菜单管理', 'menu:manage', 'menu', '菜单管理菜单', 0),
(54, '查看菜单', 'menu:view', 'api', '查看菜单', 0),
(55, '创建菜单', 'menu:create', 'api', '创建菜单', 0),
(56, '编辑菜单', 'menu:edit', 'api', '编辑菜单', 0),
(57, '删除菜单', 'menu:delete', 'api', '删除菜单', 0),
(58, '工作台', 'dashboard:read', 'menu', '工作台菜单', 0),
(59, '查看工作台', 'dashboard:view', 'api', '查看工作台', 0)
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), type=VALUES(type), description=VALUES(description);

-- =====================================================
-- 5. 用户角色关联
-- =====================================================
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES 
(1, 1),
(2, 3),
(3, 4),
(4, 4),
(5, 6),
(6, 5),
(7, 2)
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id);

-- =====================================================
-- 6. 角色权限关联 - SUPER_ADMIN角色分配所有权限
-- =====================================================
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15),
(1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
(1, 21), (1, 22), (1, 23), (1, 24), (1, 25),
(1, 26), (1, 27), (1, 28), (1, 29), (1, 30),
(1, 31), (1, 32), (1, 33), (1, 34), (1, 35),
(1, 36), (1, 37), (1, 38), (1, 39), (1, 40),
(1, 41), (1, 42), (1, 43), (1, 44), (1, 45),
(1, 46), (1, 47), (1, 48), (1, 49), (1, 50),
(1, 51), (1, 52), (1, 53), (1, 54), (1, 55),
(1, 56), (1, 57), (1, 58), (1, 59)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- =====================================================
-- 7. 角色权限关联 - TEST_ENGINEER角色分配测试相关权限
-- =====================================================
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(4, 11), (4, 12),
(4, 16), (4, 17), (4, 18), (4, 19),
(4, 21), (4, 22), (4, 23), (4, 24),
(4, 26), (4, 27), (4, 28), (4, 29),
(4, 31), (4, 32),
(4, 33), (4, 34), (4, 35), (4, 36),
(4, 38), (4, 39),
(4, 58), (4, 59)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- =====================================================
-- 8. 菜单数据 - 与前端App.vue菜单定义完全对齐
-- =====================================================
INSERT INTO `menu` (`id`, `parent_id`, `name`, `code`, `path`, `component`, `icon`, `type`, `permission`, `sort_order`, `visible`, `cacheable`, `description`, `status`, `permission_code`) VALUES
-- 业务菜单
(1, 0, '工作台', 'dashboard', '/', 'dashboard/index', '🏠', 1, 'dashboard:read', 1, 1, 1, '工作台', 1, 'dashboard:read'),
(2, 0, '项目管理', 'project', '/project', 'project/index', '📁', 1, 'project:view', 2, 1, 1, '项目管理', 1, 'project:view'),
(3, 0, '需求管理', 'requirement', '/requirement', 'requirement/index', '📋', 1, 'requirement:view', 3, 1, 1, '需求管理', 1, 'requirement:view'),
(4, 0, '用例管理', 'testcase', '/testcase', 'testcase/index', '📝', 1, 'testcase:view', 4, 1, 1, '用例管理', 1, 'testcase:view'),
(5, 0, '测试计划', 'testplan', '/testplan', 'testplan/index', '📅', 1, 'testplan:read', 5, 1, 1, '测试计划', 1, 'testplan:read'),
(6, 0, '缺陷管理', 'defect', '/defect', 'defect/index', '⚠️', 1, 'defect:view', 6, 1, 1, '缺陷管理', 1, 'defect:view'),
(7, 0, '覆盖率', 'coverage', '/coverage', 'coverage/index', '📊', 1, 'coverage:read', 7, 1, 1, '覆盖率', 1, 'coverage:read'),
(8, 0, '策略管理', 'strategy', '/strategy', 'strategy/index', '⚙️', 1, 'strategy:read', 8, 1, 1, '策略管理', 1, 'strategy:read'),
(9, 0, 'AI中心', 'ai-center', '/ai-center', 'ai-center/index', '🤖', 1, 'ai:use', 9, 1, 1, 'AI中心', 1, 'ai:use'),
-- 系统管理目录
(10, 0, '系统管理', 'system', '/system', NULL, 'Setting', 0, NULL, 10, 1, 1, '系统管理目录', 1, NULL),
(11, 10, '用户管理', 'user', '/system/users', 'system/users/index', '👥', 1, 'user:manage', 1, 1, 1, '用户管理', 1, 'user:manage'),
(12, 10, '角色管理', 'role', '/system/roles', 'system/roles/index', '🔑', 1, 'role:manage', 2, 1, 1, '角色管理', 1, 'role:manage'),
(13, 10, '部门管理', 'department', '/system/departments', 'system/departments/index', '🏢', 1, 'department:manage', 3, 1, 1, '部门管理', 1, 'department:manage'),
(14, 10, '菜单管理', 'menu', '/system/menus', 'system/menus/index', '📋', 1, 'menu:manage', 4, 1, 1, '菜单管理', 1, 'menu:manage')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- =====================================================
-- 9. 角色菜单关联
-- =====================================================
INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `menu` WHERE deleted = 0 OR deleted IS NULL
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 2, id FROM `menu` WHERE code LIKE 'system%' AND (deleted = 0 OR deleted IS NULL)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 3, id FROM `menu` WHERE (code = 'dashboard' OR code = 'project' OR code = 'requirement' OR code = 'testcase' OR code = 'testplan' OR code = 'defect' OR code = 'ai-center') AND (deleted = 0 OR deleted IS NULL)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 4, id FROM `menu` WHERE (code = 'dashboard' OR code = 'project' OR code = 'requirement' OR code = 'testcase' OR code = 'testplan' OR code = 'defect' OR code = 'ai-center') AND (deleted = 0 OR deleted IS NULL)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- =====================================================
-- 10. 项目数据 (与Project实体类对齐)
-- =====================================================
INSERT INTO `project` (`id`, `code`, `name`, `description`, `status`, `priority`, `created_by`, `updated_by`) VALUES
(1, 'PRJ-001', '示例项目', '这是一个示例项目，用于演示AI测试平台功能', 'IN_PROGRESS', 'P0', 1, 1),
(2, 'PRJ-002', '电商平台测试', '电商平台完整测试项目', 'PLANNING', 'P1', 1, 1),
(3, 'PRJ-003', '用户中心系统', '用户登录注册模块测试', 'IN_PROGRESS', 'P1', 2, 2),
(4, 'PRJ-004', '支付模块测试', '第三方支付接口测试', 'PLANNING', 'P0', 1, 1),
(5, 'PRJ-005', '数据分析平台', '大数据分析系统测试', 'COMPLETED', 'P2', 2, 2),
(6, 'PRJ-006', '后台管理系统', '管理后台功能测试', 'IN_PROGRESS', 'P2', 1, 1)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), status=VALUES(status), priority=VALUES(priority);

-- =====================================================
-- 11. 项目成员数据
-- =====================================================
INSERT INTO `project_member` (`project_id`, `user_id`, `role`, `created_by`) VALUES
(1, 1, 'owner', 1),
(1, 2, 'manager', 1),
(1, 3, 'member', 1),
(1, 4, 'member', 1),
(2, 2, 'owner', 2),
(2, 3, 'member', 2),
(3, 1, 'owner', 1),
(3, 4, 'member', 1),
(4, 2, 'owner', 2),
(4, 6, 'member', 2)
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- =====================================================
-- 12. 需求数据
-- =====================================================
INSERT INTO `requirement` (`id`, `project_id`, `title`, `description`, `type`, `priority`, `status`, `source`, `created_by`) VALUES
(1, 1, '用户登录功能', '实现用户名密码登录', 'functional', 'P0', 'implemented', 'manual', 1),
(2, 1, '商品搜索功能', '实现关键词搜索', 'functional', 'P1', 'approved', 'manual', 1),
(3, 1, '购物车功能', '添加商品到购物车', 'functional', 'P1', 'approved', 'manual', 2),
(4, 1, '订单管理', '订单创建支付流程', 'functional', 'P0', 'implemented', 'manual', 1),
(5, 3, '用户注册', '新用户注册功能', 'functional', 'P0', 'implemented', 'manual', 1),
(6, 3, '密码找回', '忘记密码重置', 'functional', 'P2', 'draft', 'manual', 2),
(7, 3, '权限管理', '角色权限控制', 'functional', 'P1', 'approved', 'manual', 1),
(8, 4, '支付回调', '第三方支付回调处理', 'functional', 'P0', 'approved', 'manual', 1),
(9, 4, '退款功能', '订单退款流程', 'functional', 'P1', 'draft', 'manual', 2),
(10, 2, '数据报表', '生成数据分析报表', 'functional', 'P2', 'implemented', 'manual', 1)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- =====================================================
-- 13. 测试用例数据 (与Testcase实体类对齐)
-- =====================================================
INSERT INTO `testcase` (`id`, `project_id`, `requirement_id`, `title`, `description`, `priority`, `type`, `status`, `test_status`, `test_module`, `steps`, `expected_result`, `actual_result`, `ai_generated`, `created_by`, `updated_by`) VALUES
(1, 1, 1, '登录成功测试', '验证正确用户名密码登录', 'P0', 'FUNCTIONAL', 'approved', 'passed', '用户模块', '1. 打开登录页面\n2. 输入用户名admin\n3. 输入密码Admin@123\n4. 点击登录按钮', '成功登录并跳转到首页', NULL, 0, 1, 1),
(2, 1, 1, '登录失败测试', '验证错误密码登录', 'P0', 'FUNCTIONAL', 'approved', 'passed', '用户模块', '1. 打开登录页面\n2. 输入用户名admin\n3. 输入错误密码\n4. 点击登录按钮', '提示密码错误', NULL, 0, 1, 1),
(3, 1, 2, '搜索空结果', '搜索不存在的商品', 'P2', 'FUNCTIONAL', 'approved', 'pending', '搜索模块', '1. 进入搜索页面\n2. 输入无效关键词\n3. 点击搜索', '显示无结果提示', NULL, 0, 2, 2),
(4, 1, 3, '添加购物车', '添加商品到购物车', 'P1', 'FUNCTIONAL', 'approved', 'pending', '购物车模块', '1. 选择商品\n2. 点击加入购物车', '商品添加成功', NULL, 0, 1, 1),
(5, 1, 4, '创建订单', '创建新订单', 'P0', 'FUNCTIONAL', 'approved', 'passed', '订单模块', '1. 选择商品\n2. 提交订单\n3. 确认支付', '订单创建成功', NULL, 0, 1, 1),
(6, 3, 5, '注册成功', '新用户注册', 'P0', 'FUNCTIONAL', 'approved', 'passed', '注册模块', '1. 填写注册信息\n2. 提交注册', '注册成功并自动登录', NULL, 0, 1, 1),
(7, 3, 7, '权限验证', '验证权限控制', 'P1', 'FUNCTIONAL', 'approved', 'pending', '权限模块', '1. 访问受限页面', '提示无权限', NULL, 0, 2, 2),
(8, 4, 8, '支付回调测试', '模拟支付回调', 'P0', 'FUNCTIONAL', 'approved', 'pending', '支付模块', '1. 发起支付\n2. 模拟回调', '订单状态更新为已支付', NULL, 0, 1, 1)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- =====================================================
-- 14. 缺陷数据 (与Defect实体类对齐)
-- =====================================================
INSERT INTO `defect` (`id`, `project_id`, `title`, `description`, `priority`, `status`, `type`, `severity`, `module`, `steps_to_reproduce`, `expected_result`, `actual_result`, `requirement_id`, `reporter_id`, `assignee`, `created_by`, `updated_by`) VALUES
(1, 1, '登录页面样式错乱', '登录按钮对齐问题', 'P3', 'new', 'bug', 'low', '用户模块', '1. 打开登录页面\n2. 观察按钮位置', '按钮对齐正确', '按钮位置偏移', 1, 3, 1, 3, 3),
(2, 1, '搜索结果显示不全', '搜索结果只显示前10条', 'P2', 'new', 'bug', 'medium', '搜索模块', '1. 搜索包含大量结果的关键词\n2. 查看结果数量', '显示所有结果', '只显示10条', 2, 4, 6, 4, 4),
(3, 1, '购物车数量错误', '添加商品数量不正确', 'P1', 'assigned', 'bug', 'high', '购物车模块', '1. 添加商品到购物车\n2. 查看数量', '数量正确', '数量多计1', 3, 3, 6, 3, 3),
(4, 1, '订单超时问题', '订单30分钟未支付未自动取消', 'P0', 'new', 'bug', 'critical', '订单模块', '1. 创建订单\n2. 等待30分钟\n3. 查看订单状态', '订单自动取消', '订单仍为待支付', 4, 1, 6, 1, 1),
(5, 3, '注册邮箱验证失败', '邮箱格式验证不严格', 'P2', 'closed', 'bug', 'medium', '注册模块', '1. 输入非法邮箱格式\n2. 提交注册', '提示邮箱格式错误', '注册成功', 5, 3, 6, 3, 3),
(6, 3, '权限绕过漏洞', '可访问未授权页面', 'P0', 'assigned', 'bug', 'critical', '权限模块', '1. 登录普通用户\n2. 直接访问管理员页面', '拒绝访问', '成功访问', 7, 4, 1, 4, 4),
(7, 4, '支付回调重复处理', '同一回调被多次处理', 'P0', 'new', 'bug', 'critical', '支付模块', '1. 模拟支付回调\n2. 多次发送相同回调', '只处理一次', '重复处理多次', 8, 1, 6, 1, 1),
(8, 4, '退款失败', '部分退款金额错误', 'P1', 'new', 'bug', 'high', '支付模块', '1. 创建订单\n2. 申请部分退款\n3. 查看退款金额', '退款金额正确', '金额计算错误', 9, 4, 6, 4, 4)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- =====================================================
-- 15. 测试计划数据
-- =====================================================
INSERT INTO `test_plan` (`id`, `name`, `description`, `project_id`, `start_date`, `end_date`, `status`, `owner`, `created_by`) VALUES
(1, 'Sprint 1测试计划', '第一阶段测试任务', 1, '2026-01-01', '2026-01-15', 'completed', '项目经理张', 2),
(2, '支付模块测试计划', '支付功能专项测试', 4, '2026-03-01', '2026-03-15', 'in_progress', '测试工程师李', 3),
(3, '用户中心测试计划', '用户模块全面测试', 3, '2026-02-15', '2026-02-28', 'completed', '测试工程师王', 4)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description);

-- =====================================================
-- 16. 策略数据
-- =====================================================
INSERT INTO `strategy` (`id`, `name`, `description`, `type`, `status`, `config`, `created_by`) VALUES
(1, 'AI测试用例生成', '自动生成测试用例', 'ai', 'ENABLED', '{"model":"gpt-4","maxTokens":2000}', 1),
(2, '缺陷智能分析', '自动分析缺陷根因', 'ai', 'ENABLED', '{"model":"claude-3","confidence":0.85}', 1),
(3, '测试报告生成', '自动生成测试报告', 'ai', 'DISABLED', '{"model":"gemini","format":"html"}', 2),
(4, '需求优先级评估', 'AI评估需求优先级', 'ai', 'ENABLED', '{"model":"qwen","threshold":0.7}', 1)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description);

-- =====================================================
-- 17. 测试覆盖率数据
-- =====================================================
INSERT INTO `coverage` (`id`, `project_id`, `covered_lines`, `total_lines`, `coverage_rate`, `report_date`) VALUES
(1, 1, 8500, 10000, 85.00, '2026-05-28'),
(2, 3, 6200, 8000, 77.50, '2026-05-28'),
(3, 4, 1500, 5000, 30.00, '2026-05-28'),
(4, 2, 9000, 9500, 94.74, '2026-05-28')
ON DUPLICATE KEY UPDATE covered_lines=VALUES(covered_lines), total_lines=VALUES(total_lines), coverage_rate=VALUES(coverage_rate);

-- =====================================================
-- 18. 待办事项数据
-- =====================================================
INSERT INTO `todo` (`id`, `title`, `description`, `completed`, `deadline`, `priority`, `created_by`) VALUES
(1, '完成测试计划文档', '编写Q1测试计划', 0, '2026-06-20', 'P1', 1),
(2, '修复登录bug', '登录页面样式问题', 1, '2026-05-15', 'P0', 1),
(3, '代码审查', '审查支付模块代码', 0, '2026-05-18', 'P2', 2),
(4, '准备测试环境', '部署测试服务器', 0, '2026-05-25', 'P1', 1),
(5, '编写接口文档', 'API接口文档', 1, '2026-05-10', 'P2', 2)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

SELECT '数据库初始化数据插入完成！' AS message;
SELECT '所有测试用户密码统一为: Admin@123' AS password_info;
