"""
추천 API를 위한 FastAPI 서버
"""
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
import httpx
from typing import List
import logging

from api.schemas import (
    UserSurveyRequest,
    RecommendationResponse,
    PassRecommendation,
    HealthCheckResponse
)
from services.recommendation_service import RecommendationService
from config.settings import settings

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# FastAPI 앱 초기화
app = FastAPI(
    title=settings.API_TITLE,
    version=settings.API_VERSION,
    description="AI 기반 운동 패키지 추천 시스템"
)

# CORS 미들웨어
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 프로덕션에서는 적절히 설정 필요
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 추천 서비스 초기화
rec_service = None


@app.on_event("startup")
async def startup_event():
    """서비스 시작 시 초기화"""
    global rec_service
    try:
        logger.info("추천 서비스 초기화 중...")
        rec_service = RecommendationService()
        logger.info("추천 서비스 준비 완료")
    except Exception as e:
        logger.error(f"추천 서비스 초기화 실패: {e}")
        raise


@app.get("/health", response_model=HealthCheckResponse)
async def health_check():
    """헬스 체크 엔드포인트"""
    return HealthCheckResponse(
        status="healthy",
        model_loaded=rec_service is not None,
        version=settings.API_VERSION
    )


@app.post("/api/recommendations", response_model=RecommendationResponse)
async def get_recommendations(survey: UserSurveyRequest):
    """
    사용자 설문을 기반으로 맞춤형 패키지 추천을 제공합니다.

    Args:
        survey: 사용자 설문 응답

    Returns:
        추천 패키지 리스트
    """
    if rec_service is None:
        raise HTTPException(
            status_code=503,
            detail="Recommendation service not initialized"
        )

    try:
        # Fetch pass metadata from Spring Boot backend
        async with httpx.AsyncClient() as client:
            response = await client.get(
                f"{settings.SPRING_BOOT_URL}/api/passes/metadata"
            )
            response.raise_for_status()
            pass_data = response.json()

        # DataFrame으로 변환
        pass_metadata = pd.DataFrame(pass_data)

        # 일관성을 위해 passId를 pass_id로 변경
        if 'passId' in pass_metadata.columns:
            pass_metadata = pass_metadata.rename(columns={'passId': 'pass_id'})

        # API 필드명을 학습 데이터 특성명으로 매핑 (학습된 특성만 포함)
        user_survey_dict = {
            'purpose': survey.purpose,
            'preferredIntensity': survey.preferred_intensity,
            'interestedSportIds': ','.join(survey.preferred_sports) if survey.preferred_sports else '',
            'preferredEnvironment': survey.environment,
            'avoidFactors': ','.join(survey.avoid_factors) if survey.avoid_factors else '',
            'recoveryCondition': survey.recovery_level if survey.recovery_level else 'missing',
            # 아래 필드들은 규칙 기반 필터링용으로 보관 (ML 학습에는 미사용)
            'budgetRange': survey.budget_range,
            'preferredTime': survey.preferred_time,
            'travelTime': survey.travel_time
        }

        recommendations = rec_service.get_recommendations(
            user_survey=user_survey_dict,
            pass_metadata=pass_metadata,
            top_n=settings.TOP_N_RECOMMENDATIONS
        )

        # 응답 모델로 변환
        pass_recommendations = [
            PassRecommendation(**rec) for rec in recommendations
        ]

        return RecommendationResponse(
            recommendations=pass_recommendations,
            total_count=len(pass_recommendations)
        )

    except httpx.HTTPError as e:
        logger.error(f"Spring Boot에서 데이터 가져오기 실패: {e}")
        raise HTTPException(
            status_code=502,
            detail="백엔드에서 패키지 메타데이터 가져오기 실패"
        )
    except Exception as e:
        logger.error(f"추천 생성 중 오류: {e}")
        raise HTTPException(
            status_code=500,
            detail=f"내부 서버 오류: {str(e)}"
        )


@app.post("/api/train")
async def trigger_training():
    """
    모델 재학습을 트리거합니다.
    (프로덕션에서는 인증으로 보호해야 함)
    """
    try:
        from models.trainer import ModelTrainer

        trainer = ModelTrainer()
        trainer.train()

        # Reload the model in the prediction service
        global rec_service
        rec_service = RecommendationService()

        return {"status": "success", "message": "Model retrained successfully"}
    except Exception as e:
        logger.error(f"Training failed: {e}")
        raise HTTPException(
            status_code=500,
            detail=f"Training failed: {str(e)}"
        )


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "server:app",
        host=settings.API_HOST,
        port=settings.API_PORT,
        reload=True
    )
