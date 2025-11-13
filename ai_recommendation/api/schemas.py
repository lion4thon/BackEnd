"""
API 요청/응답 검증을 위한 Pydantic 스키마
"""
from pydantic import BaseModel, Field
from typing import List, Optional


class UserSurveyRequest(BaseModel):
    """사용자 설문 요청 스키마"""

    purpose: str = Field(..., description="운동 목적")
    preferred_time: str = Field(..., description="선호 운동 시간")
    preferred_intensity: str = Field(..., description="선호 운동 강도")
    travel_time: str = Field(..., description="이동 가능 시간")
    environment: str = Field(..., description="운동 환경 (실내/실외)")
    preferred_sports: List[str] = Field(default=[], description="관심 운동 종목")
    recovery_level: str = Field(..., description="회복 정도")
    budget_range: str = Field(..., description="1회 기준 패키지 예산")
    avoid_factors: List[str] = Field(default=[], description="피하고 싶은 요소")

    class Config:
        json_schema_extra = {
            "example": {
                "purpose": "다이어트",
                "preferred_time": "저녁(18시~23시)",
                "preferred_intensity": "땀이 흠뻑 젖도록 중강도",
                "travel_time": "30분 이내",
                "environment": "실내",
                "preferred_sports": ["웨이트 & 크로스핏", "실내 수영"],
                "recovery_level": "평범함",
                "budget_range": "이만원대",
                "avoid_factors": ["가벼운 웜업 위주"]
            }
        }


class PassRecommendation(BaseModel):
    """단일 패키지 추천"""

    pass_id: int
    name: str
    price: int
    intensity: str
    purpose_tag: str = Field(..., alias="purposeTag")
    predicted_score: float

    class Config:
        populate_by_name = True


class RecommendationResponse(BaseModel):
    """추천 API 응답"""

    recommendations: List[PassRecommendation]
    total_count: int

    class Config:
        json_schema_extra = {
            "example": {
                "recommendations": [
                    {
                        "pass_id": 2,
                        "name": "체중감량 시작 (웨이트1회+수영1회)",
                        "price": 29000,
                        "intensity": "HIGH",
                        "purposeTag": "DIET",
                        "predicted_score": 0.85
                    }
                ],
                "total_count": 1
            }
        }


class HealthCheckResponse(BaseModel):
    """헬스 체크 응답"""

    status: str
    model_loaded: bool
    version: str
