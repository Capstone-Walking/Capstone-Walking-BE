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

## 프로젝트 인프라 구성

인프라 레포지토리 바로가기: [walking-infra](https://github.com/Capstone-Walking/Capstone-Walking-IaC)

인프라의 경우 Terraform을 사용하여 AWS에 구성하였습니다.

### 인프라 구성도

![Infra](https://github.com/Capstone-Walking/Capstone-Walking-BE/assets/102807742/67dbd317-8c01-45ed-a7f0-e9ac45406533)
