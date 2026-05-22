# 🔑 给AI授权启动服务的方法

## 为什么需要授权？

当前配置中：
- ❌ AI无法执行Docker命令
- ❌ AI无法修改自己的配置文件
- ✅ 你手动修改配置后，AI就能帮你启动服务了

---

## 📝 授权步骤（超简单！）

### 第1步：备份原配置
先复制 `trae_config.yaml` 备份一份

### 第2步：用授权版替换
打开 `trae_config_for_ai.yaml`，**全选复制**所有内容，然后**覆盖** `trae_config.yaml`

或者直接打开 `trae_config.yaml` 手动修改：

#### 要修改的地方1（开放Docker命令）：
```yaml
# 原来这样：
Bash:
    allow: []

# 改成这样：
Bash:
    allow:
    - docker compose*
    - docker*
    - cd*
    - dir*
```

#### 要修改的地方2（让AI可以改配置）：
```yaml
# 原来这样：
blocked_files:
- AGENTS.md
- trae_config.yaml  # ← 删除这一行

# 改成这样（删除或注释掉trae_config.yaml）：
blocked_files:
- AGENTS.md
# - trae_config.yaml  # ← 已删除或注释
```

### 第3步：告诉AI改完了
在对话框说：
> "我改完了，你试试"

AI就可以帮你启动服务了！

---

## 🎯 授权后AI能做什么？

| 功能 | 授权前 | 授权后 |
|-----|-------|-------|
| 启动Docker服务 | ❌ 不行 | ✅ 可以 |
| 查看服务状态 | ❌ 不行 | ✅ 可以 |
| 查看服务日志 | ❌ 不行 | ✅ 可以 |
| 停止服务 | ❌ 不行 | ✅ 可以 |

---

## 🚨 想撤销授权？

把 `trae_config.yaml` 恢复成原来的样子就行！

或者用你备份的文件覆盖。

---

## 📂 相关文件

| 文件 | 说明 |
|-----|------|
| `trae_config.yaml` | 当前配置文件（需要你改） |
| `trae_config_for_ai.yaml` | 授权版配置（已准备好，直接复制） |
