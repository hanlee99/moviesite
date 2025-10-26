# moviesite — 오늘의 영화
> 오늘 무슨 영화를 볼지, 박스오피스 순위를 확인할 수 있는 사이트  

---

## 프로젝트 소개
영화 사이트를 만들기 위해 **공개 API(Open API)**를 활용하여  
영화 정보와 영화관 정보를 가져오는 프로젝트입니다.

- **영화 정보** → KMDB API  
- **박스오피스 순위** → KOBIS API  
- **영화관 정보** → 공공데이터포털 “전국 영화상영관 표준데이터”

박스오피스 순위 API는 인증키가 필요하므로,  
**백엔드(Spring Boot)** 에서 데이터를 요청 후  
**프론트엔드(Thymeleaf, HTML, JS)** 로 전달하는 구조로 설계했습니다.

> 🔗 참고 사이트  
> - [KOBIS OPEN API](https://www.kobis.or.kr/kobisopenapi/homepg/main/main.do)  
> - [KMDB 영화정보시스템](https://www.kmdb.or.kr/main)  
> - [공공데이터포털 - 영화상영관 표준데이터](https://www.data.go.kr/data/15107749/standard.do)

---

## 기술 스택

| 구분 | 사용 기술 |
|------|------------|
| **Backend** | Java, Spring Boot, JPA (Hibernate), Lombok |
| **Frontend** | Thymeleaf, TailwindCSS, DaisyUI, HTML, JavaScript |
| **Database** | H2 Database |
| **Build Tool** | Gradle |
| **API** | KOBIS Open API, KMDB Open API |

---

## 실행 방법
```bash
# 1. 레포지토리 클론
git clone https://github.com/hanlee99/moviesite.git
cd moviesite/demo

# 2. 환경변수 설정 (Windows)
setx KOBIS_API_KEY "발급받은_코비스_API키"
setx KMDB_API_KEY "발급받은_KMDB_API키"

# 3. 실행
./gradlew bootRun
```

## 주요 기능

페이지 접속 시 서버(Spring) 가 KOBIS API를 요청해
전날의 박스오피스 순위 정보를 가져옵니다.

이후 KMDB API를 통해 저장된 영화 정보와 매칭하여
박스오피스 순위별 영화 정보(포스터, 장르, 줄거리 등)를 함께 출력합니다.

타임리프(Thymeleaf)로 초기 페이지를 구성한 후,
이후 동작은 SPA 방식으로 필요한 데이터만 비동기 요청합니다.

차후 기능으로

영화관 목록 페이지

영화관 위치 페이지
추가 예정입니다.

---

## 프로젝트 구조
demo/
┣ src/
┃ ┣ main/
┃ ┃ ┣ java/com/example/demo/
┃ ┃ ┃ ┣ controller/ → 페이지 라우팅 및 API 핸들러
┃ ┃ ┃ ┣ service/ → 비즈니스 로직 (KMDB/KOBIS API 연동)
┃ ┃ ┃ ┣ dto/ → 데이터 전송용 객체
┃ ┃ ┃ ┣ entity/ → JPA 엔티티 (DB 매핑)
┃ ┃ ┃ ┗ repository/ → JPA 리포지토리 인터페이스
┃ ┃ ┗ resources/
┃ ┃ ┃ ┣ data → 영화와 영화관의 csv파일 
┃ ┃ ┃ ┣ templates/ → Thymeleaf 템플릿 (HTML)
┃ ┃ ┃ ┗ static/ → CSS, JS, 이미지 등 정적 파일
┣ build.gradle
┗ README.md

