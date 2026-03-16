# 리뷰 조회 성능 실험 가이드 (로컬 전용: Docker 분리 DB + 대량 데이터 + k6)

요청하신 목적에 맞게, **로컬에서 Docker로 테스트 DB를 분리**하고,
**대량 리뷰 데이터를 넣은 뒤**, **인덱스 없는 상태에서 조회 성능을 먼저 측정**하는 절차를 정리했습니다.

> ⚠️ 이 문서의 절차는 **배포 환경용이 아닙니다. 로컬 성능 실험 전용**입니다.
> 운영/스테이징 DB에 `seed` 또는 `drop index` 스크립트를 실행하면 안 됩니다.

구성 파일
- Docker DB: `docs/performance/review-loadtest/docker/docker-compose.yml`
- 데이터 적재 SQL: `docs/performance/review-loadtest/sql/seed-review-data.sql`
- 인덱스 제거 SQL(보조 인덱스 제거): `docs/performance/review-loadtest/sql/drop-review-secondary-indexes.sql`
- k6 스크립트: `docs/performance/review-loadtest/k6/review-list-no-index.js`

---

## 0) 사전 준비

- Docker / Docker Compose
- MySQL 클라이언트 (`mysql`)
- k6
- 백엔드 앱 실행 가능 상태 (Spring Boot)

> 기본 포트
> - MySQL: `3307` (컨테이너 `3306` 매핑)
> - 앱: `8080`

---

## 1) Docker로 테스트 DB 올리기

```bash
docker compose -f docs/performance/review-loadtest/docker/docker-compose.yml up -d
```

> 위 compose 파일은 로컬 머신에서만 실행하세요.

상태 확인:

```bash
docker ps | grep review-perf-mysql
```

접속 정보:
- host: `127.0.0.1`
- port: `3307`
- db: `hiddencountry_perf`
- user: `perf`
- pass: `perf`

---

## 2) 앱을 테스트 DB로 연결해서 스키마 생성

중요: 이 단계부터는 **로컬 실행 프로필**(예: `dev`, `local-perf`)에서만 수행하세요.

이 프로젝트는 JPA 기반이라 앱이 한번 뜨면서 테이블을 만들도록 설정하면 편합니다.
(예: `spring.jpa.hibernate.ddl-auto=update` 또는 `create`를 테스트 프로필에서 사용)

예시 실행 환경 변수:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://127.0.0.1:3307/hiddencountry_perf?useSSL=false&serverTimezone=Asia/Seoul \
SPRING_DATASOURCE_USERNAME=perf \
SPRING_DATASOURCE_PASSWORD=perf \
SPRING_JPA_HIBERNATE_DDL_AUTO=update \
./gradlew bootRun
```

앱이 뜬 뒤에는 종료해도 됩니다. (테이블 생성 목적)

---

## 3) 대량 리뷰 데이터 삽입

아래 명령은 테스트 DB를 비우고 다시 채우므로, 반드시 로컬 테스트 DB에만 실행하세요.

```bash
mysql -h 127.0.0.1 -P 3307 -u perf -pperf hiddencountry_perf \
  < docs/performance/review-loadtest/sql/seed-review-data.sql
```

기본 삽입량:
- 사용자: 1,000명
- 장소: 1건
- 리뷰: 200,000건
- 리뷰 이미지: 리뷰의 약 30%

스크립트 마지막에 `target_place_id`를 출력하므로 값을 메모하세요.

---

## 4) 인덱스 없는 상태 맞추기 (측정 전)

요청하신 대로 "인덱스 적용 전" 측정을 위해, `review` 테이블의 **보조 인덱스만 제거**합니다.
(PK는 유지)

이 단계도 로컬 테스트 DB에서만 실행하세요.

```bash
mysql -h 127.0.0.1 -P 3307 -u perf -pperf hiddencountry_perf \
  < docs/performance/review-loadtest/sql/drop-review-secondary-indexes.sql
```

이후 `SHOW INDEX FROM review;` 결과에서 `PRIMARY`만 남았는지 확인합니다.

---

## 5) 백엔드 앱 실행 (테스트 DB 연결)

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://127.0.0.1:3307/hiddencountry_perf?useSSL=false&serverTimezone=Asia/Seoul \
SPRING_DATASOURCE_USERNAME=perf \
SPRING_DATASOURCE_PASSWORD=perf \
SPRING_JPA_HIBERNATE_DDL_AUTO=none \
./gradlew bootRun
```

> 측정 단계에서는 `ddl-auto=none` 권장 (테이블/인덱스 자동 변경 방지).

---

## 6) k6로 리뷰 조회 성능 측정 (No Index Baseline)

```bash
BASE_URL=http://localhost:8080 \
PLACE_ID=<3단계에서 출력된 target_place_id> \
PAGE_SIZE=20 \
k6 run docs/performance/review-loadtest/k6/review-list-no-index.js
```

스크립트 특징:
- `LATEST` / `RATING_DESC`를 분리 시나리오로 동시 실행
- 각 VU가 커서 기반으로 최대 3페이지 연속 조회
- 태그(`sort`) 기준으로 정렬별 p95 비교 가능

---

## 7) 측정 결과 기록 (Baseline)

권장 기록표:

| 항목 | 값 |
|---|---:|
| p95 (LATEST) |  |
| p95 (RATING_DESC) |  |
| 에러율 |  |
| RPS |  |
| DB CPU/Connections |  |

Actuator 같이 보기:
- `/actuator/metrics/http.server.requests`
- `/actuator/metrics/hikaricp.connections.active`
- `/actuator/metrics/jvm.threads.live`

---

## 8) 그 다음 개선 순서 (Baseline 이후)

1. 인덱스 적용 (예: `(place_id, id)`, `(place_id, score, id)`)
2. 동일한 k6 시나리오 재실행
3. Baseline과 비교표 작성
4. 필요 시 N+1 완화, DTO 경량화, 쿼리 튜닝으로 2차 개선

핵심: **먼저 Baseline, 그 다음 개선**.
