-- =====================================================
-- AI测试管理平台 - 数据库初始化脚本
-- 数据库版本: MySQL 8.0+
-- 创建日期: 2026-05-09
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
    UNIQUE KEY `uk_code` (`code`)
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
    UNIQUE KEY `uk_code` (`code`),
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

-- =====================================================
-- 2. 项目管理模块
-- =====================================================

-- 项目表
CREATE TABLE IF NOT EXISTS `project` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '项目ID',
    `name` VARCHAR(128) NOT NULL COMMENT '项目名称',
    `code` VARCHAR(64) NOT NULL COMMENT '项目编码',
    `description` TEXT COMMENT '项目描述',
    `owner_id` BIGINT NOT NULL COMMENT '项目负责人ID',
    `status` VARCHAR(32) NOT NULL DEFAULT 'active' COMMENT '状态: active-活跃, archived-归档',
    `visibility` VARCHAR(32) NOT NULL DEFAULT 'private' COMMENT '可见性: private-私有, public-公开',
    `start_date` DATE COMMENT '开始日期',
    `end_date` DATE COMMENT '结束日期',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_owner_id` (`owner_id`),
    KEY `idx_status` (`status`)
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
-- 4. 测试用例模块
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
    `tags` VARCHAR(512) COMMENT '标签,逗号分隔',
    `ai_generated` TINYINT NOT NULL DEFAULT 0 COMMENT '是否AI生成: 0-否, 1-是',
    `ai_task_id` BIGINT COMMENT 'AI任务ID',
    `risk_score` DECIMAL(5,2) COMMENT '风险评分 0-100',
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
-- 6. 缺陷管理模块
-- =====================================================

-- 缺陷表
CREATE TABLE IF NOT EXISTS `defect` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '缺陷ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `testcase_execution_id` BIGINT COMMENT '关联执行记录ID',
    `title` VARCHAR(512) NOT NULL COMMENT '缺陷标题',
    `description` TEXT COMMENT '缺陷描述',
    `steps_to_reproduce` TEXT COMMENT '复现步骤',
    `severity` VARCHAR(32) NOT NULL DEFAULT 'medium' COMMENT '严重程度: critical-严重, high-高, medium-中, low-低',
    `priority` VARCHAR(32) NOT NULL DEFAULT 'P2' COMMENT '优先级: P0, P1, P2, P3',
    `status` VARCHAR(32) NOT NULL DEFAULT 'new' COMMENT '状态: new-新建, confirmed-已确认, assigned-已分配, fixing-修复中, fixed-已修复, verified-已验证, closed-已关闭, reopened-已重开',
    `module` VARCHAR(128) COMMENT '所属模块',
    `assignee_id` BIGINT COMMENT '处理人ID',
    `reporter_id` BIGINT NOT NULL COMMENT '报告人ID',
    `screenshot_urls` TEXT COMMENT '截图URL,逗号分隔',
    `ai_analyzed` TINYINT NOT NULL DEFAULT 0 COMMENT '是否AI分析过: 0-否, 1-是',
    `ai_risk_level` VARCHAR(32) COMMENT 'AI风险等级: low, medium, high, critical',
    `ai_root_cause` TEXT COMMENT 'AI根因分析',
    `ai_suggestion` TEXT COMMENT 'AI修复建议',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_severity` (`severity`),
    KEY `idx_assignee_id` (`assignee_id`),
    KEY `idx_reporter_id` (`reporter_id`)
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

-- =====================================================
-- 8. 报告与统计模块
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
-- 9. 审计日志
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
