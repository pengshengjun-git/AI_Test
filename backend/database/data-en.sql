-- =====================================================
-- AI Test Platform - Database Initialization Data (English)
-- Database: MySQL 8.0+
-- Created: 2026-05-28
-- All test user password: Admin@123
-- Password BCrypt hash: $2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q
-- =====================================================

USE ai_test_platform;

-- =====================================================
-- 1. Role Data
-- =====================================================
INSERT INTO `role` (`id`, `name`, `code`, `description`, `status`, `deleted`) VALUES
(1, 'SuperAdmin', 'SUPER_ADMIN', 'Have all permissions', 1, 0),
(2, 'Admin', 'ADMIN', 'System administrator', 1, 0),
(3, 'TestManager', 'TEST_MANAGER', 'Test manager', 1, 0),
(4, 'TestEngineer', 'TEST_ENGINEER', 'Test engineer', 1, 0),
(5, 'Developer', 'DEVELOPER', 'Developer', 1, 0),
(6, 'Guest', 'GUEST', 'Guest', 1, 0)
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), description=VALUES(description), status=VALUES(status);

-- =====================================================
-- 2. Department Data
-- =====================================================
INSERT INTO `department` (`id`, `parent_id`, `name`, `code`, `leader_id`, `sort_order`, `status`, `description`) VALUES
(1, 0, 'Headquarters', 'HEADQUARTERS', NULL, 1, 1, 'Headquarters'),
(2, 1, 'Technology', 'TECHNOLOGY', NULL, 1, 1, 'Technology department'),
(3, 1, 'Quality', 'QUALITY', NULL, 2, 1, 'Quality department'),
(4, 2, 'Development', 'DEVELOPMENT', NULL, 1, 1, 'Development team'),
(5, 2, 'Testing', 'TESTING', NULL, 2, 1, 'Testing team')
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), description=VALUES(description);

-- =====================================================
-- 3. User Data
-- =====================================================
INSERT INTO `user` (`id`, `username`, `password_hash`, `email`, `phone`, `real_name`, `status`, `department_id`) VALUES
(1, 'admin', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'admin@aitest.com', '13800000001', 'System Admin', 1, 2),
(2, 'manager', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'manager@aitest.com', '13800000002', 'Project Manager', 1, 3),
(3, 'tester1', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'tester1@aitest.com', '13800000003', 'Test Engineer 1', 1, 5),
(4, 'tester2', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'tester2@aitest.com', '13800000004', 'Test Engineer 2', 1, 5),
(5, 'user1', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'user1@aitest.com', '13800000005', 'Normal User', 1, 1),
(6, 'user2', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'user2@aitest.com', '13800000006', 'Developer', 1, 4),
(7, 'admin2', '$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q', 'admin2@aitest.com', '13800000007', 'Admin 2', 1, 2)
ON DUPLICATE KEY UPDATE 
    password_hash=VALUES(password_hash), email=VALUES(email), phone=VALUES(phone), real_name=VALUES(real_name), status=VALUES(status), department_id=VALUES(department_id);

