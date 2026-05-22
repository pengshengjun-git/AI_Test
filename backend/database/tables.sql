USE ai_test_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
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
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
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
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
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
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
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
  FOREIGN KEY (assignee) REFERENCES `user`(id),
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
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
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
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
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
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
  FOREIGN KEY (created_by) REFERENCES `user`(id),
  FOREIGN KEY (updated_by) REFERENCES `user`(id)
);

-- 插入初始用户
INSERT INTO `user` (username, password_hash, email, role, status) VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'admin@example.com', 'ADMIN', 'ACTIVE');
INSERT INTO `user` (username, password_hash, email, role, status) VALUES ('testuser', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'test@example.com', 'USER', 'ACTIVE');