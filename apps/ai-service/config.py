"""
配置管理模块
"""
import os
from pydantic_settings import BaseSettings
from typing import Optional
from functools import lru_cache


class Settings(BaseSettings):
    """应用配置类"""
    
    # 服务配置
    service_port: int = 8010
    service_host: str = "0.0.0.0"
    
    # 大模型配置 - DeepSeek
    deepseek_api_key: Optional[str] = None
    deepseek_base_url: str = "https://api.deepseek.com/v1"
    deepseek_model: str = "deepseek-chat"
    
    # 大模型配置 - Qwen
    qwen_api_key: Optional[str] = None
    qwen_base_url: str = "https://dashscope.aliyuncs.com/compatible-mode/v1"
    qwen_model: str = "qwen-turbo"
    
    # 大模型配置 - OpenAI
    openai_api_key: Optional[str] = None
    openai_base_url: str = "https://api.openai.com/v1"
    openai_model: str = "gpt-4"
    
    # 大模型配置 - Claude
    claude_api_key: Optional[str] = None
    claude_base_url: str = "https://api.anthropic.com/v1"
    claude_model: str = "claude-3-opus-20240229"
    
    # 默认模型
    default_model: str = "deepseek"
    
    # Redis配置
    redis_host: str = "localhost"
    redis_port: int = 6379
    redis_db: int = 0
    redis_password: Optional[str] = None
    
    # 日志配置
    log_level: str = "INFO"
    
    class Config:
        env_file = ".env"
        case_sensitive = False


@lru_cache()
def get_settings() -> Settings:
    """获取配置单例"""
    return Settings()
