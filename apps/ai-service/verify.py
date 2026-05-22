#!/usr/bin/env python3
"""
AI服务验证脚本
用于验证AI服务的依赖是否正确安装
"""

try:
    # 测试FastAPI相关
    from fastapi import FastAPI
    from pydantic import BaseModel
    print("✓ FastAPI 和 Pydantic 导入成功")
    
    # 测试LangChain相关
    import langchain
    from langchain_core.prompts import PromptTemplate
    print("✓ LangChain 导入成功")
    
    # 测试uvicorn
    import uvicorn
    print("✓ Uvicorn 导入成功")
    
    print("\n🎉 所有依赖验证通过！AI服务可以正常运行。")
    print("\n要启动AI服务，请运行:")
    print("  python main.py")
    
except ImportError as e:
    print(f"✗ 导入失败: {e}")
    print("\n请安装依赖:")
    print("  pip install -r requirements.txt")
