09PROJECTPLAN
=

## ▶️ 개발 동기

##### 그림판을 사용해 기존에 사진과 영상만 올릴 수 있는 sns와 차별을 두어
##### 다양하게 소통할 수 있는 sns를 만들고자 하였습니다.
<br/>

## ▶️ 개발 목표

##### 그림판 기능이 추가된 sns 구현
<br/>

## ▶️ 개발 일정
#### 2023-09-11 ~ 2023-09-13(03Day) : 요구사항분석 / 유스케이스 / 
#### 2023-09-14 ~ 2023-09-18(05Day) : 스타일가이드 / 스토리보드 / ERD / ClassDiagram / Sequence Diagram
#### 2023-09-19 (01Day) : 개발환경 구축(Github / Git / IntelliJ / Mysql / AWS ...)
#### 2023-09-20 ~       : 개발중...

<br/>

## ▶️ 구성인원 

##### 박영민(조장) : FrontEnd, BackEnd (로그인, 회원가입,팔로우(친구추가) 구현, 캔버스 API)
##### 김경민(조원1) : FrontEnd, BackEnd (전체적인 디자인 구현, 마이 페이지 CRUD 구현, 알림 구현)
##### 김성진(조원2) : BackEnd (로그인, 회원가입, 검색, 댓글 구현, 메시지 CRUD 구현)
##### 장유진(조원3) : BackEnd (게시물관련 CRUD 페이지 구현, 결제(이모티콘) 구현)
<br/>

## ▶️ 개발 환경(플랫폼)

##### OS : WINDOW Server 2022 base
##### CPU SPEC : I7 Intel 
##### RAM SPEC : 16GB SAMSUNG DDR4
##### DISK SPEC : 100GB SSD 

<br/>

## ▶️ IDE 종류

##### IntelliJ IDEA 2023.2 (Community Edition)
<br/>

## ▶️ Software 목록

##### IDE : IntelliJ IDEA 2023.2 (Community Edition)
##### JDK : 11.0.2
##### SpringBoot 2.7.15
##### spring.dependency-management version 1.0.15.RELEASE
##### gradle version 8.2.1
##### Git 3.1.1
##### Mysql Server 8.0.33
##### Mysql Workbench 8.0.33
##### Visual Studio Code 1.82.2
<br/>

## ▶️ DB Query
09PROJECTPLAN
=

## ▶️ 개발 동기

##### 그림판을 사용해 기존에 사진과 영상만 올릴 수 있는 sns와 차별을 두어
##### 다양하게 소통할 수 있는 sns를 만들고자 하였습니다.
<br/>

## ▶️ 개발 목표

##### 그림판 기능이 추가된 sns 구현
<br/>

## ▶️ 개발 일정
#### 2023-09-11 ~ 2023-09-13(03Day) : 요구사항분석 / 유스케이스 / 
#### 2023-09-14 ~ 2023-09-18(05Day) : 스타일가이드 / 스토리보드 / ERD / ClassDiagram / Sequence Diagram
#### 2023-09-19 (01Day) : 개발환경 구축(Github / Git / IntelliJ / Mysql / AWS ...)
#### 2023-09-20 ~       : 개발중...

<br/>

## ▶️ 구성인원 

##### 박영민(조장) : FrontEnd, BackEnd (로그인, 회원가입,팔로우(친구추가) 구현, 캔버스 API)
##### 김경민(조원1) : FrontEnd, BackEnd (전체적인 디자인 구현, 마이 페이지 CRUD 구현, 알림 구현)
##### 김성진(조원2) : BackEnd (로그인, 회원가입, 검색, 댓글 구현, 메시지 CRUD 구현)
##### 장유진(조원3) : BackEnd (게시물관련 CRUD 페이지 구현, 결제(이모티콘) 구현)
<br/>

## ▶️ 개발 환경(플랫폼)

##### OS : WINDOW Server 2022 base
##### CPU SPEC : I7 Intel 
##### RAM SPEC : 16GB SAMSUNG DDR4
##### DISK SPEC : 100GB SSD 

<br/>

## ▶️ IDE 종류

##### IntelliJ IDEA 2023.2 (Community Edition)
<br/>

## ▶️ Software 목록

##### IDE : IntelliJ IDEA 2023.2 (Community Edition)
##### JDK : 11.0.2
##### SpringBoot 2.7.15
##### spring.dependency-management version 1.0.15.RELEASE
##### gradle version 8.2.1
##### Git 3.1.1
##### Mysql Server 8.0.33
##### Mysql Workbench 8.0.33
##### Visual Studio Code 1.82.2
<br/>

