"""
与前端、Java 网关一致的 BFF 接口：统一 { code, message, data }，承接 dashboard / strategy / ai。
"""
from __future__ import annotations

import copy
import itertools
from datetime import datetime
from typing import Any, Dict, List, Optional

from fastapi import APIRouter

router = APIRouter(tags=["platform-bff"])

_id_seq = itertools.count(100)


def ok(data: Any = None, message: str = "success") -> dict:
    return {"code": 0, "message": message, "data": data}


def fail(code: int, message: str) -> dict:
    return {"code": code, "message": message, "data": None}


# ----- 种子数据（与前端 mock 对齐） -----
_SEED_PROJECTS = [
    {"id": 1, "name": "电商平台v2.0", "code": "ECOM-V2", "status": "IN_PROGRESS", "description": "电商平台升级项目", "createdAt": "2026-05-01 10:00:00"},
    {"id": 2, "name": "支付系统重构", "code": "PAY-REF", "status": "IN_PROGRESS", "description": "支付系统架构重构", "createdAt": "2026-05-03 14:00:00"},
    {"id": 3, "name": "用户中心优化", "code": "UC-OPT", "status": "PLANNING", "description": "用户中心性能优化", "createdAt": "2026-05-05 09:00:00"},
    {"id": 4, "name": "数据分析平台", "code": "DATA-ANALYTICS", "status": "COMPLETED", "description": "数据分析平台建设", "createdAt": "2026-04-20 16:00:00"},
    {"id": 5, "name": "移动端App", "code": "MOBILE-APP", "status": "ARCHIVED", "description": "移动端应用开发", "createdAt": "2026-03-15 11:00:00"},
]

_SEED_DEFECTS = [
    {"id": 1, "title": "登录接口返回数据异常", "priority": "P0", "severity": "CRITICAL", "status": "FIXING", "projectId": 1, "creator": "admin", "createdAt": "2026-05-08 10:00:00"},
    {"id": 2, "title": "商品列表分页显示错误", "priority": "P1", "severity": "HIGH", "status": "CONFIRMING", "projectId": 1, "creator": "tester", "createdAt": "2026-05-09 14:00:00"},
    {"id": 3, "title": "支付回调超时", "priority": "P1", "severity": "HIGH", "status": "NEW", "projectId": 2, "creator": "dev", "createdAt": "2026-05-10 09:00:00"},
]

_todos: List[dict] = [
    {"id": 1, "title": "完成测试计划文档", "completed": False, "deadline": "2026-05-12"},
    {"id": 2, "title": "评审测试用例", "completed": False, "deadline": "2026-05-11"},
    {"id": 3, "title": "执行回归测试", "completed": True, "deadline": "2026-05-10"},
]

_strategies: Dict[int, dict] = {}
for s in [
    {"id": 1, "name": "核心功能冒烟测试", "type": "SMOKE", "description": "每次部署后对核心功能进行快速验证", "priority": "P0", "status": "ENABLED", "creator": "admin", "createTime": "2026-05-01 10:30:00"},
    {"id": 2, "name": "完整回归测试策略", "type": "REGRESSION", "description": "发版前执行完整回归测试", "priority": "P1", "status": "ENABLED", "creator": "admin", "createTime": "2026-05-02 14:00:00"},
    {"id": 3, "name": "性能基准测试", "type": "PERFORMANCE", "description": "定期执行性能测试", "priority": "P2", "status": "DISABLED", "creator": "tester", "createTime": "2026-05-03 09:00:00"},
]:
    _strategies[s["id"]] = copy.deepcopy(s)

_ai_tasks: List[dict] = [
    {"id": 1, "name": "电商平台用例生成", "type": "CASE_GENERATE", "status": "COMPLETED", "progress": 100, "createTime": "2026-05-09 10:30:00"},
    {"id": 2, "name": "支付系统需求解析", "type": "REQUIREMENT_PARSE", "status": "COMPLETED", "progress": 100, "createTime": "2026-05-09 11:00:00"},
]

_ai_model_config: dict = {
    "modelName": "gpt-4",
    "temperature": 0.7,
    "maxTokens": 4096,
    "topP": 0.9,
}


