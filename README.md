# Walking BE

## 프로젝트 소개
- **프로젝트 명**: [뚜벅뚜벅](https://capstonewalking.netlify.app/)
- **프로젝트 기간**: 2024.03 ~ 2024.06
- **프로젝트 목표**: C-ITS 기반 보행자 신호등 정보 제공 서비스
- **프로젝트 팀원**:
  - FE (3명): [박건형](https://github.com/GhoRid), [박주헌](https://github.com/JuHeonParkk), [안호균](https://github.com/hgyn99)
  - BE (3명): [김종준](https://github.com/belljun3395), [나제법](https://github.com/nove1080), [문영상](https://github.com/Moon-1C)

## 프로젝트 개요

C-ITS 정보를 활용하여 보행자 신호등 정보를 실시간으로 알려주는 서비스입니다.

C-ITS란 '차량이 주행 중 운전자에게 주변 **교통상황**과 급정거, 낙하물 등의 사고 위험 정보를 실시간으로 제공하는 시스템'으로 보행자 신호등 정보 또한 C-ITS 정보 중 하나입니다.
<br/><br/>

사용 C-ITS 정보
- [신호제어기 신호 정보 서비스](https://t-data.seoul.go.kr/dataprovide/trafficdataviewopenapi.do?data_id=10119)
- [신호제어기 잔여시간 정보 서비스](https://t-data.seoul.go.kr/dataprovide/trafficdataviewopenapi.do?data_id=10120)

### 프로젝트를 시작하려면

프로젝트 클론
```bash
git clone https://github.com/Capstone-Walking/Capstone-Walking-BE.git
```

프로젝트 루트 디렉토리로 이동

프로젝트 로컬 개발 환경 설정

```bash
cd ./scripts/env
/bin/sh  ./local-develop-setting
```

프로젝트 빌드
```bash
./gradlew api:build -x test
```

프로젝트 실행
```bash
./gradlew api:bootRun --args='--spring.profiles.active=local'
```

## 프로젝트 구조

```
📦 walking-be
 ┣ 📂 api: API 서버 모듈
 ┣ 📂 data: DB 스키마 정의 모듈
 ┣ 📂 api-repository: API 서버와 DB 서버 연결 모듈
 ┣ 📂 member-api: 회원 관련 API 모듈
 ┣ 📂 image-store: 이미지 관련 API 모듈
 ┣ 📂 batch: 배치 기능 모듈
```

해당 프로젝트는 멀티모듈을 사용하여 위와 같은 모듈로 구성되어 있습니다.

### 멀티모듈을 선택한 이유

- 각 모듈을 다른 언어로 개발할 수 있도록 하기 위함 (예: Java, Kotlin)
- 각 모듈별로 역할을 분리하여 개발 및 유지보수가 용이하도록 하기 위함 (예: Member API)
- 각 모듈별로 독립적인 개발 및 테스트가 가능하도록 하기 위함
- 각 모듈별로 의존성을 최소화하기 위함

### 모듈 연관 관계

- `api` 모듈은 `api-repository`, `member-api`, `batch` 모듈에 의존합니다.
- `api-repository` 모듈은 `data` 모듈에 의존합니다.
- `member-api` 모듈은 `api-repository`, `image-store` 모듈에 의존합니다.
- `batch` 모듈은 `data` 모듈에 의존합니다.
  - `batch`를 모듈로 구현한 이유는 배치 서버를 현재는 별도로 구성하지 않고, API 서버에서 `batch` 모듈을 실행하고 있는데 추후 이를 별도의 배치 서버로 분리할 수 있도록 하기 위함입니다.

## 프로젝트 기술 스택

### 공통 기술 스택
- Spring Boot 2.7.5
- Gradle
- Docker
- Git

### API 모듈
- Java 11
- Spring Web
- Spring Security

### Data 모듈
- Java 11
- Spring Data JPA

### API-Repository 모듈
- Java 11
- Spring Data JPA

### Member API 모듈
- Kotlin
- Spring Web
- Spring Cache

### Image Store 모듈
- Kotlin
- S3/Minio(prod/local)

## 프로젝트 인프라 구성

인프라 레포지토리 바로가기: [walking-infra](https://github.com/Capstone-Walking/Capstone-Walking-IaC)

인프라의 경우 Terraform을 사용하여 AWS에 구성하였습니다.

### 인프라 구성도

![Infra](https://github.com/Capstone-Walking/Capstone-Walking-BE/assets/102807742/67dbd317-8c01-45ed-a7f0-e9ac45406533)