## ▶️ DB Query
![image](https://github.com/yujin7697/1024/assets/122248974/4f65a58b-9d5c-4335-8b19-9c561b123515)



<br/>



## ▶️ 사용빌드도구(gradle) 

###     WEB
##### 	implementation 'org.springframework.boot:spring-boot-starter-web'
#####   - 웹 애플리케이션 개발, RESTful, Spring MVC, Spring Security
###     THYMELEAF
##### 	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
#####   - thyeleaf 템플릿 사용
###     LOMBOK
#####   compileOnly 'org.projectlombok:lombok'
#####   annotationProcessor 'org.projectlombok:lombok'
#####   - 애노테이션을 사용하여 getter,setter 등 매서드 생성
###     SECURITY
#####   implementation 'org.springframework.boot:spring-boot-starter-security'
#####   implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
#####   - Spring Boot 애플리케이션 통합
###     OAUTH2
##### 	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
##### 	- naver, kakao login(API 서비스 사용)
###     FILE UPLOAD
##### 	implementation 'commons-fileupload:commons-fileupload:1.5'
##### 	- 파일 업로드, 멀티파트 데이터 추출 
###     DATABASE
#####   implementation 'com.mysql:mysql-connector-j:8.1.0'
#####  	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
#####   - MYSQL 데이터베이스 연동, Spring Boot JDBC 지원
###     ORM_JPA
#####   implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
#####   - 객체-관계 매핑 
###     02CONTROLLER_VALIDATION
#####   implementation 'org.hibernate.validator:hibernate-validator'
#####   implementation 'javax.validation:validation-api'
#####   - Java Bean Validation API 제공, Dto와 Entity 간 데이터 일치성 보장
###     websocket
#####   implementation 'org.springframework.boot:spring-boot-starter-websocket'
#####   - 실시간 양방향 통신, 실시간 알림 기능

<br/>




## ▶️ DevOps 

##### Amazon Web Service EC2(Deploy Server)
##### Amazon Web Service RDS(Remote Datebase Server)
##### Git & Github
##### Docker(Server Image)
##### Swagger(API Document)
##### Adobe XD
<br/>



## ▶️ 사용(or 예정) API

##### 카카오, 네이버 로그인 API
##### 다음 주소 API
##### 결제 API
##### Canvas API

<br/>

## ▶️ 기술스택

![JAVA](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![SpringBoot](https://img.shields.io/badge/springboot-8A2BE2)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)


[참고 배지 싸이트] <br/>
https://badgen.net/ <br/>
https://shields.io/


<br/>

## ▶️ END POINT 

|END POINT|METHOD|DESCRIPTION|
|------|---|---|
|/list|GET|sns의 모든 게시물 목록 표시|
|/post|POST|sns의 게시물 첨부하기|
|/read|GET|sns의 게시물 1건 보기|
|/board/update|POST|sns의 게시물 수정하기|
|/profile/update|POST|내 정보 수정|
|/profile/leave_auth|GET|회원 탈퇴 페이지|
|/login|GET|sns로그인|
|/join|POST|sns회원가입|
|/mypage|GET|로그인 된 사용자의 마이페이지|

<br/>







<br/>



## ▶️ 사용빌드도구(gradle) 

###     WEB
##### 	implementation 'org.springframework.boot:spring-boot-starter-web'
#####   - 웹 애플리케이션 개발, RESTful, Spring MVC, Spring Security
###     THYMELEAF
##### 	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
#####   - thyeleaf 템플릿 사용
###     LOMBOK
#####   compileOnly 'org.projectlombok:lombok'
#####   annotationProcessor 'org.projectlombok:lombok'
#####   - 애노테이션을 사용하여 getter,setter 등 매서드 생성
###     SECURITY
#####   implementation 'org.springframework.boot:spring-boot-starter-security'
#####   implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
#####   - Spring Boot 애플리케이션 통합
###     OAUTH2
##### 	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
##### 	- naver, kakao login(API 서비스 사용)
###     FILE UPLOAD
##### 	implementation 'commons-fileupload:commons-fileupload:1.5'
##### 	- 파일 업로드, 멀티파트 데이터 추출 
###     DATABASE
#####   implementation 'com.mysql:mysql-connector-j:8.1.0'
#####  	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
#####   - MYSQL 데이터베이스 연동, Spring Boot JDBC 지원
###     ORM_JPA
#####   implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
#####   - 객체-관계 매핑 
###     02CONTROLLER_VALIDATION
#####   implementation 'org.hibernate.validator:hibernate-validator'
#####   implementation 'javax.validation:validation-api'
#####   - Java Bean Validation API 제공, Dto와 Entity 간 데이터 일치성 보장
###     websocket
#####   implementation 'org.springframework.boot:spring-boot-starter-websocket'
#####   - 실시간 양방향 통신, 실시간 알림 기능

<br/>




## ▶️ DevOps 

##### Amazon Web Service EC2(Deploy Server)
##### Amazon Web Service RDS(Remote Datebase Server)
##### Git & Github
##### Docker(Server Image)
##### Swagger(API Document)
##### Adobe XD
<br/>



## ▶️ 사용(or 예정) API

##### 카카오, 네이버 로그인 API
##### 다음 주소 API
##### 결제 API
##### Canvas API

<br/>

## ▶️ 기술스택

![JAVA](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![SpringBoot](https://img.shields.io/badge/springboot-8A2BE2)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)


[참고 배지 싸이트] <br/>
https://badgen.net/ <br/>
https://shields.io/


<br/>

## ▶️ END POINT 

|END POINT|METHOD|DESCRIPTION|
|------|---|---|
|/list|GET|sns의 모든 게시물 목록 표시|
|/post|POST|sns의 게시물 첨부하기|
|/read|GET|sns의 게시물 1건 보기|
|/board/update|POST|sns의 게시물 수정하기|
|/profile/update|POST|내 정보 수정|
|/profile/leave_auth|GET|회원 탈퇴 페이지|
|/login|GET|sns로그인|
|/join|POST|sns회원가입|
|/mypage|GET|로그인 된 사용자의 마이페이지|

<br/>