-- =====================================================
-- 4. Permission Data
-- =====================================================
INSERT INTO `permission` (`id`, `name`, `code`, `type`, `description`, `deleted`) VALUES
(1, 'User Management', 'user:manage', 'menu', 'User management menu', 0),
(2, 'View User', 'user:view', 'api', 'View user', 0),
(3, 'Create User', 'user:create', 'api', 'Create user', 0),
(4, 'Edit User', 'user:edit', 'api', 'Edit user', 0),
(5, 'Delete User', 'user:delete', 'api', 'Delete user', 0),
(6, 'Role Management', 'role:manage', 'menu', 'Role management menu', 0),
(7, 'View Role', 'role:view', 'api', 'View role', 0),
(8, 'Create Role', 'role:create', 'api', 'Create role', 0),
(9, 'Edit Role', 'role:edit', 'api', 'Edit role', 0),
(10, 'Delete Role', 'role:delete', 'api', 'Delete role', 0),
(11, 'Project Management', 'project:manage', 'menu', 'Project management menu', 0),
(12, 'View Project', 'project:view', 'api', 'View project', 0),
(13, 'Create Project', 'project:create', 'api', 'Create project', 0),
(14, 'Edit Project', 'project:edit', 'api', 'Edit project', 0),
(15, 'Delete Project', 'project:delete', 'api', 'Delete project', 0),
(16, 'Requirement Management', 'requirement:manage', 'menu', 'Requirement management menu', 0),
(17, 'View Requirement', 'requirement:view', 'api', 'View requirement', 0),
(18, 'Create Requirement', 'requirement:create', 'api', 'Create requirement', 0),
(19, 'Edit Requirement', 'requirement:edit', 'api', 'Edit requirement', 0),
(20, 'Delete Requirement', 'requirement:delete', 'api', 'Delete requirement', 0),
(21, 'Test Case Management', 'testcase:manage', 'menu', 'Test case management menu', 0),
(22, 'View Test Case', 'testcase:view', 'api', 'View test case', 0),
(23, 'Create Test Case', 'testcase:create', 'api', 'Create test case', 0),
(24, 'Edit Test Case', 'testcase:edit', 'api', 'Edit test case', 0),
(25, 'Delete Test Case', 'testcase:delete', 'api', 'Delete test case', 0),
(26, 'Defect Management', 'defect:manage', 'menu', 'Defect management menu', 0),
(27, 'View Defect', 'defect:view', 'api', 'View defect', 0),
(28, 'Create Defect', 'defect:create', 'api', 'Create defect', 0),
(29, 'Edit Defect', 'defect:edit', 'api', 'Edit defect', 0),
(30, 'Delete Defect', 'defect:delete', 'api', 'Delete defect', 0),
(31, 'AI Center', 'ai:center', 'menu', 'AI center menu', 0),
(32, 'Use AI', 'ai:use', 'api', 'Use AI features', 0),
(33, 'Test Plan Management', 'testplan:manage', 'menu', 'Test plan management menu', 0),
(34, 'View Test Plan', 'testplan:view', 'api', 'View test plan', 0),
(35, 'Create Test Plan', 'testplan:create', 'api', 'Create test plan', 0),
(36, 'Edit Test Plan', 'testplan:edit', 'api', 'Edit test plan', 0),
(37, 'Delete Test Plan', 'testplan:delete', 'api', 'Delete test plan', 0),
(38, 'Coverage Management', 'coverage:manage', 'menu', 'Coverage management menu', 0),
(39, 'View Coverage', 'coverage:view', 'api', 'View coverage', 0),
(40, 'Create Coverage', 'coverage:create', 'api', 'Create coverage', 0),
(41, 'Edit Coverage', 'coverage:edit', 'api', 'Edit coverage', 0),
(42, 'Delete Coverage', 'coverage:delete', 'api', 'Delete coverage', 0),
(43, 'Strategy Management', 'strategy:manage', 'menu', 'Strategy management menu', 0),
(44, 'View Strategy', 'strategy:view', 'api', 'View strategy', 0),
(45, 'Create Strategy', 'strategy:create', 'api', 'Create strategy', 0),
(46, 'Edit Strategy', 'strategy:edit', 'api', 'Edit strategy', 0),
(47, 'Delete Strategy', 'strategy:delete', 'api', 'Delete strategy', 0),
(48, 'Department Management', 'department:manage', 'menu', 'Department management menu', 0),
(49, 'View Department', 'department:view', 'api', 'View department', 0),
(50, 'Create Department', 'department:create', 'api', 'Create department', 0),
(51, 'Edit Department', 'department:edit', 'api', 'Edit department', 0),
(52, 'Delete Department', 'department:delete', 'api', 'Delete department', 0),
(53, 'Menu Management', 'menu:manage', 'menu', 'Menu management menu', 0),
(54, 'View Menu', 'menu:view', 'api', 'View menu', 0),
(55, 'Create Menu', 'menu:create', 'api', 'Create menu', 0),
(56, 'Edit Menu', 'menu:edit', 'api', 'Edit menu', 0),
(57, 'Delete Menu', 'menu:delete', 'api', 'Delete menu', 0)
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), type=VALUES(type), description=VALUES(description);

