# 개발자 MBTI 테스트 (콘솔 버전)

개발자를 위한 MBTI 테스트를 자바 콘솔 환경에서 진행할 수 있는 애플리케이션입니다.  
질문에 답변하면 사용자 성향에 맞는 개발자 유형을 확인할 수 있습니다.



## 🛠 기술 스택

![Java](https://img.shields.io/badge/Java_17%2B-007396?style=flat&logo=java&logoColor=white)
![JSON Simple](https://img.shields.io/badge/JSON_Simple-1.1.1-8BC34A?style=flat)
![JLine](https://img.shields.io/badge/JLine-3.29.0-2196F3?style=flat)



## 📋 요구 사항

- Java JDK 17 이상 (Java 21, 23도 호환 가능)
- JSON Simple 1.1.1 (JSON 파싱용)
- JLine 3.29.0 (콘솔 입력 개선용)



## 🏃‍♂️ 실행 방법

### 1. 프로젝트 클론

```bash
git clone [repository-url]
cd DEVELOPER-MBTI-JAVA
```

### 2. 실행 방법

#### ✅ Windows PowerShell (관리자 권한으로 실행)

```powershell
# 인코딩 설정
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
chcp 65001

# 컴파일
javac -encoding UTF-8 -d out/production/DEVELOPER-MBTI-JAVA -cp "lib/*;." $(Get-ChildItem -Path src -Recurse -Include "*.java" | Select-Object -ExpandProperty FullName)

# 실행
java "-Dfile.encoding=UTF-8" -cp "out/production/DEVELOPER-MBTI-JAVA;lib/*" mbti.Main
```

#### ✅ Mac / Linux

```bash
javac -d out -cp "lib/*" src/mbti/Main.java src/mbti/*/*.java -Xlint:unchecked
java -Dfile.encoding=UTF-8 -cp "out:lib/*" mbti.Main
```

#### ✅ IntelliJ IDEA

1. `File > Project Structure` > `Libraries`에서 `lib` 폴더 등록
2. `src/mbti/Main.java` 실행

---

## 🌟 주요 기능

- 12개의 MBTI 질문을 통한 테스트
- 테스트 결과 JSON 형식으로 저장
- 이전 테스트 결과 조회 및 관리
- 메뉴 키보드 방향키(좌,우,Enter)로 조작 가능
- 지연 로딩(Lazy Loading)적용으로 JSON 데이터 효율적 로딩


## 🎞 데모 GIF
| 기능 | GIF |
|:--------:|:--------:|
| 시작<br>화면 | ![java_console](https://github.com/user-attachments/assets/21b624ba-dd24-4fb9-b806-a041841d38ee)  |
| 소개 | ![java_intro](https://github.com/user-attachments/assets/3922ac0d-75d7-43fd-a6a0-82dfb093673d)|
| 사용자<br>관리 |  ![java_user](https://github.com/user-attachments/assets/c3452ce6-44a3-44f0-b6d8-c024e123069a) |
| 서비스 시작 | ![java_test](https://github.com/user-attachments/assets/daa52b49-c2cc-446e-99ac-bc466ae6ee69)   |
| 결과<br>관리 |![java_result](https://github.com/user-attachments/assets/327a6f3d-cdf6-4c88-98a4-0e6362fbb691) |
| 종료 | ![java_quit](https://github.com/user-attachments/assets/7609b629-ee48-489f-b762-6068ebab9d13) |





## 📂 프로젝트 구조

```text
DEVELOPER-MBTI-JAVA/
├── src/
│   └── mbti/
│       ├── config/       # 애플리케이션 설정
│       ├── model/        # 데이터 모델 (Question, Result, User 등)
│       ├── service/      # 비즈니스 로직 (TestService, UserService 등)
│       ├── util/         # 유틸리티 함수
│       └── Main.java     # 진입점
│
├── resources/
│   ├── importData/
│   │   ├── questions.json  # 질문 데이터
│   │   └── result.json     # MBTI 결과 템플릿
│   └── exportData/
│       ├── users.json            # 사용자 정보 저장
│       └── userTestResults.json  # 테스트 결과 저장
│
├── lib/                        # 외부 라이브러리
│   ├── json-simple-1.1.1.jar
│   └── jline-3.29.0.jar
│
└── README.md
```

## ⚠️ 주의사항

- 프로그램이 **정상 종료**되어야 결과가 저장됩니다.
- 처음 실행 시 `exportData` 폴더 및 파일이 자동 생성됩니다.
- Windows에서는 인코딩 설정 없이 실행할 경우 한글이 깨질 수 있습니다.

## 👥 팀원 소개
| 이재혁 | 이봉욱 | 박준희 | 이철우 |
| :---: | :---: | :---: | :---: |
| [![GitHub](https://img.shields.io/badge/GitHub-HYEOK9-181717?style=flat&logo=github)](https://github.com/HYEOK9)| [![GitHub](https://img.shields.io/badge/GitHub-kiv9908-181717?style=flat&logo=github)](https://github.com/kiv9908) | [![GitHub](https://img.shields.io/badge/GitHub-lucy01330-181717?style=flat&logo=github)](https://github.com/lucy01330) | [![GitHub](https://img.shields.io/badge/GitHub-fewolee-181717?style=flat&logo=github)](https://github.com/fewolee) |


