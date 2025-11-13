"""
하이브리드 추천 서비스
ML 기반 예측과 규칙 기반 필터링 결합
"""
from typing import List, Dict
import pandas as pd
import logging

from models.predictor import ModelPredictor
from services.rule_filter import RuleFilter
from config.settings import settings

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class RecommendationService:
    """ML과 규칙을 결합한 메인 추천 서비스"""

    def __init__(self):
        self.predictor = ModelPredictor()
        self.rule_filter = RuleFilter()

    def get_recommendations(
        self,
        user_survey: Dict,
        pass_metadata: pd.DataFrame,
        top_n: int = settings.TOP_N_RECOMMENDATIONS
    ) -> List[Dict]:
        """
        사용자를 위한 하이브리드 추천을 제공합니다.

        작업 흐름:
        1. ML 모델을 사용하여 모든 패키지에 점수 부여
        2. 규칙 기반 필터 적용
        3. 재순위화 후 상위 N개 반환

        Args:
            user_survey: 사용자 설문 응답
            pass_metadata: 패키지 정보가 포함된 DataFrame
            top_n: 반환할 추천 개수

        Returns:
            점수가 포함된 추천 패키지 딕셔너리 리스트
        """
        logger.info("추천 프로세스 시작")

        # 단계 1: ML 기반 점수 산출
        logger.info("단계 1: ML 점수 산출")
        ml_predictions = self.predictor.predict_for_user(
            user_survey,
            pass_metadata
        )

        # 예측과 패키지 메타데이터 병합
        candidates = pass_metadata.merge(
            ml_predictions,
            on='pass_id',
            how='inner'
        )

        logger.info(f"ML 모델이 {len(candidates)}개 패키지에 점수 부여")

        # 단계 2: 규칙 기반 필터링
        logger.info("단계 2: 규칙 기반 필터 적용")
        filtered = self.rule_filter.apply_all_filters(
            candidates,
            user_survey
        )

        logger.info(f"필터링 후: {len(filtered)}개 패키지 남음")

        # 단계 3: 최종 순위 결정
        # 최소 점수 임계값으로 필터링
        filtered = filtered[
            filtered['predicted_score'] >= settings.MIN_SCORE_THRESHOLD
        ]

        # 예측 점수로 정렬
        final_recommendations = filtered.sort_values(
            'predicted_score',
            ascending=False
        ).head(top_n)

        # 딕셔너리 리스트로 변환
        recommendations = final_recommendations[[
            'pass_id',
            'name',
            'price',
            'intensity',
            'purposeTag',
            'predicted_score'
        ]].to_dict('records')

        logger.info(f"{len(recommendations)}개 추천 반환")

        return recommendations

    def explain_recommendation(
        self,
        pass_id: int,
        user_survey: Dict
    ) -> Dict:
        """
        패키지가 추천된 이유에 대한 설명을 제공합니다.

        Args:
            pass_id: 추천된 패키지 ID
            user_survey: 사용자 설문 응답

        Returns:
            설명 세부 정보가 포함된 딕셔너리
        """
        # TODO: SHAP 또는 특성 중요도 기반 설명 구현
        return {
            'pass_id': pass_id,
            'explanation': '사용자 선호도와 ML 모델 기반',
            'match_reasons': []
        }