-- =====================================================
-- 5. User Role Association
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
-- 6. Role Permission Association - SUPER_ADMIN gets all permissions
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
(1, 56), (1, 57)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- =====================================================
-- 7. Role Permission Association - TEST_ENGINEER gets test-related permissions
-- =====================================================
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(4, 11), (4, 12),
(4, 16), (4, 17), (4, 18), (4, 19),
(4, 21), (4, 22), (4, 23), (4, 24),
(4, 26), (4, 27), (4, 28), (4, 29),
(4, 31), (4, 32),
(4, 33), (4, 34), (4, 35), (4, 36),
(4, 38), (4, 39)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- =====================================================
-- 8. Menu Data
-- =====================================================
INSERT INTO `menu` (`id`, `parent_id`, `name`, `code`, `path`, `component`, `icon`, `type`, `permission`, `sort_order`, `visible`, `cacheable`, `description`, `status`, `permission_code`) VALUES
(1, 0, 'System', 'system', '/system', NULL, 'Setting', 0, NULL, 1, 1, 1, 'System directory', 1, NULL),
(2, 1, 'User', 'user', '/system/user', 'system/user/index', 'User', 1, 'system:user:list', 1, 1, 1, 'User menu', 1, 'system:user:list'),
(3, 1, 'Role', 'role', '/system/role', 'system/role/index', 'UserFilled', 1, 'system:role:list', 2, 1, 1, 'Role menu', 1, 'system:role:list'),
(4, 1, 'Permission', 'permission', '/system/permission', 'system/permission/index', 'Lock', 1, 'system:permission:list', 3, 1, 1, 'Permission menu', 1, 'system:permission:list'),
(5, 1, 'Department', 'department', '/system/department', 'system/department/index', 'OfficeBuilding', 1, 'system:department:list', 4, 1, 1, 'Department menu', 1, 'system:department:list'),
(6, 1, 'Operation Log', 'operation-log', '/system/operation-log', 'system/operation-log/index', 'Document', 1, 'system:log:list', 5, 1, 1, 'Operation log menu', 1, 'system:log:list'),
(16, 0, 'Dashboard', 'dashboard', '/dashboard', NULL, 'DataLine', 0, NULL, 2, 1, 1, 'Dashboard directory', 1, NULL),
(17, 16, 'Overview', 'dashboard-overview', '/dashboard/overview', 'dashboard/overview/index', 'Odometer', 1, 'dashboard:overview:view', 1, 1, 1, 'Overview menu', 1, 'dashboard:overview:view'),
(18, 16, 'Project Stats', 'dashboard-project', '/dashboard/project', 'dashboard/project/index', 'FolderOpened', 1, 'dashboard:project:view', 2, 1, 1, 'Project stats menu', 1, 'dashboard:project:view'),
(19, 16, 'Test Stats', 'dashboard-test', '/dashboard/test', 'dashboard/test/index', 'DocumentChecked', 1, 'dashboard:test:view', 3, 1, 1, 'Test stats menu', 1, 'dashboard:test:view'),
(20, 0, 'AI Tools', 'ai-toolkit', '/ai-toolkit', NULL, 'MagicStick', 0, NULL, 3, 1, 1, 'AI toolkit directory', 1, NULL),
(21, 20, 'Call Records', 'ai-call-record', '/ai-toolkit/call-record', 'ai-toolkit/call-record/index', 'List', 1, 'ai:callRecord:list', 1, 1, 1, 'Call records menu', 1, 'ai:callRecord:list'),
(22, 20, 'Statistics', 'ai-statistics', '/ai-toolkit/statistics', 'ai-toolkit/statistics/index', 'TrendCharts', 1, 'ai:statistics:view', 2, 1, 1, 'Statistics menu', 1, 'ai:statistics:view')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- =====================================================
-- 9. Role Menu Association
-- =====================================================
INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `menu` WHERE deleted = 0
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 2, id FROM `menu` WHERE code LIKE 'system%' AND deleted = 0
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 3, id FROM `menu` WHERE (code LIKE 'dashboard%' OR code LIKE 'ai%') AND deleted = 0
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- =====================================================
-- 10. Project Data
-- =====================================================
INSERT INTO `project` (`id`, `name`, `description`, `status`, `priority`, `created_by`, `updated_by`) VALUES
(1, 'Demo Project', 'This is a demo project for AI test platform', 'IN_PROGRESS', 'P0', 1, 1),
(2, 'E-commerce Platform Test', 'Complete e-commerce platform test project', 'PLANNING', 'P1', 1, 1),
(3, 'User Center System', 'User login and registration module test', 'IN_PROGRESS', 'P1', 2, 2),
(4, 'Payment Module Test', 'Third-party payment interface test', 'PLANNING', 'P0', 1, 1),
(5, 'Data Analytics Platform', 'Big data analytics system test', 'COMPLETED', 'P2', 2, 2),
(6, 'Admin System', 'Admin backend feature test', 'IN_PROGRESS', 'P2', 1, 1)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), status=VALUES(status), priority=VALUES(priority);

