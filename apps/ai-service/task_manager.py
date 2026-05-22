"""
任务管理模块
"""
import asyncio
import uuid
import logging
from datetime import datetime
from typing import Dict, Any, List, Optional
from enum import Enum
from agents import RequirementParseAgent, TestcaseGenerateAgent, TestcaseReviewAgent, DefectAnalysisAgent

logger = logging.getLogger(__name__)


class TaskStatus(str, Enum):
    """任务状态枚举"""
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"


class TaskType(str, Enum):
    """任务类型枚举"""
    REQUIREMENT_PARSE = "requirement_parse"
    TESTCASE_GENERATE = "testcase_generate"
    TESTCASE_REVIEW = "testcase_review"
    DEFECT_ANALYSIS = "defect_analysis"


class Task:
    """任务类"""
    
    def __init__(
        self,
        task_id: str,
        task_type: TaskType,
        params: Dict[str, Any]
    ):
        self.task_id = task_id
        self.task_type = task_type
        self.params = params
        self.status = TaskStatus.PENDING
        self.progress = 0
        self.result: Optional[Dict[str, Any]] = None
        self.error: Optional[str] = None
        self.created_at = datetime.now()
        self.started_at: Optional[datetime] = None
        self.completed_at: Optional[datetime] = None


class TaskManager:
    """任务管理器"""
    
    def __init__(self):
        self.tasks: Dict[str, Task] = {}
        self._lock = asyncio.Lock()
    
    async def create_task(
        self,
        task_type: TaskType,
        params: Dict[str, Any],
        model_type: Optional[str] = None
    ) -> str:
        """
        创建任务
        
        Args:
            task_type: 任务类型
            params: 任务参数
            model_type: 模型类型
            
        Returns:
            任务ID
        """
        task_id = str(uuid.uuid4())
        task = Task(task_id, task_type, params)
        task.params["model_type"] = model_type
        
        async with self._lock:
            self.tasks[task_id] = task
        
        # 异步执行任务
        asyncio.create_task(self._execute_task(task))
        
        logger.info(f"Created task: {task_id}, type={task_type}")
        return task_id
    
    async def get_task(self, task_id: str) -> Optional[Task]:
        """获取任务"""
        async with self._lock:
            return self.tasks.get(task_id)
    
    def get_task_status(self, task_id: str) -> Optional[Dict[str, Any]]:
        """获取任务状态（同步方法）"""
        task = self.tasks.get(task_id)
        if not task:
            return None
        
        return {
            "task_id": task.task_id,
            "task_type": task.task_type,
            "status": task.status,
            "progress": task.progress,
            "created_at": task.created_at.isoformat(),
            "started_at": task.started_at.isoformat() if task.started_at else None,
            "completed_at": task.completed_at.isoformat() if task.completed_at else None,
            "error": task.error,
            "result": task.result
        }
    
    async def _execute_task(self, task: Task):
        """执行任务"""
        task.status = TaskStatus.RUNNING
        task.started_at = datetime.now()
        
        try:
            if task.task_type == TaskType.REQUIREMENT_PARSE:
                await self._execute_requirement_parse(task)
            elif task.task_type == TaskType.TESTCASE_GENERATE:
                await self._execute_testcase_generate(task)
            elif task.task_type == TaskType.TESTCASE_REVIEW:
                await self._execute_testcase_review(task)
            elif task.task_type == TaskType.DEFECT_ANALYSIS:
                await self._execute_defect_analysis(task)
            
            task.status = TaskStatus.COMPLETED
            task.progress = 100
            
        except Exception as e:
            logger.error(f"Task {task.task_id} failed: {e}", exc_info=True)
            task.status = TaskStatus.FAILED
            task.error = str(e)
        
        task.completed_at = datetime.now()
        logger.info(f"Task {task.task_id} completed, status={task.status}")
    
    async def _execute_requirement_parse(self, task: Task):
        """执行需求解析任务"""
        model_type = task.params.get("model_type")
        agent = RequirementParseAgent(model_type)
        
        task.progress = 20
        result = await agent.parse(task.params["content"])
        
        task.progress = 100
        task.result = {
            "analysis": result
        }
    
    async def _execute_testcase_generate(self, task: Task):
        """执行用例生成任务"""
        model_type = task.params.get("model_type")
        agent = TestcaseGenerateAgent(model_type)
        
        # 先解析需求
        requirement_analysis = None
        if task.params.get("content"):
            parse_agent = RequirementParseAgent(model_type)
            task.progress = 10
            requirement_analysis = await parse_agent.parse(task.params["content"])
        
        task.progress = 30
        
        # 生成用例
        testcases = await agent.generate(
            requirement_content=task.params.get("content", ""),
            requirement_analysis=requirement_analysis,
            coverage_level=task.params.get("coverage_level", "full")
        )
        
        task.progress = 80
        
        # 质量审核
        if testcases:
            review_agent = TestcaseReviewAgent(model_type)
            review_result = await review_agent.review(testcases)
            
            # 为每个用例添加AI生成标记和风险分数
            for i, tc in enumerate(testcases):
                tc["ai_generated"] = True
                tc["risk_score"] = review_result.get("quality_score", 0.8)
            
            task.result = {
                "testcases": testcases,
                "count": len(testcases),
                "review": review_result,
                "requirement_analysis": requirement_analysis
            }
        else:
            task.result = {
                "testcases": [],
                "count": 0,
                "review": None,
                "requirement_analysis": requirement_analysis
            }
        
        task.progress = 100
    
    async def _execute_testcase_review(self, task: Task):
        """执行用例审核任务"""
        model_type = task.params.get("model_type")
        agent = TestcaseReviewAgent(model_type)
        
        task.progress = 20
        result = await agent.review(task.params["testcases"])
        
        task.progress = 100
        task.result = {
            "review": result
        }
    
    async def _execute_defect_analysis(self, task: Task):
        """执行缺陷分析任务"""
        model_type = task.params.get("model_type")
        agent = DefectAnalysisAgent(model_type)
        
        task.progress = 20
        result = await agent.analyze(task.params["description"])
        
        task.progress = 100
        task.result = {
            "analysis": result
        }


# 全局任务管理器实例
_task_manager: Optional[TaskManager] = None


def get_task_manager() -> TaskManager:
    """获取任务管理器单例"""
    global _task_manager
    if _task_manager is None:
        _task_manager = TaskManager()
    return _task_manager
