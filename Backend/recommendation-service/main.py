from fastapi import FastAPI
from routes import recommendations

from services.monitor_service import start_monitoring
from settings.rabbitmq_client import create_queue

app = FastAPI()

app.include_router(recommendations.router, prefix="/api", tags=["Recommendations"])


@app.on_event("startup")
async def startup_event():
    await create_queue()

if __name__ == "__main__":
    start_monitoring()
