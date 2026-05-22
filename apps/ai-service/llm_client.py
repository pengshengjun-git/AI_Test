"""
大模型客户端工厂
"""
import json
import logging
from typing import Dict, Any, Optional, List
from abc import ABC, abstractmethod
from tenacity import retry, stop_after_attempt, wait_exponential, retry_if_exception_type
import httpx
from config import get_settings

logger = logging.getLogger(__name__)


class LLMClient(ABC):
    """LLM客户端抽象基类"""
    
    @abstractmethod
    async def chat_completion(
        self,
        messages: List[Dict[str, str]],
        temperature: float = 0.7,
        max_tokens: Optional[int] = None,
        **kwargs
    ) -> Dict[str, Any]:
        """聊天补全接口"""
        pass


class OpenAICompatibleClient(LLMClient):
    """OpenAI兼容API客户端"""
    
    def __init__(self, api_key: str, base_url: str, model: str):
        self.api_key = api_key
        self.base_url = base_url
        self.model = model
        self.client = httpx.AsyncClient(timeout=120.0)
    
    @retry(
        stop=stop_after_attempt(3),
        wait=wait_exponential(multiplier=1, min=2, max=10),
        retry=retry_if_exception_type((httpx.HTTPError, httpx.TimeoutException))
    )
    async def chat_completion(
        self,
        messages: List[Dict[str, str]],
        temperature: float = 0.7,
        max_tokens: Optional[int] = None,
        **kwargs
    ) -> Dict[str, Any]:
        """
        调用OpenAI兼容的聊天补全接口
        
        Args:
            messages: 消息列表
            temperature: 温度参数
            max_tokens: 最大token数
            **kwargs: 其他参数
            
        Returns:
            响应结果
        """
        url = f"{self.base_url}/chat/completions"
        headers = {
            "Authorization": f"Bearer {self.api_key}",
            "Content-Type": "application/json"
        }
        
        payload = {
            "model": self.model,
            "messages": messages,
            "temperature": temperature,
            **kwargs
        }
        
        if max_tokens:
            payload["max_tokens"] = max_tokens
        
        logger.debug(f"Calling LLM: model={self.model}, messages_count={len(messages)}")
        
        response = await self.client.post(url, headers=headers, json=payload)
        response.raise_for_status()
        
        result = response.json()
        
        logger.debug(f"LLM response received: {result.get('usage', {})}")
        
        return result


class ClaudeClient(LLMClient):
    """Claude API客户端"""
    
    def __init__(self, api_key: str, base_url: str, model: str):
        self.api_key = api_key
        self.base_url = base_url
        self.model = model
        self.client = httpx.AsyncClient(timeout=120.0)
    
    @retry(
        stop=stop_after_attempt(3),
        wait=wait_exponential(multiplier=1, min=2, max=10),
        retry=retry_if_exception_type((httpx.HTTPError, httpx.TimeoutException))
    )
    async def chat_completion(
        self,
        messages: List[Dict[str, str]],
        temperature: float = 0.7,
        max_tokens: Optional[int] = None,
        **kwargs
    ) -> Dict[str, Any]:
        """
        调用Claude聊天补全接口
        
        Args:
            messages: 消息列表
            temperature: 温度参数
            max_tokens: 最大token数
            **kwargs: 其他参数
            
        Returns:
            响应结果
        """
        url = f"{self.base_url}/messages"
        headers = {
            "x-api-key": self.api_key,
            "anthropic-version": "2023-06-01",
            "Content-Type": "application/json"
        }
        
        # 转换消息格式
        system_message = None
        claude_messages = []
        for msg in messages:
            if msg["role"] == "system":
                system_message = msg["content"]
            else:
                claude_messages.append(msg)
        
        payload = {
            "model": self.model,
            "messages": claude_messages,
            "temperature": temperature,
            "max_tokens": max_tokens or 4096,
            **kwargs
        }
        
        if system_message:
            payload["system"] = system_message
        
        logger.debug(f"Calling Claude: model={self.model}, messages_count={len(claude_messages)}")
        
        response = await self.client.post(url, headers=headers, json=payload)
        response.raise_for_status()
        
        result = response.json()
        
        # 转换为OpenAI兼容格式
        openai_format = {
            "choices": [{
                "message": {
                    "role": "assistant",
                    "content": result["content"][0]["text"] if result.get("content") else ""
                }
            }],
            "usage": result.get("usage", {})
        }
        
        return openai_format


