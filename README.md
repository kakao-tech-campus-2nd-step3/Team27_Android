# 🐶 함께 찾는 반려동물, Together Pet
![image](https://github.com/user-attachments/assets/0a98c2d7-bced-4846-9193-adba2231770b)

<br>

## 목차
- [프로젝트 소개 및 기획 의도](#프로젝트-소개-및-기획-의도)
- [팀원 구성](#팀원-구성)
- [배포](#배포)
- [개발 문서](#개발-문서)
- [아키텍처](#아키텍처)
- [프로젝트 구조](#프로젝트-구조)
- [프로젝트 주요 기능](#프로젝트-주요-기능)
- [진행 상황](#진행-상황)
- [주안점을 둔 부분](#주안점을-둔-부분)

<br>

## 프로젝트 소개 및 기획 의도
**Together Pet**은 **잃어버린 반려동물의 제보나, 발견한 실종동물의 정보를 쉽게 공유할 수 있는 서비스**입니다.

매년 반려동물 양육가구가 증가하는 만큼, 유실/유기 동물도 매년 증가하고 있습니다.
<br>하지만 잃어버린 반려동물을 찾는 과정은 정말 쉽지 않습니다. 유기 동물 보호소를 찾아 다니거나 sns, 전단지를 통해 기약 없이 제보를 기다리는 수 밖에 없습니다.

Together Pet은 동물들의 위치 정보를 훨씬 쉽고 빠르게 공유할 수 있는 창구를 제공합니다.
<br>유기동물을 발견한 사람들은 발견한 동물에 대한 정보를 쉽게 많은 사람들에게 공유하거나 주인에게 제보할 수 있고, 이를 통해 반려동물을 잃어버린 주인은 더욱 빠르고 쉽게 반려동물과 재회할 수 있도록 서비스를 기획하였습니다.

![image](https://github.com/user-attachments/assets/da2dece5-5927-42b8-a7c2-7e5491b2a701)
반려동물을 찾는 것을 돕는 큰 힘은 바로 **지역 커뮤니티**라고 생각하였습니다.
<br>산책 기록 기능을 통해 사람들이 일상적으로 앱을 사용할 수 있게 하고,
<br>산책 중 실종 반려동물 정보를 손쉽게 열람하고 공유할 수 있도록 하여 함께 찾기에 대한 참여를 독려합니다.
<br>커뮤니티를 활성화하기 위해 추가로 반려 일기와 우리 동네 커뮤니티 기능을 제공할 예정입니다.

지역 중심의 반려인 커뮤니티를 기반으로, 실종 반려동물 찾기를 자연스럽게 돕는 플랫폼을 제공하여
<br>서로 도움을 주고받을 수 있는 생태계를 구축하고자 합니다.


> :computer: **Note**
>
> MVP(Minimally Viable Produect)에서는 실종 정보/목격 정보 등록 및 열람, 산책 기록을 중심으로 기능을 구현하였습니다

<br>

## 팀원 구성
**[Android]**
<div style="display: flex; justify-content: flex-start;">
    
|                                                     **오진우**                                                      |                                                     **최영빈**                                                      |
|:----------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/user-attachments/assets/8a55698a-8a26-4d0f-af29-1ed5ecf5ebdf" height=150 width=150> | <img src="https://github.com/user-attachments/assets/70ad69bb-82ed-4612-bf58-7b71646c8bcd" height=150 width=150> |
|                                                  **Android_조장**                                                  |                                                 **Android_기획리더**                                                 |
|                                                  스플래시 화면, 간편 로그인, 산책하기 기능                                                 |                                                  홈 화면, 반려동물 같이 찾기 기능                                                    |


**[고마운 조력자]**
<div style="display: flex; justify-content: flex-start;">

|                                                     **이현기**                                                      |
|:----------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/user-attachments/assets/df8f872f-6dd4-47fd-934c-10aede3eafb2" height=150 width=150> |
|                                                  **BE_테크리더**                                                  |
|                                                  서버와의 데이터 송수신 기능                                               |

</div>


> :computer: **Note**
>
> 백엔드 팀 구성은 [백엔드 레포](https://github.com/kakao-tech-campus-2nd-step3/Team27_BE/tree/Evaluation)에서 확인 가능합니다

<br>

## 배포
- [원스토어](https://m.onestore.co.kr/ko-kr/apps/appsDetail.omp?prodId=0000779558)에서 다운로드 할 수 있습니다

  
<img src="https://github.com/user-attachments/assets/0bbd105c-dc2d-4db9-99a8-1d948aaa245d" height=450 width=220>

<br>

<details>
  <summary>추가 정보</summary>
    <br>
  <div style="margin-left: 20px;">
    <ul>
      <li>위치 정보를 원활하게 활용하기 위해 <strong>아마존 앱스토어</strong> 배포를 시도하였습니다.</li>
      <li>원할한 앱 동작을 위해 Retrofit 관련 ProGuard 규칙을 추가하여 아마존 앱스토어에 업데이트 버전을 등록하는 것을 고려하고 있습니다.
        <br>
          <br>
        <img src="https://github.com/user-attachments/assets/2504d4ed-c522-4829-b3ab-2469d1c8ecf9" height="450" width="220" style="margin-left: 20px;">
      </li>
    </ul>
  </div>
</details>

<br>

## 개발 문서
- [피그마-와이어프레임](https://www.figma.com/design/5j2O200pBFZDR4bAit5Iam/KTC-27%EC%A1%B0?node-id=1860-8157&t=4DyB3EUaPz0R6G5c-1)
  <br>

- [피그마-Prototype](https://www.figma.com/proto/5j2O200pBFZDR4bAit5Iam/KTC-27%EC%A1%B0?node-id=1860-4058&node-type=section&t=fFr1Y5zW9251MUsz-1&scaling=scale-down&content-scaling=fixed&page-id=85%3A1081&starting-point-node-id=423%3A4217&show-proto-sidebar=1)
  <br>
  
- [API 명세서](https://quickest-asterisk-75d.notion.site/API-6d3b77b528b14cfa8b7dc8cd81d95872)
  <br>
  
- 개발 환경 설정
    * 안드로이드 스튜디오 버전 : Android Studio Iguana | 2023.2.1 Patch 2
    * java 버전 : 23.0.1
    * Android Gradle Plugin 버전 : 8.3.1
    * Gradle 버전 : 8.4
    * Sdk 버전
        + compileSdk : 34
        + minSdk : 26
        + targetSdk : 34
    * 카카오맵 사용을 위해 실물 기기를 사용하는 것을 권장
    * API 키 사용 : 팀원이 요청할 시 Github Secrets를 통해 공유

<br>

    
  * 프로젝트 브랜치 전략

  <img src="https://github.com/user-attachments/assets/0b00dde1-7cc4-4689-b640-7bb5842bddb1" alt="환경 설정" style="margin-right: 20px;">


## 아키텍처

- 데이터베이스 설계
  * 세 개의 테이블로 이루어진 Room 데이터베이스와 하나의 DataStore를 사용합니다.
    
    1. **실종 테이블** (`missing_db`)
       - 위치를 기준으로 해당 행정동의 실종 제보를 서버에서 수신하여 저장합니다.
    
    2. **제보 테이블** (`missing_db`)
       - 위치를 기준으로 해당 행정동의 실종 의심 제보를 수신하여 저장합니다.
       - 내 반려동물 목격 제보를 수신하여 저장합니다.
       - 실종 의심 제보와 내 반려동물 목격 제보는 `isOwnReport` 값으로 구분됩니다.
    
    3. **산책 테이블** (`missing_db`)
       - 산책 정보를 저장합니다.

         > :computer: **Note**
         >
         > 원스토어 앱 배포를 위해 산책 경로를 그리기 위한 위치 데이터를 서버로 전송하지 않고, 로컬 DB에 저장하여 사용합니다.
             
    4. **DataStore**
       - 가입 시 등록한 정보를 저장하여 UI를 구성 시 사용합니다.
       - 내 반려동물을 실종 유무를 저장하여 UI를 구성 시 사용합니다.
      
    ![data_diagram](https://github.com/user-attachments/assets/3afb07e3-1344-44b5-a188-b56e9e620d2d)

<br>
  
- 아키텍처 구조
  
  <img src="https://github.com/user-attachments/assets/126334ca-fc71-42ae-b4da-8318fdf71f2b" width="600" alt="구조">

<br>

- ERD 
  ![image](https://github.com/user-attachments/assets/7581c761-3477-41ed-a46b-dc5dedc9a17f)
<br><br>


## 프로젝트 구조
```
com.jnu.togetherpet
├── BR.java
├── DataBinderMapperImpl.java
├── MyApplication_GeneratedInjector.java
├── data
│   ├── dao
│   │  
│   ├── database
│   │   
│   ├── datasource
│   │   
│   └── repository
│       
├── di
│   
├── security
│   
├── ui
│   ├── activity
│   │   
│   ├── fragment
│   │   ├── common
│   │   │ 
│   │   ├── home
│   │   │   
│   │   ├── registration
│   │   │   
│   │   ├── searching
│   │   │  
│   │   └── walking
│       
└── viewmodel
```

<br>

## 프로젝트 주요 기능
> :computer: **Note**
>
> 자세한 기능 구현 목록은 [Together_Pet_Wiki](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki)에서 확인 가능합니다

### [Oauth 인증](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EC%B9%B4%EC%B9%B4%EC%98%A4%EB%A1%9C%EA%B7%B8%EC%9D%B8)
| 초기화면                                                                                                      |
|-----------------------------------------------------------------------------------------------------------|
| <img src = "https://github.com/user-attachments/assets/32f4cb4a-b6ee-437f-a2d0-d2287c3865f1" width=300px> |
- Kakao Oauth를 활용하여 사용자가 Kakao 계정으로 회원가입 및 로그인을 할 수 있습니다.
  - 카카오 인증 후 프론트에서 사용자의 이메일을 전송받아 이메일의 형식을 검증합니다.
  - 이메일이 USER 데이터베이스에 존재하지 않을 경우 회원가입을, 존재할 경우 로그인을 수행합니다.
  - 이메일을 토대로 JWT를 이용해 생성한 access token을 전송합니다.
  - access token은 이후 API 요청 시 사용자를 검증하기 위해 사용됩니다.
  
<br>

### [사용자 및 반려동물 정보 입력](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EB%B0%98%EB%A0%A4-%EB%8F%99%EB%AC%BC-%EC%A0%95%EB%B3%B4-%EB%93%B1%EB%A1%9D)
| 회원가입 |
|----------|
|<img src = "https://github.com/user-attachments/assets/64198c17-2850-42ca-89e4-e7401941c9e1" width=300px>|
- 회원가입의 경우, 사용자의 정보와 반려동물 정보를 입력받아 저장할 수 있습니다.
  - 사용자의 정보는 닉네임, 거주 지역을 입력받습니다.
  - 반려동물의 정보는 이름, 개월수, 종, 중성화 여부, 상세 특징을 입력받습니다.

<br>

### 사용자 및 반려동물 정보 가져오기
| 로그인 |
|-----------------------------------------------------------------------------------------------------------------|
|<img src = "https://github.com/user-attachments/assets/8ea75212-dc2a-475b-8d3d-3db1885f04aa" width=300px>|
- 로그인의 경우, 사용자의 정보 및 반려동물의 정보를 가져올 수 있습니다.
  - 사용자의 정보는 사용자의 닉네임을 가져옵니다.
  - 반려동물의 정보는 반려동물 이름, 반려동물 사진, 반려동물의 개월수를 가져옵니다.

<br>

### [실종 제보 조회, 상세보기](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EA%B8%B0-%E2%80%90-%EC%8B%A4%EC%A2%85-%EC%A0%95%EB%B3%B4-%ED%99%94%EB%A9%B4)
| 실종 제보 조회, 상세보기                                                                                            |
|-----------------------------------------------------------------------------------------------------------|
| <img src = "https://github.com/user-attachments/assets/f8854355-ec85-44ee-bb76-a81d0ed36e94" width=300px> |
- 현재 자신의 위치 주변의 실종 제보들을 조회할 수 있습니다.
  - 자신의 위치를 기반으로 주변의 실종 제보들을 확인할 수 있습니다.
  - 화면에 보이는 실종 제보 위치 아이콘을 클릭할 경우 해당 제보의 상세 내용을 확인할 수 있습니다. 

<br>

### [실종동물 제보](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EA%B8%B0-%E2%80%90-%EC%8B%A4%EC%A2%85-%EC%A0%95%EB%B3%B4-%ED%99%94%EB%A9%B4)
| 실종동물 제보                                                                                                              |
|----------------------------------------------------------------------------------------------------------------------|
|<img src = "https://github.com/user-attachments/assets/ff587c7b-8402-4086-b115-81a678000e14" width=300px>|
- 실종된 자신의 애완동물에 대한 정보를 입력하여 제보할 수 있습니다.
  - 애완동물의 정보는 이름, 성별, 개월수, 종, 중성화 여부, 실종 시간, 실종 위치, 상세 설명을 입력받을 수 있습니다.

<br>

### [실종 의심 동물 목격 제보 조회, 상세보기](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EA%B8%B0-%E2%80%90-%EC%A0%9C%EB%B3%B4-%EC%A0%95%EB%B3%B4-%ED%99%94%EB%A9%B4)
| 실종 의심 동물 목격 제보 조회, 상세보기 |
|----------|
|<img src = "https://github.com/user-attachments/assets/356cf40f-bbd5-4393-9e45-80e384aee960" width=300px>|
- 현재 자신의 위치 주변의 실종 의심 동물 목격 제보들을 조회할 수 있습니다.
  - 자신의 위치를 기반으로 주변의 실종 의심 동물 목격 제보들을 확인할 수 있습니다.
  - 화면에 보이는 실종 의심 동물 목격 제보 위치 아이콘을 클릭할 경우 해당 제보의 상세 내용을 확인할 수 있습니다.

<br>

### [실종 의심 동물 목격 제보](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EA%B8%B0-%E2%80%90-%EC%A0%9C%EB%B3%B4-%EC%A0%95%EB%B3%B4-%ED%99%94%EB%A9%B4)
| 실종 의심 동물 목격 제보 |
|----------|
|<img src = "https://github.com/user-attachments/assets/7a5efcc6-01f4-43b7-93a2-9e74aace41ba" width=300px>|
- 사용자가 목격한 실종 의심 동물에 대한 정보를 입력하여 제보할 수 있습니다.
  - 목격한 실종 의심 동물의 정보는 색깔, 종, 성별, 설명을 적을 수 있습니다.
  - 실종 의심 동물에 대한 사진을 업로드하여 등록할 수 있습니다.

<br>

### [실종 제보자에게 실종 동물 목격 제보](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EA%B8%B0-%E2%80%90-%EC%8B%A4%EC%A2%85-%EC%A0%95%EB%B3%B4-%ED%99%94%EB%A9%B4)
| 실종 제보자에게 실종 의심 동물 목격 제보 |
|----------|
|<img src = "https://github.com/user-attachments/assets/70c8155f-ce43-4634-b1e1-ed053a962af8" width=300px>|
- 실종 제보에서 해당 제보자에게 자신이 목격한 실종 동물에 대한 정보를 제보할 수 있습니다.
  - 실종 제보 위치 아이콘에서 제보하기를 클릭할 경우 제보할 수 있습니다.
  - 목격한 실종 동물의 정보는 색깔, 종, 성별, 설명을 적을 수 있습니다.
  - 실종 동물에 대한 사진을 업로드하여 등록할 수 있습니다.

### [나에게 온 실종 의심 동물 목격 제보 확인](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EA%B8%B0-%E2%80%90-%EB%82%B4-%EB%B0%98%EB%A0%A4-%EB%8F%99%EB%AC%BC-%ED%99%94%EB%A9%B4)
| 나에게 온 실종 의심 동물 목격 제보 확인                                                                                   |
|-----------------------------------------------------------------------------------------------------------|
| <img src = "https://github.com/user-attachments/assets/49233edc-9e21-406e-bc72-bed0694246c4" width=300px> |
- 다른 사용자가 나에게 제보한 목격 제보를 확인 할 수 있습니다.
  - 다른 사용자가 나에게 제보한 목격 제보가 있을 경우 상단에 나의 반려동물 이름으로 된 탭이 생성됩니다.
  - 해당 탭을 누를 경우, 목격 제보를 확인할 수 있습니다.
  - 해당 제보는 사진, 색깔, 성별, 품종, 설명을 포함하고 있습니다.

<br>

### [산책 후 산책 결과 등록](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6%EC%82%B0%EC%B1%85-%E2%80%90-%EC%82%B0%EC%B1%85-%ED%99%94%EB%A9%B4)
| 산책 등록 |
|----------------------------------------------------------------------------------------------------------------|
|<img src = "https://github.com/user-attachments/assets/953b1cee-97e8-4372-ae3a-05c64265df09" width=300px>|
- 산책 후 결과에 대한 정보를 서버에 등록할 수 있습니다.
  - 산책에 대한 정보는 산책 경로의 (위도,경도) 리스트, 산책 중 이동 거리, 산책 중 소요 시간을 담습니다.
 
<br>

### [산책 정보 조회](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%F0%9F%90%B6%EC%82%B0%EC%B1%85-%E2%80%90-%EC%82%B0%EC%B1%85-%EA%B8%B0%EB%A1%9D-%ED%99%94%EB%A9%B4)
| 산책 정보 조회 |
|-------------------------------------------------------------------------------------------------------------|
|<img src = "https://github.com/user-attachments/assets/55cfd877-d952-4bf2-a0b6-0d7d6bb58ec1" width=300px>|
- 지금까지 한 산책에 대한 정보들을 날짜에 따라 조회할 수 있습니다.
  - 산책에 대한 각 정보는 산책 경로의 (위도,경도) 리스트, 산책 중 이동 거리, 산책 시작 시간, 산책 종료 시간, 산책 중 소요 시간을 담고 있습니다.
  - 클릭 시 해당 산책에 대한 세부사항을 볼 수 있습니다.

<br>

### [산책 통계 조회]
| 산책 통계 조회 |
|----------------------------------------------------------------------------------------------------------------|
|<img src = "https://github.com/user-attachments/assets/e2f8ef43-39de-4741-bf10-a8f88e6e5f97" width=300px>|
- 해당 날짜의 산책에 대한 통계를 조회할 수 있습니다.
  - 산책 통계에 대한 정보는 해당 날짜 산책 여부, 해당 날짜 산책 횟수, 해당 날짜 산책 총 산책 거리, 해당 날짜 산책 총 산책 시간, 전체 평균 산책 시간을 담고 있습니다.

<br>

## 진행 상황
1. **지도 화면의 중심을 기준으로 데이터 처리**
   - 원스토어에 앱을 배포하기 위해 사용자 위치 정보를 서버에 보내지 않도록 수정했습니다.
   - 대신, 지도 화면의 중심을 기준으로 서버에 데이터를 저장하고 수신하도록 변경했습니다.

2. **반려동물 종류 입력 방식 임시 구현**
   - 배포 일정을 맞추기 위해 사용자가 직접 텍스트로 입력하는 방식으로 임시 처리했습니다.
   - 만약 DB에 등록되지 않은 품종이 입력되면 기본값으로 설정되어 서버에 전송됩니다.
   - 추후 업데이트에서 선택 방식으로 개선할 예정입니다.
   - 현제 DB에 등록 된 종은 [Together_Pet_Wiki](https://github.com/kakao-tech-campus-2nd-step3/Team27_Android/wiki/%ED%98%84%EC%9E%AC-DB%EC%97%90-%EB%93%B1%EB%A1%9D-%EB%90%9C-%EC%A2%85)에서 확인 하실 수 있습니다.
  
3. **거주지 입력 필드 제거 계획**
   - 반려동물 정보 등록 페이지의 거주지 입력 부분은 현재 위치 정보를 바탕으로 앱이 동작하기 때문에 의미가 없어졌습니다.
   - 이 필드는 추후 삭제하고, 업데이트 버전에 반영할 계획입니다.
  
4. **알림 서비스 구현 중단**
   - 유저의 현재 위치를 기준으로 실종 등록 시 알림을 보내는 서비스 API를 개발하였으나, 원스토어 앱 배포를 위해 해당 기능 구현을 중단한 상황입니다.
   - 향후 알림 서비스 기을 추가하여 다른 환경에서 배포할 계획입니다.
  
<br>

### 주안점을 둔 부분
1. **산책 기능**
   - 산책 기능에서는 사용자가 이동하는 경로를 지도에 표시하고, 백그라운드에서도 안정적으로 동작하도록 구현하는 데 중점을 두었습니다.
       * 산책 루트 표시 : 사용자가 이동하는 경로를 실시간으로 지도에 표시하는 기능을 구현하였습니다.
       * 백그라운드 동작 : 사용자들이 산책 기능을 활성화한 후 산책 화면을 보지 않고 다른 작업을 할 가능성이 높다고 판단하여, **트래킹이 백그라운드에서도 지속될 수 있도록** 구현하였습니다. 또한, 다시 산책 화면으로 돌아왔을 때 기존의 경로와 거리 데이터가 **끊김 없이 업데이트되도록** 하는 데 중점을 두어 사용자 경험을 향상시켰습니다.
       * 프로젝트 초창기 GPS 앱이 익숙치 않아 테스트를 위한 산책을 열심히 했던 팀장님이 생각나네요🤣

2. **같이 찾기 화면**
     - 같이 찾기 화면에서는 서버에서 데이터를 받아와 리사이클러뷰, 지도 핀, 바텀 네이게이션 뷰에 정보를 표시하는 기능을 구현하였습니다.
         * 데이터 통신 및 UI 갱신 : 서버에서 데이터를 받아와서 뷰에 표시하는 과정에서 네트워크 통신 속도와 UI 생명주기, 비동기에 대한 고민이 많았습니다.
         * 멘토링 시간에 미러링으로 한시간 넘게 지도 라벨 쪽을 디버깅 했던 기억이 아직도 생생합니다🤣
