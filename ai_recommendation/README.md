# MOV AI 추천 시스템

CatBoost와 규칙 기반 필터링을 사용한 AI 기반 운동 패키지 추천 시스템

## 시스템 아키텍처

```
프론트엔드 (React)
    ↓
Spring Boot (8080 포트)
    ↓ (프록시)
FastAPI (8000 포트)
    ↓ (ML 추천 엔진)
MySQL RDS (패키지 데이터)
```

## 프로젝트 구조

```
ai_recommendation/
├── config/          # 설정 파일 (settings.py)
├── data/            # 학습 데이터 (training_data.csv)
├── models/          # ML 모델 (trainer, predictor, catboost_model.cbm)
├── services/        # 비즈니스 로직 (추천, 필터링)
├── api/             # FastAPI 서버 및 스키마
├── Dockerfile       # FastAPI Docker 이미지
├── requirements.txt # Python 의존성
└── .dockerignore    # Docker 빌드 제외 파일
```

## 로컬 개발 환경 설정

### 1. 가상환경 생성 및 활성화

```bash
cd ai_recommendation
python3 -m venv venv
source venv/bin/activate  # Mac/Linux
# venv\Scripts\activate  # Windows
```

### 2. 의존성 설치

```bash
pip install -r requirements.txt
```

### 3. 모델 학습 (선택)

이미 학습된 모델(`models/catboost_model.cbm`)이 있으면 생략 가능

```bash
python -m models.trainer
```

### 4. FastAPI 서버 시작

```bash
uvicorn api.server:app --host 0.0.0.0 --port 8000 --reload
```

### 5. Spring Boot 서버 연동

FastAPI는 Spring Boot의 패키지 메타데이터를 가져오므로, Spring Boot 서버가 먼저 실행되어야 합니다:

```bash
# 별도 터미널에서 Spring Boot 실행
cd ..  # 프로젝트 루트로 이동
./gradlew bootRun
```

## Docker 배포

### 로컬에서 Docker 빌드 테스트

```bash
# FastAPI 이미지 빌드
cd ai_recommendation
docker build -t mov-fastapi .

# 컨테이너 실행
docker run -d -p 8000:8000 mov-fastapi
```

### 자동 배포 (CI/CD)

`main` 브랜치에 push하면 GitHub Actions가 자동으로:
1. Spring Boot와 FastAPI Docker 이미지 빌드
2. Docker Hub에 푸시
3. EC2 서버에 배포

## API 엔드포인트

### 1. 프론트엔드 → Spring Boot (추천)

**엔드포인트**: `POST /api/ai/recommendations`
**설명**: 프론트엔드가 호출하는 메인 추천 API (Spring Boot가 FastAPI로 프록시)

**요청 예시**:
```json
{
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
```

**응답 예시**:
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

### 2. FastAPI 직접 호출 (테스트용)

**엔드포인트**: `POST http://localhost:8000/api/recommendations`
**설명**: FastAPI 서버에 직접 추천 요청 (개발/테스트용)

### 3. 헬스 체크

```bash
GET http://localhost:8000/health
```

**응답**:
```json
{
  "status": "healthy",
  "model_loaded": true,
  "version": "1.0.0"
}
```

### 4. 모델 재학습

```bash
POST http://localhost:8000/api/train
```

**주의**: 프로덕션에서는 인증 추가 권장

## Spring Boot 통합

FastAPI는 다음 Spring Boot 엔드포인트를 호출합니다:

```
GET http://localhost:8080/api/passes/metadata
```

이 엔드포인트는 모든 패키지 메타데이터(pass_id, name, price, intensity, purposeTag)를 JSON 배열로 반환합니다.

## 환경 설정

`config/settings.py` 또는 `.env` 파일에서 설정 가능:

```env
SPRING_BOOT_URL=http://localhost:8080  # Spring Boot 서버 주소
API_HOST=0.0.0.0
API_PORT=8000
TOP_N_RECOMMENDATIONS=10               # 반환할 추천 개수
MIN_SCORE_THRESHOLD=0.3                # 최소 점수 임계값
```

## 주요 특징

### 1. 하이브리드 추천 시스템
- **ML 기반 점수**: CatBoost를 사용한 협업 필터링
- **규칙 기반 필터**: 사용자 선호도(강도, 목적, 환경 등) 반영

### 2. 9개 설문 항목 지원
- 운동 목적, 선호 시간, 선호 강도, 이동 시간
- 운동 환경, 관심 운동 종목, 회복 정도
- 예산 범위, 피하고 싶은 요소

### 3. 실시간 통합
- Spring Boot와 FastAPI 간 실시간 데이터 교환
- 패키지 메타데이터 자동 동기화

### 4. 자동 배포
- Docker 컨테이너화
- GitHub Actions CI/CD 파이프라인

## 추천 알고리즘 흐름

```
1. 프론트엔드에서 9개 설문 응답 제출
   ↓
2. Spring Boot가 FastAPI로 프록시
   ↓
3. FastAPI가 Spring Boot에서 패키지 메타데이터 조회
   ↓
4. CatBoost 모델로 각 패키지에 점수 부여
   ↓
5. 규칙 기반 필터 적용 (목적, 강도, 환경 등)
   ↓
6. 상위 10개 패키지 반환
   ↓
7. Spring Boot → 프론트엔드로 응답
```

## 학습 데이터 구조

**파일**: `data/training_data.csv`

**컬럼**:
- `purpose`: 운동 목적 (다이어트, 근육 증가, 체력 향상, 취미 탐색)
- `preferredIntensity`: 선호 강도
- `interestedSportIds`: 관심 운동 종목 (쉼표로 구분된 ID)
- `price`: 패키지 가격
- `preferredEnvironment`: 운동 환경 (실내/실외/상관없음)
- `avoidFactors`: 피하고 싶은 요소
- `recoveryCondition`: 회복 정도
- `purchased_pass_id`: 구매한 패키지 ID (타겟 변수)

## 트러블슈팅

### FastAPI 서버가 Spring Boot에 연결 못함
- Spring Boot 서버가 실행 중인지 확인
- `http://localhost:8080/api/passes/metadata` 엔드포인트 확인

### 모델 파일이 없다는 오류
```bash
python -m models.trainer  # 모델 재학습
```

### Docker 이미지 빌드 실패
- `requirements.txt` 파일 확인
- `.dockerignore`에 `venv/` 포함되었는지 확인

## 라이선스

이 프로젝트는 해커톤용으로 제작되었습니다.
