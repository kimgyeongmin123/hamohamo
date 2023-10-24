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
#### 2023-09-19 (01Day) : 개발환경 구축(Github / Git / STS / Mysql / AWS ...)
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

##### IntelliJ IDEA 2023-06
<br/>

## ▶️ Software 목록

##### IDE : IntelliJ IDEA 버전명
##### JDK : 11.0.2
##### SpringBoot 2.7.15
##### maven version 3.1.2
##### Git 3.1.1
##### Mysql Server 8.0.33
##### Mysql Workbench 8.0.33
##### Visual Studio Code 1.82.2
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






