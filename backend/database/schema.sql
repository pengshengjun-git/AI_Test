-- =====================================================
-- AI测试管理平台 - 数据库表结构脚本
-- 数据库版本: MySQL 8.0+
-- 创建日期: 2026-05-28
-- 注意: 表结构与Java实体类完全对齐
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_test_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_test_platform;

-- =====================================================
-- 1. 用户与权限模块
-- =====================================================

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password_hash` VARCHAR(256) NOT NULL COMMENT '密码哈希',
    `email` VARCHAR(128) COMMENT '邮箱',
    `phone` VARCHAR(32) COMMENT '手机号',
    `real_name` VARCHAR(64) COMMENT '真实姓名',
    `avatar` VARCHAR(512) COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `mfa_enabled` TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用MFA: 0-否, 1-是',
    `department_id` BIGINT COMMENT '部门ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(64) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(256) COMMENT '角色描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name` VARCHAR(128) NOT NULL COMMENT '权限名称',
    `code` VARCHAR(128) NOT NULL COMMENT '权限编码',
    `type` VARCHAR(32) NOT NULL COMMENT '权限类型: menu-菜单, api-接口, data-数据',
    `resource` VARCHAR(512) COMMENT '资源路径',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `description` VARCHAR(256) COMMENT '权限描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 部门表
CREATE TABLE IF NOT EXISTS `department` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
    `name` VARCHAR(128) NOT NULL COMMENT '部门名称',
    `code` VARCHAR(64) NOT NULL COMMENT '部门编码',
    `leader_id` BIGINT COMMENT '负责人ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `description` VARCHAR(512) COMMENT '部门描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_leader_id` (`leader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 菜单表
