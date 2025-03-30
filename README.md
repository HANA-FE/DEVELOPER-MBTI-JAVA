# 개발놈 테스트

본 프로젝트는 개발자 성향 테스트를 실행하는 자바 콘솔 애플리케이션입니다.

## 필요 환경

- Java JDK 8 이상
- JSON Simple 라이브러리

## 설치 및 실행 방법

### 1. 프로젝트 다운로드
```bash
git clone [repository-url]
cd [project-directory]
```

### 2. 라이브러리 설정
이 프로젝트는 JSON Simple 라이브러리를 사용합니다.

#### 직접 JAR 파일 추가 (IntelliJ IDEA)
1. [Maven Repository](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1)에서 JSON Simple JAR 파일 다운로드
2. 프로젝트 내에 `lib` 디렉토리 생성 (없는 경우)
3. 다운로드한 JSON Simple JAR 파일을 `lib` 디렉토리에 복사
4. IntelliJ IDEA에서 JAR 파일 추가하기:
   - `File` > `Project Structure` 메뉴 선택
   - 왼쪽 패널에서 `Modules` 선택
   - `Dependencies` 탭 선택
   - `+` 버튼 클릭 후 `JARs or directories` 선택
   - `lib` 디렉토리에 있는 JSON Simple JAR 파일 선택
   - `OK` 버튼 클릭하여 적용

### 3. 프로그램 실행

#### IntelliJ IDEA에서 실행하기
1. Main.java 파일을 열고 실행 버튼을 클릭합니다.

#### 2. Windows PowerShell에서 실행하기 (한글 인코딩 문제 발생 가능)
PowerShell에서 한글이 깨지지 않도록 하려면 다음과 같이 설정해야 합니다:

```powershell
# 아래 코드에서 경로 부분(cd 명령어 다음)은 반드시 자신의 실제 프로젝트 경로로 수정해야 합니다!
# 1. PowerShell 인코딩 설정
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8

# 2. 프로젝트 디렉토리로 이동 (아래 경로를 자신의 실제 프로젝트 경로로 변경해야 합니다)
cd C:\your\actual\path\to\DEVELOPER-MBTI-JAVA

# 3. Java 프로그램 실행 (큰따옴표와 & 연산자 사용)
# 주의: 클래스 경로(-cp)는 자신의 프로젝트 구조에 맞게 수정해야 합니다
& java "-Dfile.encoding=UTF-8" "-Dsun.stdout.encoding=UTF-8" "-Dsun.stderr.encoding=UTF-8" "-cp" "out\production\DEVELOPER-MBTI-JAVA;lib\*" mbti.Main
```

### 4. 디렉토리 구조 확인
프로젝트에는 다음 디렉토리 구조가 필요합니다:
```
project-root/
  ├── resources/
  │   ├── importData/
  │   │   ├── questions.json
  │   │   └── result.json
  │   └── exportData/
  │       ├── users.json (자동 생성됨)
  │       └── testResults.json (자동 생성됨)
  └── src/
      └── mbti/
          ├── config/
          ├── model/
          ├── service/
          ├── util/
          └── Main.java
```

프로젝트를 처음 실행할 때 `resources/exportData` 디렉토리가 없으면 자동으로 생성됩니다.

### 5. 데이터 파일 설명

### importData/questions.json
테스트에 사용될 질문 목록이 저장된 파일입니다. 다음 형식을 따릅니다:
```json
[
  {
    "text": "질문 내용",
    "choices": ["선택지1", "선택지2"],
    "type": "EI"
  },
  ...
]
```
`type` 필드는 MBTI 성격 유형 쌍(EI, SN, TF, JP)을 나타냅니다.

### importData/result.json
MBTI 유형별 결과 템플릿이 저장된 파일입니다. 다음 형식을 따릅니다:
```json
[
  {
    "mbti": "ISTJ",
    "name": "개발자 유형 이름",
    "hashTag": ["특성1", "특성2", "특성3"],
    "contents": ["상세설명1", "상세설명2", "상세설명3"]
  },
  ...
]
```

### exportData/users.json
사용자 데이터가 저장되는 파일입니다. 프로그램 실행 중에 생성된 사용자 정보를 가지고 있다가 프로그램 종료 시 저장됩니다:
```json
[
  {
    "userName": "사용자 이름"
  },
  ...
]
```

### exportData/userTestResults.json
사용자가 완료한 테스트 결과가 저장되는 파일입니다 마찬가지로 프로그램 종료시 저장됩니다. 다음 형식을 따릅니다:
```json
[
  {
    "userName": "사용자 이름",
    "mbtiType": "ISTJ",
    "mbtiName": "개발자 유형 이름",
    "isCompleted": true,
    "startTime": "2023-03-29 14:30:00",
    "endTime": "2023-03-29 14:35:00",
    "hashTag": ["특성1", "특성2", "특성3"],
    "contents": ["상세설명1", "상세설명2", "상세설명3"]
  },
  ...
]
```
이 파일은 테스트가 완료(`isCompleted`가 `true`)되고 프로그램이 정상적으로 종료될 때만 업데이트됩니다.

## 주의사항

- 테스트 결과와 사용자 정보는 프로그램 종료 시에만 파일에 저장됩니다. 프로그램을 강제 종료하면 데이터가 손실될 수 있습니다.
- 프로그램을 처음 실행할 때는 사용자 데이터와 테스트 결과 파일이 없으므로 자동으로 빈 파일이 생성됩니다.
- Windows PowerShell에서 실행할 때 한글이 제대로 표시되지 않는 문제가 있을 수 있습니다. 위의 인코딩 설정을 적용하면 해결됩니다.
- PowerShell 실행 예제에서 프로젝트 경로(`cd` 명령어 다음)와 클래스 경로(`-cp` 옵션)는 반드시 자신의 실제 개발 환경에 맞게 수정해야 합니다.
