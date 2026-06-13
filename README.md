# 숨은나라찾기 (2025 관광데이터 공모전 수상작) 🌏

> **국내에서 즐기는 이국적인 여행**
> 해외에 온 듯한 분위기의 국내 관광지를 발견하고, 리뷰·여행 코스·AI 챗봇으로 여행을 계획하는 서비스
<img width="7680" height="3558" alt="image" src="https://github.com/user-attachments/assets/f0c9a792-0d6b-4b77-b5a4-e6b9819bee6c" />


## ✨ 주요 기능

| 도메인 | 설명 |
| --- | --- |
| **관광지 (Place)** | 나라/지역/관광타입/계절 필터링, 정렬, 위치 기반 거리 계산, 페이지네이션 제공. 지도 영역 기반 조회 및 상세/인근 관광지 정보 |
| **여행 코스 (Travel Course)** | 여러 관광지를 묶어 나만의 여행 코스 등록·조회·삭제, 마이페이지 코스 관리 |
| **리뷰 (Review)** | 평점·태그·이미지 리뷰 작성, 커서(키셋) 기반 무한스크롤 조회, S3 Presigned URL 직접 업로드, 장소 통계(평점·리뷰수·대표 해시태그) 자동 집계 |
| **AI 챗봇 (RAG)** | OpenAI + Pinecone 벡터 스토어 기반 RAG 챗봇. 세션 메모리를 활용한 여행지 추천 Q&A |
| **인증 (Auth)** | 카카오 OAuth 소셜 로그인, JWT(Access/Refresh) 발급 및 쿠키 기반 인증 |
| **마이페이지 / 찜 (UserPlace)** | 관심 관광지 저장, 내가 쓴 리뷰·코스 조회 |
| **문의 (Inquiry)** | 사용자 문의 등록 |

<br>

## 🛠 기술 스택

**Language & Framework**
- Java 17
- Spring Boot 3.3.11 (Web, Data JPA, Security, Validation, WebFlux, Actuator)

**Database & Storage**
- MySQL 8
- Redis (Refresh Token / 세션 관리)
- AWS S3 (리뷰 이미지, Presigned URL 업로드)

**AI**
- Spring AI 1.0.3
- OpenAI (`gpt-4o-mini` 챗 / `text-embedding-3-large` 임베딩)
- Pinecone (Vector Store, RAG)

**Auth & Security**
- Spring Security
- JWT (jjwt 0.12.3)
- Kakao OAuth

## 📁 프로젝트 구조

```
src/main/java/com/example/hiddencountry
├── ai          # RAG 챗봇 (OpenAI, Pinecone)
├── place       # 관광지, 지도, 여행 코스, 찜
├── review      # 리뷰, 리뷰 이미지/태그
├── user        # 회원, 카카오 로그인
├── inquire     # 문의
└── global      # 공통 설정 (Security, JWT, S3, 예외, 응답 모델 등)
```
