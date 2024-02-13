# 게시판 sample
- 간단한 회원, 게시판 기능을 만들어 다양한 기능을 구현해보는 공부용 proj

## 📝 기능 - 과제 기준 정리
1. 회원가입 기능
   - 복습과제 1/30 : validation - 비밀번호 형식 글자수 등.
   - 복습과제 1/30 : 같은 닉네임이 존재하는지 "확인"
   - 복습과제 1/30 : 비밀번호를 평문으로 저장하는 것이 아닌, 단방향 암호화 알고리즘을 이용하여 암호화
2. 로그인 기능
   - 복습과제 1/31 : 성공시 header로 JWT 토큰 전송
3. JWT 인증 및 인가, 회원 탈퇴 기능
4. 이미지 첨부 기능
   - 복습과제 2/2 : 이미지 업로드 기능
5. 게시글 쓰기 기능
   - 복습과제 2/2 : 토큰 검사 후 유효한 토큰일 때만 게시글 작성 가능
6. 게시글 검색 기능 및 목록 조회 페이징 기능
   - 복습과제 2/1 : 페이징 조회
   - 개선과제 2/1 : QueryDSL 을 사용하여 검색 기능
   - 개선과제 2/2 : NoOffset 기반으로 페이징 조회
   - 개선과제 2/5 : QueryDSL 을 사용하여 동적 쿼리 기능
7. 에러 처리
   - 개선과제 1/31 : CustomException 정의
8. 게시물 단건 조회 기능 및 최근 본 5개 게시물 조회 기능 추가
9. 게시물 조회시 조회수 확인 가능하도록 처리
   - redis 사용
   - 추 후 스케쥴러로 주기적으로 redis의 조회수를 db에 flush 하도록 변경 예정
10. 테스트 코드 작성
   - repository 단위 테스트: searchByWhere 조건 테스트

## ⚙️ 라이브러리
- Spring Web
- Spring Data Jpa
- Spring data redis
- Spring Validation
- Jackson
- QueryDSL
- JWT
- AWS starter sdk
- postgreSQL driver for supabase
- Kotest
- Mockk
