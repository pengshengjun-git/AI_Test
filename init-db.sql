-- 创建数据库
DROP DATABASE IF EXISTS ai_test_platform;
CREATE DATABASE ai_test_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_test_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(256) NOT NULL,
  email VARCHAR(128),
  role VARCHAR(32) DEFAULT 'USER',
  status VARCHAR(32) DEFAULT 'ACTIVE',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);

-- 项目表
CREATE TABLE IF NOT EXISTS project (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  description TEXT,
  status VARCHAR(32) DEFAULT 'PLANNING',
  priority VARCHAR(32) DEFAULT 'P2',
  created_by BIGINT,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 需求表
CREATE TABLE IF NOT EXISTS requirement (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  project_id BIGINT NOT NULL,
  title VARCHAR(256) NOT NULL,
  description TEXT,
  type VARCHAR(32) DEFAULT 'functional',
  priority VARCHAR(32) DEFAULT 'P2',
  status VARCHAR(32) DEFAULT 'draft',
  source VARCHAR(32),
  document_url VARCHAR(512),
  ai_analyzed TINYINT DEFAULT 0,
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (project_id) REFERENCES project(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 测试用例表
CREATE TABLE IF NOT EXISTS testcase (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(512) NOT NULL,
  description TEXT,
  priority VARCHAR(32) DEFAULT 'P2',
  type VARCHAR(32) DEFAULT 'functional',
  status VARCHAR(32) DEFAULT 'pending',
  test_status VARCHAR(32) DEFAULT 'pending',
  test_module VARCHAR(128),
  requirement_id BIGINT,
  project_id BIGINT,
  steps TEXT,
  expected_result TEXT,
  actual_result TEXT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (requirement_id) REFERENCES requirement(id),
  FOREIGN KEY (project_id) REFERENCES project(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 缺陷表
CREATE TABLE IF NOT EXISTS defect (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(512) NOT NULL,
  description TEXT,
  priority VARCHAR(32) DEFAULT 'P2',
  status VARCHAR(32) DEFAULT 'open',
  type VARCHAR(32) DEFAULT 'bug',
  severity VARCHAR(32) DEFAULT 'medium',
  requirement_id BIGINT,
  project_id BIGINT,
  assignee BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (requirement_id) REFERENCES requirement(id),
  FOREIGN KEY (project_id) REFERENCES project(id),
  FOREIGN KEY (assignee) REFERENCES user(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 测试计划表
CREATE TABLE IF NOT EXISTS test_plan (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  description TEXT,
  project_id BIGINT,
  start_date DATE,
  end_date DATE,
  status VARCHAR(32) DEFAULT 'draft',
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (project_id) REFERENCES project(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 测试覆盖率表
CREATE TABLE IF NOT EXISTS coverage (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  project_id BIGINT NOT NULL,
  covered_lines BIGINT DEFAULT 0,
  total_lines BIGINT DEFAULT 0,
  coverage_rate DECIMAL(5,2) DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (project_id) REFERENCES project(id)
);

-- 策略表
CREATE TABLE IF NOT EXISTS strategy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  description TEXT,
  type VARCHAR(32) DEFAULT 'ai',
  status VARCHAR(32) DEFAULT 'ENABLED',
  config JSON,
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 待办事项表
CREATE TABLE IF NOT EXISTS todo (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(512) NOT NULL,
  description TEXT,
  completed TINYINT DEFAULT 0,
  deadline DATE,
  priority VARCHAR(32) DEFAULT 'P2',
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- 插入测试数据

-- 用户数据
INSERT INTO user (username, password_hash, email, role, status) VALUES
('admin', 'admin123', 'admin@example.com', 'ADMIN', 'ACTIVE'),
('testuser', 'test123', 'test@example.com', 'USER', 'ACTIVE'),
('developer', 'dev123', 'dev@example.com', 'USER', 'ACTIVE');

-- 项目数据
INSERT INTO project (name, description, status, priority, created_by) VALUES
('电商平台测试项目', '电商平台全链路测试', 'ACTIVE', 'P0', 1),
('用户中心系统', '用户登录注册模块测试', 'ACTIVE', 'P1', 1),
('支付模块测试', '第三方支付接口测试', 'PLANNING', 'P0', 1),
('数据分析平台', '大数据分析系统测试', 'COMPLETED', 'P2', 2),
('后台管理系统', '管理后台功能测试', 'ACTIVE', 'P1', 2),
('移动App测试', '移动端应用测试', 'PLANNING', 'P1', 1),
('API网关测试', 'API接口性能测试', 'ACTIVE', 'P2', 2),
('消息队列测试', 'MQ消息可靠性测试', 'COMPLETED', 'P3', 1);

-- 需求数据
INSERT INTO requirement (project_id, title, description, type, priority, status, created_by) VALUES
(1, '用户登录功能', '实现用户名密码登录', 'functional', 'P0', 'completed', 1),
(1, '商品搜索功能', '实现关键词搜索', 'functional', 'P1', 'in_progress', 1),
(1, '购物车功能', '添加商品到购物车', 'functional', 'P1', 'in_progress', 2),
(1, '订单管理', '订单创建支付流程', 'functional', 'P0', 'completed', 1),
(2, '用户注册', '新用户注册功能', 'functional', 'P0', 'completed', 1),
(2, '密码找回', '忘记密码重置', 'functional', 'P2', 'pending', 2),
(2, '权限管理', '角色权限控制', 'functional', 'P1', 'in_progress', 1),
(3, '支付回调', '第三方支付回调处理', 'functional', 'P0', 'pending', 1),
(3, '退款功能', '订单退款流程', 'functional', 'P1', 'pending', 2),
(4, '数据报表', '生成数据分析报表', 'functional', 'P2', 'completed', 1);

-- 测试用例数据
INSERT INTO testcase (title, description, priority, type, test_status, test_module, requirement_id, project_id, steps, expected_result, created_by) VALUES
('登录成功测试', '验证正确用户名密码登录', 'P0', 'functional', 'passed', '登录模块', 1, 1, '1.输入admin/admin123\n2.点击登录', '登录成功', 1),
('登录失败测试', '验证错误密码登录', 'P0', 'functional', 'passed', '登录模块', 1, 1, '1.输入admin/wrong\n2.点击登录', '提示密码错误', 1),
('搜索空结果', '搜索不存在的商品', 'P2', 'functional', 'pending', '搜索模块', 2, 1, '1.输入无效关键词\n2.点击搜索', '显示无结果', 2),
('添加购物车', '添加商品到购物车', 'P1', 'functional', 'testing', '购物车模块', 3, 1, '1.选择商品\n2.点击加入购物车', '商品添加成功', 1),
('创建订单', '创建新订单', 'P0', 'functional', 'passed', '订单模块', 4, 1, '1.选择商品\n2.提交订单', '订单创建成功', 1),
('注册成功', '新用户注册', 'P0', 'functional', 'passed', '注册模块', 5, 2, '1.填写注册信息\n2.提交', '注册成功', 1),
('权限验证', '验证权限控制', 'P1', 'functional', 'testing', '权限模块', 7, 2, '1.访问受限页面', '提示无权限', 2),
('支付回调测试', '模拟支付回调', 'P0', 'integration', 'pending', '支付模块', 8, 3, '1.发起支付\n2.模拟回调', '订单状态更新', 1);

-- 缺陷数据
INSERT INTO defect (title, description, priority, status, severity, requirement_id, project_id, created_by) VALUES
('登录页面样式错乱', '登录按钮对齐问题', 'P3', 'open', 'low', 1, 1, 1),
('搜索结果显示不全', '搜索结果只显示前10条', 'P2', 'open', 'medium', 2, 1, 2),
('购物车数量错误', '添加商品数量不正确', 'P1', 'in_progress', 'high', 3, 1, 1),
('订单超时问题', '订单30分钟未支付未自动取消', 'P0', 'open', 'critical', 4, 1, 1),
('注册邮箱验证失败', '邮箱格式验证不严格', 'P2', 'closed', 'medium', 5, 2, 2),
('权限绕过漏洞', '可访问未授权页面', 'P0', 'in_progress', 'critical', 7, 2, 1),
('支付回调重复处理', '同一回调被多次处理', 'P0', 'open', 'critical', 8, 3, 1),
('退款失败', '部分退款金额错误', 'P1', 'open', 'high', 9, 3, 2);

-- 测试计划数据
INSERT INTO test_plan (name, description, project_id, start_date, end_date, status, created_by) VALUES
('Sprint 1测试计划', '第一阶段测试任务', 1, '2024-01-01', '2024-01-15', 'in_progress', 1),
('支付模块测试计划', '支付功能专项测试', 3, '2024-02-01', '2024-02-15', 'draft', 1),
('用户中心测试计划', '用户模块全面测试', 2, '2024-01-15', '2024-01-30', 'completed', 2);

-- 策略数据
INSERT INTO strategy (name, description, type, status, config, created_by) VALUES
('AI测试用例生成', '自动生成测试用例', 'ai', 'ENABLED', '{"model":"gpt-4","maxTokens":2000}', 1),
('缺陷智能分析', '自动分析缺陷根因', 'ai', 'ENABLED', '{"model":"claude-3","confidence":0.85}', 1),
('测试报告生成', '自动生成测试报告', 'ai', 'DISABLED', '{"model":"gemini","format":"html"}', 2),
('需求优先级评估', 'AI评估需求优先级', 'ai', 'ENABLED', '{"model":"qwen","threshold":0.7}', 1);

-- 待办事项数据
INSERT INTO todo (title, description, completed, deadline, priority, created_by) VALUES
('完成测试计划文档', '编写Q1测试计划', 0, '2024-01-20', 'P1', 1),
('修复登录bug', '登录页面样式问题', 1, '2024-01-15', 'P0', 1),
('代码审查', '审查支付模块代码', 0, '2024-01-18', 'P2', 2),
('准备测试环境', '部署测试服务器', 0, '2024-01-25', 'P1', 1),
('编写接口文档', 'API接口文档', 1, '2024-01-10', 'P2', 2);

-- 测试覆盖率数据
INSERT INTO coverage (project_id, covered_lines, total_lines, coverage_rate) VALUES
(1, 8500, 10000, 85.00),
(2, 6200, 8000, 77.50),
(3, 1500, 5000, 30.00),
(4, 9000, 9500, 94.74);

SELECT '数据库初始化完成' AS result;