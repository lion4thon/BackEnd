"""
규칙 기반 필터링 서비스
추천에 비즈니스 규칙을 적용
"""
from typing import List, Dict
import pandas as pd
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class RuleFilter:
    """추천에 규칙 기반 필터링을 적용합니다."""

    @staticmethod
    def filter_by_intensity(
        pass_candidates: pd.DataFrame,
        user_preferred_intensity: str
    ) -> pd.DataFrame:
        """
        강도 선호도로 패키지를 필터링합니다.

        Args:
            pass_candidates: 후보 패키지 DataFrame
            user_preferred_intensity: 사용자 선호 강도 (LOW/MID/HIGH)

        Returns:
            필터링된 DataFrame
        """
        intensity_map = {
            '가벼운 웜업 위주': 'LOW',
            '살짝 땀이 나는 정도': 'LOW',
            '땀이 흠뻑 젖도록 중강도': 'MID',
            '끝나면 기진맥진 고강도': 'HIGH'
        }

        target_intensity = intensity_map.get(user_preferred_intensity, 'MID')

        # 동일하거나 인접한 강도 레벨 허용
        intensity_tolerance = {
            'LOW': ['LOW', 'MID'],
            'MID': ['LOW', 'MID', 'HIGH'],
            'HIGH': ['MID', 'HIGH']
        }

        allowed_intensities = intensity_tolerance.get(target_intensity, ['MID'])

        filtered = pass_candidates[
            pass_candidates['intensity'].isin(allowed_intensities)
        ]

        logger.info(
            f"강도 필터: {len(pass_candidates)} -> {len(filtered)}개 패키지"
        )

        return filtered

    @staticmethod
    def filter_by_purpose(
        pass_candidates: pd.DataFrame,
        user_purpose: str
    ) -> pd.DataFrame:
        """
        목적으로 패키지를 필터링합니다.

        Args:
            pass_candidates: 후보 패키지 DataFrame
            user_purpose: 사용자 운동 목적

        Returns:
            필터링된 DataFrame
        """
        purpose_map = {
            '다이어트': 'DIET',
            '회복(통증완화, 재활 등)': 'REHAB',
            '체력 증진': 'FITNESS',
            '스트레스 해소': 'STRESS_RELIEF',
            '취미 탐색': 'EXPLORE'
        }

        target_purpose = purpose_map.get(user_purpose, 'FITNESS')

        # 목적 일치로 필터링
        filtered = pass_candidates[
            pass_candidates['purposeTag'] == target_purpose
        ]

        # 결과가 너무 적으면 다른 목적도 포함
        if len(filtered) < 10:
            # 충분한 후보가 없으면 predicted_score로 정렬된 모든 후보 반환
            filtered = pass_candidates.copy()
            logger.info(f"{target_purpose}에 대한 일치 항목이 부족하여 모든 후보 반환")

        logger.info(
            f"목적 필터: {len(pass_candidates)} -> {len(filtered)}개 패키지"
        )

        return filtered

    @staticmethod
    def filter_by_sport_preference(
        pass_candidates: pd.DataFrame,
        preferred_sports: List[str]
    ) -> pd.DataFrame:
        """
        사용자 선호 종목이 포함된 패키지를 우대합니다.

        Args:
            pass_candidates: 후보 패키지 DataFrame
            preferred_sports: 사용자가 관심 있는 종목 이름 리스트

        Returns:
            일치하는 종목에 대한 점수가 높은 DataFrame
        """
        if not preferred_sports:
            return pass_candidates

        # pass_candidates에 종목 이름이 있는 'sports' 컬럼이 있다고 가정
        # PassItem 및 Sport 데이터와 조인 필요
        # 현재는 그대로 반환
        # TODO: 종목 매칭 로직 구현

        return pass_candidates

    def apply_all_filters(
        self,
        pass_candidates: pd.DataFrame,
        user_survey: Dict
    ) -> pd.DataFrame:
        """
        모든 규칙 기반 필터를 적용합니다.

        Args:
            pass_candidates: 후보 패키지 DataFrame
            user_survey: 사용자 설문 응답

        Returns:
            필터링된 DataFrame
        """
        filtered = pass_candidates.copy()

        # 강도 필터 적용
        if 'preferred_intensity' in user_survey:
            filtered = self.filter_by_intensity(
                filtered,
                user_survey['preferred_intensity']
            )

        # 목적 필터 적용
        if 'purpose' in user_survey:
            filtered = self.filter_by_purpose(
                filtered,
                user_survey['purpose']
            )

        # 종목 선호도 우대 적용
        if 'preferred_sports' in user_survey:
            filtered = self.filter_by_sport_preference(
                filtered,
                user_survey['preferred_sports']
            )

        return filtered
