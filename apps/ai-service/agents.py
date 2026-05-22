"""
Agent模块：需求解析Agent、用例生成Agent、质量审核Agent
"""
import logging
from typing import Dict, Any, List, Optional
from llm_client import LLMClientFactory

logger = logging.getLogger(__name__)


class BaseAgent:
    """Agent基类"""
    
    def __init__(self, model_type: Optional[str] = None):
        self.model_type = model_type
    
    async def _call_llm(self, system_prompt: str, user_prompt: str, **kwargs) -> str:
        """调用LLM"""
        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]
        return await LLMClientFactory.call_llm(
            messages=messages,
            model_type=self.model_type,
            **kwargs
        )
    
    async def _call_llm_json(self, system_prompt: str, user_prompt: str, **kwargs) -> Dict[str, Any]:
        """调用LLM并返回JSON"""
        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]
        return await LLMClientFactory.call_llm_json(
            messages=messages,
            model_type=self.model_type,
            **kwargs
        )


class RequirementParseAgent(BaseAgent):
    """需求解析Agent"""
    
    SYSTEM_PROMPT = """你是一位专业的软件测试架构师，擅长从需求文档中提取测试相关信息。

请根据输入的需求内容，分析并提取以下信息，以JSON格式输出：

{
  "functional_points": ["功能点1", "功能点2"],
  "business_rules": ["业务规则1", "业务规则2"],
  "boundary_conditions": ["边界条件1", "边界条件2"],
  "risk_points": ["风险点1", "风险点2"],
  "test_points": ["测试点1", "测试点2"]
}

要求：
1. functional_points：提取所有核心功能点
2. business_rules：提取业务规则和约束条件
3. boundary_conditions：提取边界值、异常情况、极限场景
4. risk_points：识别潜在风险点和需要重点测试的内容
5. test_points：列出需要测试的测试点

只输出JSON，不要其他文字说明。"""
    
    async def parse(self, requirement_content: str) -> Dict[str, Any]:
        """
        解析需求文档
        
        Args:
            requirement_content: 需求内容
            
        Returns:
            解析结果
        """
        logger.info("RequirementParseAgent parsing requirement")
        
        user_prompt = f"""请分析以下需求内容：

{requirement_content}"""
        
        try:
            result = await self._call_llm_json(
                system_prompt=self.SYSTEM_PROMPT,
                user_prompt=user_prompt,
                temperature=0.3
            )
            logger.info("RequirementParseAgent parse completed successfully")
            return result
        except Exception as e:
            logger.error(f"RequirementParseAgent parse failed: {e}")
            # 返回默认结构
            return {
                "functional_points": [],
                "business_rules": [],
                "boundary_conditions": [],
                "risk_points": [],
                "test_points": []
            }


class TestcaseGenerateAgent(BaseAgent):
    """测试用例生成Agent"""
    
    SYSTEM_PROMPT = """你是一位专业的软件测试工程师，擅长根据需求生成高质量的测试用例。

请根据输入的需求分析和测试点，生成测试用例，以JSON格式输出：

{
  "testcases": [
    {
      "title": "用例标题",
      "priority": "P1",
      "preconditions": ["前置条件1", "前置条件2"],
      "steps": ["步骤1", "步骤2"],
      "expected_result": "预期结果",
      "tags": ["标签1", "标签2"],
      "case_type": "functional",
      "description": "用例描述"
    }
  ]
}

优先级说明：
- P1: 阻塞/严重问题，必须测试
- P2: 重要问题，应该测试
- P3: 一般问题，可以测试

用例类型：
- functional: 功能测试
- boundary: 边界测试
- exception: 异常测试
- performance: 性能测试
- security: 安全测试
- compatibility: 兼容性测试

要求：
1. 覆盖功能测试、边界测试、异常测试等场景
2. 用例步骤清晰可执行
3. 预期结果明确可验证
4. 标签合理分类

只输出JSON，不要其他文字说明。"""
    
    async def generate(
        self,
        requirement_content: str,
        requirement_analysis: Optional[Dict[str, Any]] = None,
        coverage_level: str = "full"
    ) -> List[Dict[str, Any]]:
        """
        生成测试用例
        
        Args:
            requirement_content: 需求内容
            requirement_analysis: 需求解析结果
            coverage_level: 覆盖级别 (basic/full/comprehensive)
            
        Returns:
            测试用例列表
        """
        logger.info(f"TestcaseGenerateAgent generating testcases, coverage_level={coverage_level}")
        
        analysis_text = ""
        if requirement_analysis:
            analysis_text = f"""
需求分析结果：
- 功能点: {requirement_analysis.get('functional_points', [])}
- 业务规则: {requirement_analysis.get('business_rules', [])}
- 边界条件: {requirement_analysis.get('boundary_conditions', [])}
- 风险点: {requirement_analysis.get('risk_points', [])}
- 测试点: {requirement_analysis.get('test_points', [])}
"""
        
        coverage_hint = {
            "basic": "生成基础测试用例，覆盖主要功能即可，约5-10条用例",
            "full": "生成完整测试用例，覆盖功能、边界、异常场景，约10-20条用例",
            "comprehensive": "生成全面测试用例，覆盖所有场景，约20-50条用例"
        }.get(coverage_level, "生成完整测试用例")
        
        user_prompt = f"""请根据以下信息生成测试用例：

需求内容：
{requirement_content}

{analysis_text}

覆盖级别要求：{coverage_hint}"""
        
        try:
            result = await self._call_llm_json(
                system_prompt=self.SYSTEM_PROMPT,
                user_prompt=user_prompt,
                temperature=0.7
            )
            testcases = result.get("testcases", [])
            logger.info(f"TestcaseGenerateAgent generated {len(testcases)} testcases")
            return testcases
        except Exception as e:
            logger.error(f"TestcaseGenerateAgent generate failed: {e}")
            return []


