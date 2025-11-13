"""
CatBoost 모델 학습기
추천 모델 학습 담당
"""
import pandas as pd
from catboost import CatBoostRegressor, Pool
from pathlib import Path
from typing import Tuple, List
import logging

from config.settings import settings

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class ModelTrainer:
    """CatBoost 모델 학습을 처리합니다."""

    def __init__(self):
        self.model = None
        self.categorical_features = None

    def prepare_features(
        self,
        df: pd.DataFrame
    ) -> Tuple[pd.DataFrame, pd.Series, List[str]]:
        """
        데이터프레임에서 특성과 타겟을 준비합니다.

        Args:
            df: 학습 데이터프레임

        Returns:
            (X, y, categorical_feature_names) 튜플
        """
        # 타겟 변수 - purchased_pass_id
        target_col = 'purchased_pass_id'

        if target_col not in df.columns:
            raise ValueError(f"타겟 컬럼 '{target_col}'을 데이터에서 찾을 수 없습니다")

        y = df[target_col]
        X = df.drop(columns=[target_col], errors='ignore')

        # 실제 CSV 컬럼 기반으로 범주형 특성 정의
        categorical_features = [
            'purpose',              # 운동 목적
            'preferredIntensity',   # 선호 강도
            'interestedSportIds',   # 관심 종목 IDs (문자열로 처리)
            'preferredEnvironment', # 실내/실외
            'avoidFactors',         # 피하고 싶은 강도
            'recoveryCondition',    # 회복 상태
        ]

        # X에 존재하는 범주형 특성만 필터링
        categorical_features = [f for f in categorical_features if f in X.columns]

        # 모든 범주형 특성을 문자열로 변환하고 NaN 처리
        for col in categorical_features:
            X[col] = X[col].fillna('missing').astype(str)

        logger.info(f"범주형 특성: {categorical_features}")
        logger.info(f"특성 컬럼: {X.columns.tolist()}")

        return X, y, categorical_features

    def train(
        self,
        train_data_path: Path = settings.TRAINING_DATA_PATH,
        save_model: bool = True
    ) -> CatBoostRegressor:
        """
        CatBoost 모델을 학습합니다.

        Args:
            train_data_path: 학습 CSV 경로
            save_model: 학습된 모델 저장 여부

        Returns:
            학습된 CatBoost 모델
        """
        logger.info(f"{train_data_path}에서 학습 데이터 로드 중...")
        df = pd.read_csv(train_data_path)

        logger.info(f"특성 준비 중... 데이터셋 크기: {df.shape}")
        X, y, cat_features = self.prepare_features(df)

        self.categorical_features = cat_features

        # CatBoost Pool 생성
        train_pool = Pool(
            data=X,
            label=y,
            cat_features=cat_features
        )

        # 모델 초기화
        self.model = CatBoostRegressor(
            iterations=settings.CATBOOST_ITERATIONS,
            learning_rate=settings.CATBOOST_LEARNING_RATE,
            depth=settings.CATBOOST_DEPTH,
            loss_function=settings.CATBOOST_LOSS_FUNCTION,
            verbose=50,
            random_seed=42
        )

        logger.info("CatBoost 모델 학습 중...")
        self.model.fit(train_pool)

        logger.info("학습 완료!")

        if save_model:
            self.save_model()

        return self.model

    def save_model(self, model_path: Path = settings.MODEL_PATH) -> None:
        """학습된 모델을 디스크에 저장합니다."""
        if self.model is None:
            raise ValueError("저장할 모델이 없습니다. 먼저 모델을 학습하세요.")

        model_path.parent.mkdir(parents=True, exist_ok=True)
        self.model.save_model(str(model_path))
        logger.info(f"모델이 {model_path}에 저장되었습니다")


if __name__ == "__main__":
    trainer = ModelTrainer()
    trainer.train()