-- =====================================================
-- 11. Project Member Data
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
-- 12. Requirement Data
-- =====================================================
INSERT INTO `requirement` (`id`, `project_id`, `title`, `description`, `type`, `priority`, `status`, `source`, `created_by`) VALUES
(1, 1, 'User Login', 'Implement user login feature', 'functional', 'P0', 'implemented', 'manual', 1),
(2, 1, 'Product Search', 'Implement keyword search', 'functional', 'P1', 'approved', 'manual', 1),
(3, 1, 'Shopping Cart', 'Add product to shopping cart', 'functional', 'P1', 'approved', 'manual', 2),
(4, 1, 'Order Management', 'Order creation and payment flow', 'functional', 'P0', 'implemented', 'manual', 1),
(5, 3, 'User Registration', 'New user registration feature', 'functional', 'P0', 'implemented', 'manual', 1),
(6, 3, 'Password Recovery', 'Forgot password reset', 'functional', 'P2', 'draft', 'manual', 2),
(7, 3, 'Permission Management', 'Role-based permission control', 'functional', 'P1', 'approved', 'manual', 1),
(8, 4, 'Payment Callback', 'Third-party payment callback handling', 'functional', 'P0', 'approved', 'manual', 1),
(9, 4, 'Refund Feature', 'Order refund flow', 'functional', 'P1', 'draft', 'manual', 2),
(10, 2, 'Data Report', 'Generate data analytics report', 'functional', 'P2', 'implemented', 'manual', 1)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- =====================================================
-- 13. Test Case Data
-- =====================================================
INSERT INTO `testcase` (`id`, `project_id`, `requirement_id`, `title`, `description`, `priority`, `type`, `status`, `test_status`, `test_module`, `steps`, `expected_result`, `actual_result`, `ai_generated`, `created_by`, `updated_by`) VALUES
(1, 1, 1, 'Login Success Test', 'Verify correct username and password login', 'P0', 'functional', 'approved', 'passed', 'User Module', '1. Open login page\n2. Enter username admin\n3. Enter password Admin@123\n4. Click login button', 'Login successful and redirect to homepage', NULL, 0, 1, 1),
(2, 1, 1, 'Login Failure Test', 'Verify wrong password login', 'P0', 'functional', 'approved', 'passed', 'User Module', '1. Open login page\n2. Enter username admin\n3. Enter wrong password\n4. Click login button', 'Show password error', NULL, 0, 1, 1),
(3, 1, 2, 'Search Empty Result', 'Search for non-existent product', 'P2', 'functional', 'approved', 'pending', 'Search Module', '1. Go to search page\n2. Enter invalid keyword\n3. Click search', 'Show no results message', NULL, 0, 2, 2),
(4, 1, 3, 'Add to Cart', 'Add product to shopping cart', 'P1', 'functional', 'approved', 'pending', 'Cart Module', '1. Select product\n2. Click add to cart', 'Product added successfully', NULL, 0, 1, 1),
(5, 1, 4, 'Create Order', 'Create new order', 'P0', 'functional', 'approved', 'passed', 'Order Module', '1. Select product\n2. Submit order\n3. Confirm payment', 'Order created successfully', NULL, 0, 1, 1),
(6, 3, 5, 'Registration Success', 'New user registration', 'P0', 'functional', 'approved', 'passed', 'Registration Module', '1. Fill registration info\n2. Submit registration', 'Registration successful and auto login', NULL, 0, 1, 1),
(7, 3, 7, 'Permission Verification', 'Verify permission control', 'P1', 'functional', 'approved', 'pending', 'Permission Module', '1. Access restricted page', 'Show no permission', NULL, 0, 2, 2),
(8, 4, 8, 'Payment Callback Test', 'Simulate payment callback', 'P0', 'functional', 'approved', 'pending', 'Payment Module', '1. Initiate payment\n2. Simulate callback', 'Order status updated to paid', NULL, 0, 1, 1)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- =====================================================
-- 14. Defect Data
-- =====================================================
INSERT INTO `defect` (`id`, `project_id`, `title`, `description`, `priority`, `status`, `type`, `severity`, `module`, `steps_to_reproduce`, `expected_result`, `actual_result`, `requirement_id`, `reporter_id`, `assignee`, `created_by`, `updated_by`) VALUES
(1, 1, 'Login Page Style Issue', 'Login button alignment problem', 'P3', 'new', 'bug', 'low', 'User Module', '1. Open login page\n2. Observe button position', 'Button aligned correctly', 'Button position offset', 1, 3, 1, 3, 3),
(2, 1, 'Search Results Incomplete', 'Search results only show first 10 items', 'P2', 'new', 'bug', 'medium', 'Search Module', '1. Search for keyword with many results\n2. Check result count', 'Show all results', 'Only show 10 items', 2, 4, 6, 4, 4),
(3, 1, 'Shopping Cart Quantity Error', 'Product quantity incorrect', 'P1', 'assigned', 'bug', 'high', 'Cart Module', '1. Add product to cart\n2. Check quantity', 'Quantity correct', 'Quantity overcounted by 1', 3, 3, 6, 3, 3),
(4, 1, 'Order Timeout Issue', 'Order not auto cancelled after 30 mins unpaid', 'P0', 'new', 'bug', 'critical', 'Order Module', '1. Create order\n2. Wait 30 mins\n3. Check order status', 'Order auto cancelled', 'Order still pending payment', 4, 1, 6, 1, 1),
(5, 3, 'Registration Email Validation Failed', 'Email format validation not strict', 'P2', 'closed', 'bug', 'medium', 'Registration Module', '1. Enter invalid email format\n2. Submit registration', 'Show email format error', 'Registration successful', 5, 3, 6, 3, 3),
(6, 3, 'Permission Bypass Vulnerability', 'Can access unauthorized page', 'P0', 'assigned', 'bug', 'critical', 'Permission Module', '1. Login as normal user\n2. Directly access admin page', 'Access denied', 'Successfully accessed', 7, 4, 1, 4, 4),
(7, 4, 'Payment Callback Duplicate Handling', 'Same callback handled multiple times', 'P0', 'new', 'bug', 'critical', 'Payment Module', '1. Simulate payment callback\n2. Send same callback multiple times', 'Only handle once', 'Handled multiple times', 8, 1, 6, 1, 1),
(8, 4, 'Refund Failed', 'Partial refund amount error', 'P1', 'new', 'bug', 'high', 'Payment Module', '1. Create order\n2. Request partial refund\n3. Check refund amount', 'Refund amount correct', 'Amount calculation error', 9, 4, 6, 4, 4)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- =====================================================
-- 15. Test Plan Data
-- =====================================================
INSERT INTO `test_plan` (`id`, `name`, `description`, `project_id`, `start_date`, `end_date`, `status`, `owner`, `created_by`) VALUES
(1, 'Sprint 1 Test Plan', 'First phase test task', 1, '2026-01-01', '2026-01-15', 'completed', 'Project Manager', 2),
(2, 'Payment Module Test Plan', 'Payment feature specific test', 4, '2026-03-01', '2026-03-15', 'in_progress', 'Test Engineer 1', 3),
(3, 'User Center Test Plan', 'User module comprehensive test', 3, '2026-02-15', '2026-02-28', 'completed', 'Test Engineer 2', 4)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description);

