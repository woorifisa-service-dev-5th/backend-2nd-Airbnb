# 🏠 Servlet Airbnb Clone Project

서블릿과 JSP를 학습하기 위한 Airbnb 클론 프로젝트입니다. 팀 기반 숙소 공유 및 위시리스트 기능을 구현했습니다.

## 📋 프로젝트 개요

- **프로젝트명**: Servlet Airbnb Clone
- **기술 스택**: Java Servlet, JSP, MySQL, HikariCP
- **아키텍처**: MVC 패턴 (Model-View-Controller)
- **목적**: 서블릿/JSP 웹 개발 학습

## 🏗️ 프로젝트 구조

```
src/main/java/
├── config/                 # 설정 클래스들
│   ├── DB.java            # HikariCP 데이터베이스 연결 관리
│   └── AppContextListener.java  # 애플리케이션 생명주기 관리
├── controller/            # 서블릿 컨트롤러
│   ├── HomeServlet.java   # 메인 페이지 (숙소 검색/필터링)
│   ├── LoginServlet.java  # 로그인 처리
│   ├── LogoutServlet.java # 로그아웃 처리
│   ├── AccommodationListServlet.java # 숙소 목록
│   ├── TeamLikeServlet.java # 팀 좋아요 기능
│   └── TeamMemberServlet.java # 팀 멤버 관리
├── dao/                   # 데이터 접근 객체
│   ├── AccommodationDao.java
│   ├── LoginDao.java
│   ├── TeamDao.java
│   ├── TeamAccommodationDao.java
│   └── TeamUserDao.java
├── Filter/                # 필터 클래스들
│   ├── AuthFilter.java    # 인증 필터 (새로 추가됨)
│   └── EncodingFilter.java # 인코딩 필터
├── model/                 # 모델 클래스들
│   ├── User.java
│   ├── Team.java
│   ├── Accomodation.java
│   ├── TeamAccommodation.java
│   ├── AccomCategoryEnum.java
│   ├── BuildingCategoryEnum.java
│   └── DTO/
│       └── UserSessionDto.java
└── service/               # 비즈니스 로직
    ├── LoginService.java
    ├── TeamUserService.java
    ├── TeamAccommodationService.java
    └── impl/
        ├── LoginServiceImpl.java
        ├── TeamUserServiceImpl.java
        └── TeamAccommodationImpl.java
```

## 🗄️ 데이터베이스 스키마

### 주요 테이블
- **users**: 사용자 정보 (id, name, email, password)
- **team**: 팀/공유 폴더 (id, name)
- **accommodation**: 숙소 정보 (카테고리, 침실, 침대, 욕실, 가격 등)
- **team_users**: 팀-사용자 관계 (다대다)
- **team_accom**: 팀-숙소 관계 (좋아요 기능)

### 특징
- ENUM 타입 활용 (숙소 카테고리, 건물 타입)
- 비트마스크로 편의시설 관리
- 외래키 제약조건으로 데이터 무결성 보장

## 🔐 인증 시스템

### AuthFilter
- **목적**: 보호된 리소스에 대한 접근 제어
- **동작**: 모든 요청을 가로채어 세션 기반 인증 확인
- **공개 URL**: `/login`, `/logout`, `/index.jsp`, 정적 리소스
- **인증 실패 시**: 자동으로 로그인 페이지로 리다이렉트

### 세션 관리
- `UserSessionDto` 객체를 세션에 저장
- 로그인/로그아웃 시 세션 생성/무효화
- 인증된 사용자만 보호된 페이지 접근 가능

## 🚀 주요 기능

### 1. 사용자 인증
- 로그인/로그아웃
- 세션 기반 인증
- 인증 필터를 통한 보안 강화

### 2. 숙소 검색 및 필터링
- 숙소 카테고리별 검색 (방/집 전체)
- 건물 타입별 필터링 (주택/아파트/게스트하우스/호텔)
- 침실, 침대, 욕실 수 기준 필터링
- 가격 범위 설정
- 편의시설 비트마스크 기반 필터링

### 3. 팀 기능
- 팀 생성 및 관리
- 팀 멤버 초대/관리
- 팀별 숙소 위시리스트
- 좋아요 기능

## 🛠️ 기술적 특징