class TestcaseReviewAgent(BaseAgent):
    """测试用例质量审核Agent"""
    
    SYSTEM_PROMPT = """你是一位资深的测试经理，负责审核测试用例的质量。

请根据输入的测试用例列表，进行质量审核，以JSON格式输出：

{
  "quality_score": 0.85,
  "overall_assessment": "总体评价",
  "review_results": [
    {
      "case_index": 0,
      "case_title": "用例标题",
      "score": 0.9,
      "issues": ["问题1", "问题2"],
      "suggestions": ["建议1", "建议2"],
      "passed": true
    }
  ],
  "improvement_suggestions": ["总体改进建议1", "总体改进建议2"]
}

评分标准（0-1）：
- 0.9-1.0: 优秀，用例完整、准确、可执行
- 0.7-0.9: 良好，基本满足要求，有小改进空间
- 0.5-0.7: 一般，需要较多改进
- 0-0.5: 较差，需要重新设计

审核维度：
1. 完整性：是否覆盖了必要的测试场景
2. 准确性：步骤和预期结果是否准确
3. 可执行性：用例是否可以实际执行
4. 清晰度：描述是否清晰易懂
5. 优先级：优先级设置是否合理

只输出JSON，不要其他文字说明。"""
    
    async def review(self, testcases: List[Dict[str, Any]]) -> Dict[str, Any]:
        """
        审核测试用例质量
        
        Args:
            testcases: 测试用例列表
            
        Returns:
            审核结果
        """
        logger.info(f"TestcaseReviewAgent reviewing {len(testcases)} testcases")
        
        testcases_text = "\n".join([
            f"用例{i+1}: {tc.get('title', '')}\n"
            f"  优先级: {tc.get('priority', '')}\n"
            f"  步骤: {tc.get('steps', [])}\n"
            f"  预期结果: {tc.get('expected_result', '')}"
            for i, tc in enumerate(testcases)
        ])
        
        user_prompt = f"""请审核以下测试用例：

{testcases_text}"""
        
        try:
            result = await self._call_llm_json(
                system_prompt=self.SYSTEM_PROMPT,
                user_prompt=user_prompt,
                temperature=0.3
            )
            logger.info(f"TestcaseReviewAgent review completed, quality_score={result.get('quality_score')}")
            return result
        except Exception as e:
            logger.error(f"TestcaseReviewAgent review failed: {e}")
            return {
                "quality_score": 0.5,
                "overall_assessment": "审核过程出现异常",
                "review_results": [],
                "improvement_suggestions": []
            }


class DefectAnalysisAgent(BaseAgent):
    """缺陷分析Agent"""
    
    SYSTEM_PROMPT = """你是一位资深的软件质量工程师，擅长分析和定位软件缺陷。

请根据输入的缺陷描述，进行缺陷分析，以JSON格式输出：

{
  "root_cause": "根因分析",
  "severity": "high",
  "priority": "high",
  "similar_defects": [
    {"defect_id": 1, "title": "相似缺陷标题", "similarity": 0.85}
  ],
  "fix_suggestions": ["修复建议1", "修复建议2"],
  "risk_level": "medium",
  "estimated_effort": "4小时",
  "affected_modules": ["模块1", "模块2"]
}

严重程度：critical/high/medium/low
优先级：high/medium/low
风险等级：high/medium/low

只输出JSON，不要其他文字说明。"""
    
    async def analyze(self, defect_description: str) -> Dict[str, Any]:
        """
        分析缺陷
        
        Args:
            defect_description: 缺陷描述
            
        Returns:
            分析结果
        """
        logger.info("DefectAnalysisAgent analyzing defect")
        
        user_prompt = f"""请分析以下缺陷：

{defect_description}"""
        
        try:
            result = await self._call_llm_json(
                system_prompt=self.SYSTEM_PROMPT,
                user_prompt=user_prompt,
                temperature=0.5
            )
            logger.info("DefectAnalysisAgent analysis completed")
            return result
        except Exception as e:
            logger.error(f"DefectAnalysisAgent analyze failed: {e}")
            return {
                "root_cause": "分析失败",
                "severity": "medium",
                "priority": "medium",
                "similar_defects": [],
                "fix_suggestions": [],
                "risk_level": "medium",
                "estimated_effort": "未知",
                "affected_modules": []
            }
