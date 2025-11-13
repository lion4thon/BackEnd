"""
추천을 위한 모델 예측기
학습된 모델 로드 및 예측 담당
"""
import pandas as pd
from catboost import CatBoostRegressor
from pathlib import Path
from typing import List, Dict
import logging

from config.settings import settings

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class ModelPredictor:
    """모델 로드 및 예측을 처리합니다."""

    def __init__(self, model_path: Path = settings.MODEL_PATH):
        self.model = None
        self.model_path = model_path
        self._load_model()

    def _load_model(self) -> None:
        """디스크에서 학습된 모델을 로드합니다."""
        if not self.model_path.exists():
            raise FileNotFoundError(
                f"{self.model_path}에서 모델 파일을 찾을 수 없습니다. "
                "먼저 모델을 학습하세요."
            )

        logger.info(f"{self.model_path}에서 모델 로드 중")
        self.model = CatBoostRegressor()
        self.model.load_model(str(self.model_path))
        logger.info("모델 로드 완료")

    def predict(self, features: pd.DataFrame) -> pd.Series:
        """
        주어진 특성에 대해 예측을 수행합니다.

        Args:
            features: 학습 데이터와 동일한 구조의 DataFrame

        Returns:
            예측 점수의 Series
        """
        if self.model is None:
            raise ValueError("모델이 로드되지 않았습니다")

        predictions = self.model.predict(features)
        return pd.Series(predictions, index=features.index)

    def predict_for_user(
        self,
        user_features: Dict,
        pass_metadata: pd.DataFrame
    ) -> pd.DataFrame:
        """
        주어진 사용자에 대해 모든 패키지의 점수를 예측합니다.

        Args:
            user_features: 사용자 설문 응답 (purpose, preferredIntensity, interestedSportIds, preferredEnvironment, avoidFactors, recoveryCondition)
            pass_metadata: 패키지 정보 DataFrame (price 컬럼 필수)

        Returns:
            pass_id와 predicted_score를 포함한 DataFrame
        """
        # 사용자 특성과 각 패스를 결합하여 특성 매트릭스 생성
        num_passes = len(pass_metadata)

        # 각 패스에 대해 사용자 특성 복제
        user_df = pd.DataFrame([user_features] * num_passes)

        # pass_metadata에서 price 추출하여 특성에 추가
        # 학습 데이터 컬럼: purpose, preferredIntensity, interestedSportIds, price, preferredEnvironment, avoidFactors, recoveryCondition
        features = pd.DataFrame({
            'purpose': user_df['purpose'],
            'preferredIntensity': user_df['preferredIntensity'],
            'interestedSportIds': user_df['interestedSportIds'],
            'price': pass_metadata['price'].values,
            'preferredEnvironment': user_df['preferredEnvironment'],
            'avoidFactors': user_df['avoidFactors'],
            'recoveryCondition': user_df['recoveryCondition']
        })

        # 예측 수행
        scores = self.predict(features)

        # 결과 데이터프레임 생성
        result = pd.DataFrame({
            'pass_id': pass_metadata['pass_id'].values,  # server.py에서 pass_id로 변경됨
            'predicted_score': scores
        })

        return result.sort_values('predicted_score', ascending=False)

    def get_top_n(
        self,
        user_features: Dict,
        pass_metadata: pd.DataFrame,
        n: int = settings.TOP_N_RECOMMENDATIONS
    ) -> List[int]:
        """
        사용자를 위한 상위 N개 패키지 ID를 가져옵니다.

        Args:
            user_features: 사용자 설문 응답
            pass_metadata: 패키지 정보 DataFrame
            n: 추천 개수

        Returns:
            상위 N개 패키지 ID 리스트
        """
        predictions = self.predict_for_user(user_features, pass_metadata)
        top_n = predictions.head(n)
        return top_n['pass_id'].tolist()