def _paginate(items: List[Any], page: int, size: int) -> dict:
    page = max(1, page)
    size = max(1, min(100, size))
    start = (page - 1) * size
    return {"list": items[start : start + size], "total": len(items), "page": page, "size": size}


# ----- Dashboard -----
@router.get("/api/v1/dashboard/stats")
async def dashboard_stats():
    return ok({
        "projectCount": 5,
        "testcaseCount": 128,
        "defectCount": 15,
        "aiGeneratedCount": 45,
    })


@router.get("/api/v1/dashboard/recent-projects")
async def dashboard_recent_projects():
    return ok(copy.deepcopy(_SEED_PROJECTS[:5]))


@router.get("/api/v1/dashboard/recent-defects")
async def dashboard_recent_defects():
    return ok(copy.deepcopy(_SEED_DEFECTS[:5]))


@router.get("/api/v1/dashboard/system-status")
async def dashboard_system_status():
    return ok([
        {"name": "认证服务", "status": "online"},
        {"name": "项目服务", "status": "online"},
        {"name": "用例服务", "status": "online"},
        {"name": "AI服务", "status": "online"},
    ])


@router.get("/api/v1/dashboard/todo-list")
async def dashboard_todos():
    return ok(copy.deepcopy(_todos))


@router.put("/api/v1/dashboard/todo/{todo_id}/status")
async def dashboard_toggle_todo(todo_id: int, body: dict):
    for t in _todos:
        if t["id"] == todo_id:
            t["completed"] = bool(body.get("completed"))
            return ok(copy.deepcopy(t))
    return fail(404, "待办不存在")


# ----- Strategy -----
@router.get("/api/v1/strategy")
async def strategy_list(name: Optional[str] = None, type: Optional[str] = None, status: Optional[str] = None, page: int = 1, size: int = 10):
    rows = list(_strategies.values())
    if name:
        rows = [s for s in rows if name in s.get("name", "")]
    if type:
        rows = [s for s in rows if s.get("type") == type]
    if status:
        rows = [s for s in rows if s.get("status") == status]
    rows.sort(key=lambda x: x["id"])
    return ok(_paginate(rows, page, size))


@router.get("/api/v1/strategy/{strategy_id}")
async def strategy_get(strategy_id: int):
    s = _strategies.get(strategy_id)
    if not s:
        return fail(404, "策略不存在")
    return ok(copy.deepcopy(s))


@router.post("/api/v1/strategy")
async def strategy_create(body: dict):
    new_id = next(_id_seq)
    row = dict(body)
    row["id"] = new_id
    row.setdefault("name", "新策略")
    row.setdefault("type", "SMOKE")
    row.setdefault("priority", "P1")
    row.setdefault("status", "ENABLED")
    row["creator"] = "admin"
    row["createTime"] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    _strategies[new_id] = row
    return ok(copy.deepcopy(row))


@router.put("/api/v1/strategy/{strategy_id}")
async def strategy_update(strategy_id: int, body: dict):
    if strategy_id not in _strategies:
        return fail(404, "策略不存在")
    cur = _strategies[strategy_id]
    for k, v in body.items():
        if v is not None:
            cur[k] = v
    return ok(copy.deepcopy(cur))


@router.delete("/api/v1/strategy/{strategy_id}")
async def strategy_delete(strategy_id: int):
    if strategy_id not in _strategies:
        return fail(404, "策略不存在")
    del _strategies[strategy_id]
    return ok(None)


@router.put("/api/v1/strategy/{strategy_id}/status")
async def strategy_toggle(strategy_id: int, body: dict):
    if strategy_id not in _strategies:
        return fail(404, "策略不存在")
    st = body.get("status")
    if st:
        _strategies[strategy_id]["status"] = st
    return ok(copy.deepcopy(_strategies[strategy_id]))


# ----- AI（前端路径 + 统一封装） -----
@router.get("/api/v1/ai/tasks")
async def ai_tasks_list():
    return ok(copy.deepcopy(_ai_tasks))