### 1. 데이터베이스 연결
- **HikariCP** 커넥션 풀 사용
- 설정 파일(`db.properties`) 기반 DB 연결
- 애플리케이션 종료 시 자원 정리

### 2. 아키텍처 패턴
- **MVC 패턴** 적용
- **DAO 패턴**으로 데이터 접근 분리
- **Service 계층**으로 비즈니스 로직 분리

### 3. 필터 활용
- **EncodingFilter**: UTF-8 인코딩 처리
- **AuthFilter**: 인증 및 권한 제어

### 4. 예외 처리
- 로그인 실패 시 적절한 에러 메시지
- 데이터베이스 연결 실패 시 예외 처리
- 파라미터 검증 및 안전한 형변환

## 🏗️ MVC 패턴 적용 이유 및 구현 상태

### 왜 MVC 패턴을 사용했는가?

#### 1. **관심사의 분리 (Separation of Concerns)**
- **Model**: 데이터와 비즈니스 로직을 담당
- **View**: 사용자 인터페이스와 표현 로직
- **Controller**: 사용자 요청 처리와 Model/View 간 조율

#### 2. **유지보수성 향상**
- 각 계층이 독립적으로 수정 가능
- 코드 재사용성 증대
- 테스트 용이성 향상

#### 3. **팀 협업 효율성**
- 프론트엔드/백엔드 개발자 역할 분담
- 병렬 개발 가능

### 구현 상태

1. **Controller 계층**
   - 서블릿을 통한 요청 처리
   - 파라미터 검증 및 형변환
   - 적절한 리다이렉트/포워드 처리
   - 로깅을 통한 디버깅 지원

2. **Model 계층**
   - DAO 패턴으로 데이터 접근 분리
   - Service 계층으로 비즈니스 로직 캡슐화
   - DTO를 통한 데이터 전달 객체 분리
   - ENUM 활용으로 타입 안전성 확보

3. **View 계층**
   - JSP를 통한 동적 페이지 생성
   - EL/JSTL 활용으로 스크립틀릿 최소화
   - 반응형 CSS 디자인 적용

4. **보안 및 필터**
   - AuthFilter로 인증/인가 처리
   - EncodingFilter로 인코딩 통일
   - 세션 기반 사용자 관리

## 🚀 실행 방법

1. **데이터베이스 설정**
   ```sql
   -- schema.sql 실행
   CREATE DATABASE airbnb_clone;
   USE airbnb_clone;
   -- schema.sql의 모든 테이블 생성 스크립트 실행
   ```

2. **설정 파일 생성**
   - `src/main/resources/db.properties` 파일 생성
   - 데이터베이스 연결 정보 입력

3. **서버 실행**
   - Tomcat 서버에 프로젝트 배포
   - `http://localhost:8080/프로젝트명/` 접속

4. **초기 데이터 입력**
   - `queries.sql`의 샘플 데이터 삽입

## 📚 학습 포인트

### 서블릿/JSP
- 서블릿 생명주기 이해
- 요청/응답 처리
- 세션 관리
- 필터 활용

### 데이터베이스
- JDBC 프로그래밍
- 커넥션 풀 사용
- 트랜잭션 관리

### 웹 보안
- 인증/인가 구현
- 세션 보안
- SQL 인젝션 방지

### 아키텍처
- MVC 패턴 적용
- 계층별 책임 분리
- 의존성 관리

## 🔧 개발 환경

- **Java**: JDK 17
- **서버**: Apache Tomcat 9.0 (Java EE 지원)
- **데이터베이스**: MySQL 8.0
- **IDE**: Eclipse, IntelliJ IDEA
- **빌드 도구**: Maven (권장)

## 📝 향후 개선 사항

- [ ] 회원가입 기능 추가
- [ ] 비밀번호 암호화 (BCrypt)
- [ ] RESTful API 설계
- [ ] 프론트엔드 프레임워크 연동
- [ ] 이미지 업로드 기능
- [ ] 예약 시스템 구현

## 👥 팀 정보

- **프로젝트**: T1F3_AIRBNB
- **목적**: 서블릿/JSP 웹 개발 학습
- **버전**: 1.0.0

---

**참고**: 이 프로젝트는 학습 목적으로 제작되었으며, 실제 서비스에 사용하기 위해서는 보안 강화 및 추가 기능 구현이 필요합니다.