# Team27_Android
## Team27_Android 8주차 과제 제출

### 구현한 내용
- 로그인 및 정보 등록 api 통신 구현
- 산책 완료시 저장 api 통신 구현
- HTTP 통신위한 코드 리펙토링 중
- 위도-경도 정보를 주소 정보로 변환하는 기능 추가

### 코치님께 피드백 요청드리고 싶은 부분
#### 1. loginWithKakaoTalk 함수 AndroidRuntimeException
- kakaoLogin 관련 함수들 중 loginWithKakaoTalk는 내부적으로 카카오톡으로 연결하기 위해 startActivity() 함수를 사용하는 것 같습니다. 그래서 activity에 위치하지 않으면 실행이 되지 않고 오류가 뜨는 상황입니다.
- 이러한 경우 kakaoLogin 관련 함수들을 repository나 다른 곳에 위치시키지 않고 activity에 위치시켜야 할 지 궁금합니다.

