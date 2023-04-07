# liveAloneCommunity
 2023 1학기 캡스톤 프로젝트입니다.

## Crew And Role
- 엄태인 : 백엔드, 배포
- 이장희 : 프론트엔드, 백엔드
- 이재호 : 백엔드
 
## 개발
- JWT : Spring Security를 이용한 jwt토큰 생성. Refresh Token을 이용한 Access Token의 생명주기 관리 및 토큰 재발해 관리
- Swagger : Swagger-ui를 이용한 api문서 자동 생성
- QueryDSL : QueryDSL을 이용한 쿼리 최적화 및 동적 로직 활용
- email인증 : spring-boot-starter-mail과 google의 메일 기능을 이용해 회원가입시 이메일로 인증 기느 구현
- guava : 구글의 guava를 이용해 댓글로직에서 연관된 객체들의 컬렉션 관리

## progress( by api)
- auth : 회원가입, 로그인, 토큰 재발행 로직
  - 도메인 로직, 비즈니스 로직, 컨트롤러 구현 및 Junit을 이용한 유닛테스트 적용(컨트롤러는 @SpringBootTest, @AutoConfigureMockMvc르 이용해 Mock으로 검증)
- member : 회원 검색, 회원 정보 조회, 회원 닉네임 수정, 회원 비밀번호 수정, 회원 삭제(탈퇴) 로직
  - 도메인 로직, 비즈니스 로직, 컨트롤러 구현 및 Junit을 이용한 유닛테스트 적용(컨트롤러는 @SpringBootTest, @AutoConfigureMockMvc르 이용해 Mock으로 검증)
- email : 회원가입 전에 이메일 인증번호 발송, 인증번호 검증 로직
  - 도메인 로직, 비즈니스 로직 구현 및 Junit으 이용한 유닛테스트 적용(진행중)
- comment : 댓글 생성 로직(진행중)
  - 도메인 로직, 비즈니스 로직, 컨트롤러 구현 및 Junit을 이용한 유닛테스트 적용(컨트롤러는 @SpringBootTest, @AutoConfigureMockMvc르 이용해 Mock으로 검증)