-- =====================================================
-- 16. Strategy Data
-- =====================================================
INSERT INTO `strategy` (`id`, `name`, `description`, `type`, `status`, `config`, `created_by`) VALUES
(1, 'AI Test Case Generation', 'Auto generate test cases', 'ai', 'ENABLED', '{\"model\":\"gpt-4\",\"maxTokens\":2000}', 1),
(2, 'Defect Smart Analysis', 'Auto analyze defect root cause', 'ai', 'ENABLED', '{\"model\":\"claude-3\",\"confidence\":0.85}', 1),
(3, 'Test Report Generation', 'Auto generate test report', 'ai', 'DISABLED', '{\"model\":\"gemini\",\"format\":\"html\"}', 2),
(4, 'Requirement Priority Evaluation', 'AI evaluate requirement priority', 'ai', 'ENABLED', '{\"model\":\"qwen\",\"threshold\":0.7}', 1)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description);

-- =====================================================
-- 17. Test Coverage Data
-- =====================================================
INSERT INTO `coverage` (`id`, `project_id`, `covered_lines`, `total_lines`, `coverage_rate`, `report_date`) VALUES
(1, 1, 8500, 10000, 85.00, '2026-05-28'),
(2, 3, 6200, 8000, 77.50, '2026-05-28'),
(3, 4, 1500, 5000, 30.00, '2026-05-28'),
(4, 2, 9000, 9500, 94.74, '2026-05-28')
ON DUPLICATE KEY UPDATE covered_lines=VALUES(covered_lines), total_lines=VALUES(total_lines), coverage_rate=VALUES(coverage_rate);

-- =====================================================
-- 18. Todo Data
-- =====================================================
INSERT INTO `todo` (`id`, `title`, `description`, `completed`, `deadline`, `priority`, `created_by`) VALUES
(1, 'Complete Test Plan Document', 'Write Q1 test plan', 0, '2026-06-20', 'P1', 1),
(2, 'Fix Login Bug', 'Login page style issue', 1, '2026-05-15', 'P0', 1),
(3, 'Code Review', 'Review payment module code', 0, '2026-05-18', 'P2', 2),
(4, 'Prepare Test Environment', 'Deploy test server', 0, '2026-05-25', 'P1', 1),
(5, 'Write API Documentation', 'API interface documentation', 1, '2026-05-10', 'P2', 2)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

SELECT 'Database initialization data completed!' AS message;
SELECT 'All test user password: Admin@123' AS password_info;

