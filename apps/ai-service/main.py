"""
AI服务主程序：算法能力 + 平台 BFF（dashboard / strategy / AI 网关）
"""
import logging
from datetime import datetime

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from platform_bff import router as platform_router

import uvicorn

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
)
logger = logging.getLogger(__name__)

app = FastAPI(title="AI测试管理平台 - AI服务", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(platform_router)


@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "service": "ai-service",
        "timestamp": datetime.now().isoformat(),
    }


if __name__ == "__main__":
    logger.info("启动AI服务...")
    uvicorn.run(app, host="0.0.0.0", port=8010)
