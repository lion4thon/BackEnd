# MOV AI 추천 시스템

CatBoost와 규칙 기반 필터링을 사용한 AI 기반 운동 패키지 추천 시스템

## 아키텍처

```
ai_recommendation/
├── config/          # 설정 파일
├── data/            # 학습 데이터 (CSV)
├── models/          # ML 모델 (trainer, predictor)
├── services/        # 비즈니스 로직 (추천, 필터링)
├── api/             # FastAPI 서버
└── utils/           # 유틸리티
```

## 설치 및 실행

### 1. 의존성 설치

```bash
cd ai_recommendation
pip install -r requirements.txt
```

### 2. 학습 데이터 준비

`data/training_data.csv`에 학습 데이터 배치

예상 컬럼:
- 사용자 특성: purpose, preferredIntensity, interestedSportIds, preferredEnvironment, avoidFactors, recoveryCondition
- 패키지 특성: price
- 타겟: purchased_pass_id

### 3. 모델 학습

```bash
python -m models.trainer
```

실행 결과:
- `data/training_data.csv`에서 데이터 로드
- CatBoost 모델 학습
- 모델을 `models/catboost_model.cbm`에 저장

### 4. API 서버 시작

```bash
python -m api.server
```

또는 uvicorn으로 직접 실행:

```bash
uvicorn api.server:app --host 0.0.0.0 --port 8000 --reload
```

## API 사용법

### 추천 받기

```bash
POST /api/recommendations
Content-Type: application/json

{
  "purpose": "다이어트",
  "preferred_time": "저녁(18시~23시)",
  "preferred_intensity": "땀이 흠뻑 젖도록 중강도",
  "travel_time": "30분 이내",
  "environment": "실내",
  "preferred_sports": ["요가", "필라테스"],
  "recovery_level": "보통/적당히 회복됨"
}
```

응답:
```json
{
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
  "total_count": 10
}
```

### 헬스 체크

```bash
GET /health
```

### 모델 재학습 트리거

```bash
POST /api/train
```

## Spring Boot 연동

FastAPI 서버는 Spring Boot 엔드포인트를 호출합니다:

```
GET http://localhost:8080/api/passes/metadata
```

이 엔드포인트는 모든 패키지 메타데이터를 JSON 배열로 반환해야 합니다.

## 설정

`config/settings.py` 편집 또는 `.env` 파일 생성:

```env
SPRING_BOOT_URL=http://localhost:8080
API_PORT=8000
TOP_N_RECOMMENDATIONS=10
MIN_SCORE_THRESHOLD=0.3
```

## 개발

테스트 실행:
```bash
pytest
```

코드 포맷팅:
```bash
black .
```

## 주요 특징

- CatBoost를 사용한 범주형 특성 효율적 처리
- 하이브리드 접근: ML 점수 산출 + 규칙 기반 필터링
- 30개 패키지와 9개 운동 종목을 위한 설계
- Spring Boot 백엔드와 완전 통합

## 추천 흐름

1. **사용자 설문** → FastAPI 서버로 전송
2. **Spring Boot에서 패키지 메타데이터 가져오기**
3. **ML 모델로 점수 예측** (CatBoost)
4. **규칙 기반 필터링** 적용 (강도, 목적 등)
5. **Top 10 추천 반환**