CREATE TABLE IF NOT EXISTS `menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `name` VARCHAR(128) NOT NULL COMMENT '菜单名称',
    `code` VARCHAR(128) NOT NULL COMMENT '菜单编码',
    `path` VARCHAR(256) COMMENT '路由路径',
    `component` VARCHAR(256) COMMENT '组件路径',
    `icon` VARCHAR(128) COMMENT '图标',
    `type` INT NOT NULL DEFAULT 0 COMMENT '类型: 0-目录, 1-菜单, 2-按钮',
    `permission` VARCHAR(256) COMMENT '权限标识',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见: 0-隐藏, 1-显示',
    `cacheable` TINYINT NOT NULL DEFAULT 1 COMMENT '是否缓存: 0-否, 1-是',
    `redirect` VARCHAR(256) COMMENT '重定向路径',
    `description` VARCHAR(512) COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `permission_code` VARCHAR(256) COMMENT '权限编码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_menu_code` (`code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS `role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- =====================================================
-- 2. 项目管理模块
-- =====================================================

-- 项目表 (与Project实体类对齐)
CREATE TABLE IF NOT EXISTS `project` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '项目ID',
    `name` VARCHAR(128) NOT NULL COMMENT '项目名称',
    `code` VARCHAR(64) NOT NULL COMMENT '项目编码',
    `description` TEXT COMMENT '项目描述',
    `status` VARCHAR(32) NOT NULL DEFAULT 'PLANNING' COMMENT '状态: PLANNING-规划中, IN_PROGRESS-进行中, COMPLETED-已完成, ARCHIVED-已归档',
    `priority` VARCHAR(32) DEFAULT 'P2' COMMENT '优先级: P0-P3',
    `created_by` BIGINT COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_code` (`code`),
    KEY `idx_status` (`status`),
    KEY `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目表';

-- 项目成员表
CREATE TABLE IF NOT EXISTS `project_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` VARCHAR(32) NOT NULL COMMENT '项目角色: owner, manager, member, viewer',
    `joined_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `created_by` BIGINT COMMENT '添加人ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_user` (`project_id`, `user_id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目成员表';

-- =====================================================
-- 3. 需求管理模块
-- =====================================================

-- 需求表
CREATE TABLE IF NOT EXISTS `requirement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '需求ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `title` VARCHAR(256) NOT NULL COMMENT '需求标题',
    `description` TEXT COMMENT '需求描述',
    `type` VARCHAR(32) NOT NULL DEFAULT 'functional' COMMENT '需求类型: functional-功能, performance-性能, security-安全, usability-易用性',
    `priority` VARCHAR(32) NOT NULL DEFAULT 'P2' COMMENT '优先级: P0, P1, P2, P3',
    `status` VARCHAR(32) NOT NULL DEFAULT 'draft' COMMENT '状态: draft-草稿, reviewing-评审中, approved-已批准, implemented-已实现, closed-已关闭',
    `source` VARCHAR(32) COMMENT '需求来源: manual-手动, import-导入, jira-Jira, confluence-Confluence',
    `document_url` VARCHAR(512) COMMENT '关联文档URL',
    `ai_analyzed` TINYINT NOT NULL DEFAULT 0 COMMENT '是否AI解析过: 0-否, 1-是',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需求表';

-- 需求测试点表
CREATE TABLE IF NOT EXISTS `requirement_test_point` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `requirement_id` BIGINT NOT NULL COMMENT '需求ID',
    `content` TEXT NOT NULL COMMENT '测试点内容',
    `type` VARCHAR(32) COMMENT '类型: function-功能, boundary-边界, exception-异常, security-安全',
    `ai_generated` TINYINT NOT NULL DEFAULT 1 COMMENT '是否AI生成: 0-否, 1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_requirement_id` (`requirement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需求测试点表';

-- =====================================================
-- 4. 测试用例模块 (与Testcase实体类对齐)
-- =====================================================

-- 测试用例表
CREATE TABLE IF NOT EXISTS `testcase` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用例ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `requirement_id` BIGINT COMMENT '关联需求ID',
    `title` VARCHAR(256) NOT NULL COMMENT '用例标题',
    `description` TEXT COMMENT '用例描述',
    `preconditions` TEXT COMMENT '前置条件',
    `priority` VARCHAR(32) NOT NULL DEFAULT 'P2' COMMENT '优先级: P0, P1, P2, P3',
    `type` VARCHAR(32) NOT NULL DEFAULT 'functional' COMMENT '用例类型: functional-功能, boundary-边界, exception-异常, security-安全, api-接口, performance-性能, compatibility-兼容性',
    `status` VARCHAR(32) NOT NULL DEFAULT 'draft' COMMENT '状态: draft-草稿, reviewed-已评审, approved-已批准, deprecated-已废弃',
    `test_status` VARCHAR(32) DEFAULT 'pending' COMMENT '执行状态: pending-未执行, running-执行中, passed-通过, failed-失败, blocked-阻塞, skipped-跳过',
    `test_module` VARCHAR(128) COMMENT '测试模块',
    `steps` TEXT COMMENT '测试步骤',
    `expected_result` TEXT COMMENT '预期结果',
    `actual_result` TEXT COMMENT '实际结果',
    `ai_generated` INT DEFAULT 0 COMMENT '是否AI生成: 0-否, 1-是',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_requirement_id` (`requirement_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试用例表';

-- 用例步骤表
CREATE TABLE IF NOT EXISTS `testcase_step` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
    `testcase_id` BIGINT NOT NULL COMMENT '用例ID',
    `step_number` INT NOT NULL COMMENT '步骤序号',
    `action` TEXT NOT NULL COMMENT '操作步骤',
    `expected_result` TEXT NOT NULL COMMENT '预期结果',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_testcase_id` (`testcase_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用例步骤表';

-- =====================================================
-- 5. 测试执行模块
-- =====================================================

-- 执行记录表
CREATE TABLE IF NOT EXISTS `testcase_execution` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '执行ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `testcase_id` BIGINT NOT NULL COMMENT '用例ID',
    `task_id` BIGINT COMMENT '执行任务ID',
    `status` VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '执行状态: pending-待执行, running-执行中, passed-通过, failed-失败, blocked-阻塞, skipped-跳过',
    `execution_type` VARCHAR(32) NOT NULL DEFAULT 'manual' COMMENT '执行类型: manual-手工, automated-自动化',
    `executor_id` BIGINT COMMENT '执行人ID',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `duration` BIGINT COMMENT '执行时长(毫秒)',
    `actual_result` TEXT COMMENT '实际结果',
    `error_message` TEXT COMMENT '错误信息',
    `screenshot_urls` TEXT COMMENT '截图URL,逗号分隔',
    `log_url` VARCHAR(512) COMMENT '日志URL',
    `environment` VARCHAR(512) COMMENT '环境信息JSON',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_testcase_id` (`testcase_id`),
    KEY `idx_status` (`status`),
    KEY `idx_executor_id` (`executor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='执行记录表';

-- 执行任务表
CREATE TABLE IF NOT EXISTS `execution_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `name` VARCHAR(256) NOT NULL COMMENT '任务名称',
    `description` TEXT COMMENT '任务描述',
    `type` VARCHAR(32) NOT NULL DEFAULT 'manual' COMMENT '任务类型: manual-手工, automated-自动化, performance-性能, security-安全',
    `status` VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '任务状态: pending-待执行, running-执行中, completed-已完成, failed-失败',
    `testcase_ids` TEXT COMMENT '用例ID列表,逗号分隔',
    `environment` VARCHAR(512) COMMENT '环境信息JSON',
    `schedule_type` VARCHAR(32) COMMENT '调度类型: immediate-立即, cron-定时',
    `cron_expression` VARCHAR(64) COMMENT 'Cron表达式',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='执行任务表';

-- =====================================================
-- 6. 缺陷管理模块 (与Defect实体类对齐)
-- =====================================================

-- 缺陷表
CREATE TABLE IF NOT EXISTS `defect` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '缺陷ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `title` VARCHAR(512) NOT NULL COMMENT '缺陷标题',
    `description` TEXT COMMENT '缺陷描述',
    `priority` VARCHAR(32) NOT NULL DEFAULT 'P2' COMMENT '优先级: P0, P1, P2, P3',
    `status` VARCHAR(32) NOT NULL DEFAULT 'new' COMMENT '状态: new-新建, confirmed-已确认, assigned-已分配, fixing-修复中, fixed-已修复, verified-已验证, closed-已关闭, reopened-已重开',
    `type` VARCHAR(32) DEFAULT 'bug' COMMENT '缺陷类型',
    `severity` VARCHAR(32) DEFAULT 'medium' COMMENT '严重程度: critical-严重, high-高, medium-中, low-低',
    `module` VARCHAR(128) COMMENT '所属模块',
    `steps_to_reproduce` TEXT COMMENT '复现步骤',
    `expected_result` TEXT COMMENT '预期结果',
    `actual_result` TEXT COMMENT '实际结果',
    `requirement_id` BIGINT COMMENT '关联需求ID',
    `reporter_id` BIGINT NOT NULL COMMENT '报告人ID',
    `assignee` BIGINT COMMENT '处理人ID',
    `created_by` BIGINT COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_severity` (`severity`),
    KEY `idx_reporter_id` (`reporter_id`),
    KEY `idx_assignee` (`assignee`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='缺陷表';

-- 缺陷评论表
CREATE TABLE IF NOT EXISTS `defect_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `defect_id` BIGINT NOT NULL COMMENT '缺陷ID',
    `user_id` BIGINT NOT NULL COMMENT '评论人ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_defect_id` (`defect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='缺陷评论表';

-- =====================================================
-- 7. AI任务模块
-- =====================================================

-- AI任务表
CREATE TABLE IF NOT EXISTS `ai_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AI任务ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `type` VARCHAR(32) NOT NULL COMMENT '任务类型: requirement_analysis-需求分析, case_generate-用例生成, defect_analysis-缺陷分析, report_generate-报告生成, risk_analysis-风险分析',
    `status` VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '任务状态: pending-待处理, processing-处理中, completed-已完成, failed-失败',
    `input_data` TEXT COMMENT '输入数据JSON',
    `output_data` TEXT COMMENT '输出数据JSON',
    `model` VARCHAR(64) COMMENT '使用的AI模型',
    `prompt_version` VARCHAR(32) COMMENT 'Prompt版本',
    `token_used` INT COMMENT '消耗Token数',
    `cost` DECIMAL(10,4) COMMENT '成本',
    `error_message` TEXT COMMENT '错误信息',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `started_at` DATETIME COMMENT '开始时间',
    `completed_at` DATETIME COMMENT '完成时间',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI任务表';

-- AI调用记录表
CREATE TABLE IF NOT EXISTS `ai_call_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `project_id` BIGINT COMMENT '项目ID',
    `function_type` VARCHAR(64) NOT NULL COMMENT '功能类型',
    `model_name` VARCHAR(128) NOT NULL COMMENT '模型名称',
    `prompt_tokens` INT DEFAULT 0 COMMENT 'Prompt Token数',
    `completion_tokens` INT DEFAULT 0 COMMENT 'Completion Token数',
    `total_tokens` INT DEFAULT 0 COMMENT '总Token数',
    `status` VARCHAR(32) NOT NULL DEFAULT 'success' COMMENT '状态',
    `error_message` TEXT COMMENT '错误信息',
    `response_time` BIGINT COMMENT '响应时间(毫秒)',
    `request_id` VARCHAR(128) COMMENT '请求ID',
    `input_data` TEXT COMMENT '输入数据JSON',
    `output_data` TEXT COMMENT '输出数据JSON',
    `cost` DECIMAL(10,6) COMMENT '成本',
    `ip_address` VARCHAR(64) COMMENT 'IP地址',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_function_type` (`function_type`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI调用记录表';

-- =====================================================
-- 8. 测试计划与覆盖率模块
-- =====================================================

-- 测试计划表
CREATE TABLE IF NOT EXISTS `test_plan` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '计划ID',
    `name` VARCHAR(256) NOT NULL COMMENT '计划名称',
    `description` TEXT COMMENT '计划描述',
    `project_id` BIGINT COMMENT '项目ID',
    `start_date` DATE COMMENT '开始日期',
    `end_date` DATE COMMENT '结束日期',
    `status` VARCHAR(32) NOT NULL DEFAULT 'draft' COMMENT '状态: draft-草稿, in_progress-进行中, completed-已完成',
    `owner` VARCHAR(100) COMMENT '负责人',
    `created_by` BIGINT COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试计划表';

-- 测试覆盖率表
CREATE TABLE IF NOT EXISTS `coverage` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '覆盖率ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `covered_lines` BIGINT DEFAULT 0 COMMENT '覆盖行数',
    `total_lines` BIGINT DEFAULT 0 COMMENT '总行数',
    `coverage_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '覆盖率',
    `report_date` DATE COMMENT '报告日期',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试覆盖率表';

-- =====================================================
-- 9. 策略模块
-- =====================================================

-- 策略表
CREATE TABLE IF NOT EXISTS `strategy` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '策略ID',
    `name` VARCHAR(256) NOT NULL COMMENT '策略名称',
    `description` TEXT COMMENT '策略描述',
    `type` VARCHAR(32) NOT NULL DEFAULT 'ai' COMMENT '策略类型: ai-AI策略, test-测试策略',
    `status` VARCHAR(32) NOT NULL DEFAULT 'ENABLED' COMMENT '状态: ENABLED-启用, DISABLED-禁用',
    `config` JSON COMMENT '配置JSON',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='策略表';

-- =====================================================
-- 10. 报告模块
-- =====================================================

-- 报告表
CREATE TABLE IF NOT EXISTS `report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报告ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `name` VARCHAR(256) NOT NULL COMMENT '报告名称',
    `type` VARCHAR(32) NOT NULL COMMENT '报告类型: test_summary-测试总结, defect-缺陷分析, risk-风险分析, coverage-覆盖率',
    `status` VARCHAR(32) NOT NULL DEFAULT 'generating' COMMENT '状态: generating-生成中, ready-就绪, failed-失败',
    `content` TEXT COMMENT '报告内容JSON',
    `file_url` VARCHAR(512) COMMENT '报告文件URL',
    `ai_generated` TINYINT NOT NULL DEFAULT 0 COMMENT '是否AI生成: 0-否, 1-是',
    `ai_task_id` BIGINT COMMENT 'AI任务ID',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报告表';

-- =====================================================
-- 11. 审计日志模块
-- =====================================================

-- 操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `project_id` BIGINT COMMENT '项目ID',
    `user_id` BIGINT NOT NULL COMMENT '操作用户ID',
    `module` VARCHAR(64) NOT NULL COMMENT '模块',
    `operation` VARCHAR(64) NOT NULL COMMENT '操作类型',
    `description` VARCHAR(512) COMMENT '操作描述',
    `request_url` VARCHAR(512) COMMENT '请求URL',
    `request_method` VARCHAR(16) COMMENT '请求方法',
    `request_params` TEXT COMMENT '请求参数JSON',
    `response_result` TEXT COMMENT '响应结果JSON',
    `ip_address` VARCHAR(64) COMMENT 'IP地址',
    `user_agent` VARCHAR(512) COMMENT 'User Agent',
    `execution_time` BIGINT COMMENT '执行时长(毫秒)',
    `trace_id` VARCHAR(64) COMMENT '链路追踪ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =====================================================
-- 12. 待办事项模块
-- =====================================================

-- 待办事项表
CREATE TABLE IF NOT EXISTS `todo` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '待办ID',
    `title` VARCHAR(512) NOT NULL COMMENT '待办标题',
    `description` TEXT COMMENT '待办描述',
    `completed` TINYINT DEFAULT 0 COMMENT '是否完成',
    `deadline` DATE COMMENT '截止日期',
    `priority` VARCHAR(32) DEFAULT 'P2' COMMENT '优先级',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';

SELECT '数据库表结构创建完成！' AS message;