@router.post("/api/v1/ai/tasks")
async def ai_tasks_create(body: dict):
    tid = int(datetime.now().timestamp() * 1000)
    row = {
        "id": tid,
        "name": body.get("name", "新任务"),
        "type": body.get("type", "CASE_GENERATE"),
        "status": "PENDING",
        "progress": 0,
        "createTime": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
    }
    _ai_tasks.insert(0, row)
    return ok(copy.deepcopy(row))


@router.get("/api/v1/ai/tasks/{task_id}")
async def ai_task_status(task_id: str):
    return ok({
        "task_id": task_id,
        "status": "completed",
        "progress": 100,
        "created_at": datetime.now().isoformat(),
    })


@router.get("/api/v1/ai/model/config")
async def ai_model_get():
    return ok(copy.deepcopy(_ai_model_config))


@router.put("/api/v1/ai/model/config")
async def ai_model_put(body: dict):
    global _ai_model_config
    _ai_model_config = {**_ai_model_config, **body}
    return ok(copy.deepcopy(_ai_model_config))


@router.post("/api/v1/ai/generate/testcases")
async def ai_generate_testcases_frontend(body: dict):
    """对接前端 /api/v1/ai/generate/testcases"""
    pid = body.get("projectId") or body.get("project_id") or 0
    task_id = f"task_{datetime.now().strftime('%Y%m%d%H%M%S')}"
    cases = [
        {
            "title": "登录成功测试",
            "priority": "P1",
            "preconditions": ["用户已注册"],
            "steps": ["打开登录页", "输入合法账号密码", "点击登录"],
            "expected_result": "进入首页",
            "tags": ["登录"],
            "ai_generated": True,
            "risk_score": 0.85,
        },
        {
            "title": "异常登录测试",
            "priority": "P2",
            "preconditions": [],
            "steps": ["错误密码连续提交"],
            "expected_result": "提示错误并限制频率",
            "tags": ["安全"],
            "ai_generated": True,
            "risk_score": 0.65,
        },
    ]
    return ok({
        "task_id": task_id,
        "status": "completed",
        "message": "AI生成测试用例成功",
        "projectId": pid,
        "test_cases": cases,
        "success": True,
        "count": len(cases),
    })


@router.post("/api/v1/ai/parse/requirement")
async def ai_parse_requirement_frontend(body: dict):
    content = body.get("requirement") or body.get("content") or ""
    return ok({
        "content": content,
        "function_points": ["用户登录", "用户注册", "密码重置"],
        "business_rules": ["密码长度不少于8位", "用户名不能重复"],
        "boundary_conditions": ["密码为空", "用户名为空"],
        "risk_points": ["暴力破解风险"],
        "test_points": ["正常登录流程", "异常登录场景"],
    })


@router.post("/api/v1/ai/analyze/defect")
async def ai_analyze_defect_frontend(body: dict):
    _ = body.get("defectId")
    return ok({
        "analysis": "根据AI分析，此缺陷可能与边界条件处理不当有关。",
        "suggestions": ["检查边界条件", "添加单元测试", "复核并发场景"],
        "similar_defects": [],
        "root_cause": "代码逻辑问题",
        "risk_level": "medium",
        "fix_suggestions": ["检查边界条件", "添加单元测试"],
    })


@router.post("/api/v1/ai/generate/report")
async def ai_generate_report_frontend(body: dict):
    return ok({
        "success": True,
        "message": "报告生成完成",
        "url": "/report/download/demo",
        "projectId": body.get("projectId"),
        "type": body.get("type"),
    })


# 保留旧路径（直接调用方）
@router.post("/api/v1/ai/cases/generate")
async def ai_cases_generate_legacy(body: dict):
    return await ai_generate_testcases_frontend({"projectId": body.get("project_id"), "project_id": body.get("project_id")})


@router.post("/api/v1/ai/requirements/parse")
async def ai_requirements_parse_legacy(body: dict):
    return await ai_parse_requirement_frontend({"content": body.get("content"), "requirement": body.get("content")})


@router.post("/api/v1/ai/defects/analyze")
async def ai_defects_analyze_legacy(body: dict):
    return await ai_analyze_defect_frontend({"defectId": body.get("defectId")})