class LLMClientFactory:
    """LLM客户端工厂"""
    
    _clients: Dict[str, LLMClient] = {}
    
    @classmethod
    def get_client(cls, model_type: Optional[str] = None) -> LLMClient:
        """
        获取LLM客户端
        
        Args:
            model_type: 模型类型 (deepseek/qwen/openai/claude)
            
        Returns:
            LLM客户端实例
        """
        settings = get_settings()
        model_type = model_type or settings.default_model
        
        if model_type in cls._clients:
            return cls._clients[model_type]
        
        if model_type == "deepseek":
            if not settings.deepseek_api_key:
                raise ValueError("DeepSeek API key not configured")
            client = OpenAICompatibleClient(
                api_key=settings.deepseek_api_key,
                base_url=settings.deepseek_base_url,
                model=settings.deepseek_model
            )
        elif model_type == "qwen":
            if not settings.qwen_api_key:
                raise ValueError("Qwen API key not configured")
            client = OpenAICompatibleClient(
                api_key=settings.qwen_api_key,
                base_url=settings.qwen_base_url,
                model=settings.qwen_model
            )
        elif model_type == "openai":
            if not settings.openai_api_key:
                raise ValueError("OpenAI API key not configured")
            client = OpenAICompatibleClient(
                api_key=settings.openai_api_key,
                base_url=settings.openai_base_url,
                model=settings.openai_model
            )
        elif model_type == "claude":
            if not settings.claude_api_key:
                raise ValueError("Claude API key not configured")
            client = ClaudeClient(
                api_key=settings.claude_api_key,
                base_url=settings.claude_base_url,
                model=settings.claude_model
            )
        else:
            raise ValueError(f"Unsupported model type: {model_type}")
        
        cls._clients[model_type] = client
        return client
    
    @classmethod
    async def call_llm(
        cls,
        messages: List[Dict[str, str]],
        model_type: Optional[str] = None,
        temperature: float = 0.7,
        max_tokens: Optional[int] = None,
        **kwargs
    ) -> str:
        """
        便捷方法：调用LLM并获取回复内容
        
        Args:
            messages: 消息列表
            model_type: 模型类型
            temperature: 温度参数
            max_tokens: 最大token数
            
        Returns:
            回复内容
        """
        client = cls.get_client(model_type)
        response = await client.chat_completion(
            messages=messages,
            temperature=temperature,
            max_tokens=max_tokens,
            **kwargs
        )
        return response["choices"][0]["message"]["content"]
    
    @classmethod
    async def call_llm_json(
        cls,
        messages: List[Dict[str, str]],
        model_type: Optional[str] = None,
        temperature: float = 0.7,
        max_tokens: Optional[int] = None,
        **kwargs
    ) -> Dict[str, Any]:
        """
        便捷方法：调用LLM并解析JSON回复
        
        Args:
            messages: 消息列表
            model_type: 模型类型
            temperature: 温度参数
            max_tokens: 最大token数
            
        Returns:
            解析后的JSON对象
        """
        content = await cls.call_llm(
            messages=messages,
            model_type=model_type,
            temperature=temperature,
            max_tokens=max_tokens,
            **kwargs
        )
        
        # 尝试解析JSON
        try:
            # 清理可能的markdown代码块标记
            content = content.strip()
            if content.startswith("```json"):
                content = content[7:]
            if content.startswith("```"):
                content = content[3:]
            if content.endswith("```"):
                content = content[:-3]
            content = content.strip()
            
            return json.loads(content)
        except json.JSONDecodeError as e:
            logger.error(f"Failed to parse LLM response as JSON: {e}")
            logger.debug(f"LLM response content: {content}")
            raise ValueError("LLM response is not valid JSON")
