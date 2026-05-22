"""
LLM 客户端封装
"""
from openai import OpenAI
from config import OPENAI_API_KEY, MODEL_NAME

client = None
if OPENAI_API_KEY:
    client = OpenAI(api_key=OPENAI_API_KEY)


async def call_llm(prompt: str) -> str:
    """调用 LLM"""
    if not client:
        return "LLM 客户端未配置，请设置 OPENAI_API_KEY"
    
    try:
        response = client.chat.completions.create(
            model=MODEL_NAME,
            messages=[{"role": "user", "content": prompt}],
            temperature=0.7,
        )
        return response.choices[0].message.content
    except Exception as e:
        return f"LLM 调用失败: {str(e)}"
