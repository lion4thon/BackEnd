"""
AI 추천 시스템 설정 파일
"""
from pathlib import Path
from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    """애플리케이션 설정"""

    # 프로젝트 경로
    PROJECT_ROOT: Path = Path(__file__).parent.parent
    DATA_DIR: Path = PROJECT_ROOT / "data"
    MODELS_DIR: Path = PROJECT_ROOT / "models"

    # 데이터 파일
    TRAINING_DATA_PATH: Path = DATA_DIR / "training_data.csv"
    MODEL_PATH: Path = MODELS_DIR / "catboost_model.cbm"

    # 모델 파라미터
    CATBOOST_ITERATIONS: int = 100
    CATBOOST_LEARNING_RATE: float = 0.1
    CATBOOST_DEPTH: int = 6
    CATBOOST_LOSS_FUNCTION: str = "RMSE"

    # API 설정
    API_HOST: str = "0.0.0.0"
    API_PORT: int = 8000
    API_TITLE: str = "MOV AI Recommendation API"
    API_VERSION: str = "1.0.0"

    # Spring Boot 백엔드
    SPRING_BOOT_URL: str = "http://localhost:8080"

    # 추천 설정
    TOP_N_RECOMMENDATIONS: int = 10
    MIN_SCORE_THRESHOLD: float = 0.3

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


# 전역 설정 인스턴스
settings = Settings()
