# Car Value Coach

**팀 카로그** — Pleos Connect의 차량 신호를 받아 주행 기록과 정비 안내, 주행 후 코칭으로 연결하는 차량 관리 앱의 1차 심사용 프로토타입입니다.

## 제출 APK

- 파일: [apk/카로그_CarValueCoach_release.apk](apk/카로그_CarValueCoach_release.apk)
- 버전: 1.6 (versionCode 7)
- 패키지: `com.aistudio.carvaluecoach.vkmq`
- Android Studio에서 작업한 최신 Android 소스는 `app/src/`에 있습니다. 기존 Google AI Studio 프로젝트에서 발전시킨 버전입니다.

## 구현 범위와 한계

| 구분 | 현재 상태 |
|---|---|
| Pleos 연동 | 에뮬레이터의 문 상태, 누적 주행거리 및 속도 읽기 |
| 연결 확인 화면 | 설정(톱니바퀴) → 차량 연결 확인 → 연결 확인 열기에서 수신 상태와 기록 조작을 확인. 기본 앱 화면에서는 테스트 항목을 숨김 |
| 주행거리 | 1호차에 연결하고 정비 안내 계산에 반영. 다른 차량은 예시 데이터 |
| 주행 기록 | 사용자가 시작·종료하는 서비스에서 속도와 시각을 수신하고 앱 내부 CSV로 저장 |
| 주행 후 분석 | 저장 기록의 최고 속도, 분석 가능 구간, 누락·오류 및 급가속·급감속 후보 표시 |
| 코칭 | 수신 기록에 따른 규칙 기반 피드백. 생성형 AI 분석 아님 |
| 주행 중 화면 | Pleos의 주행 중 화면 제한을 유지하며 기록 서비스 동작을 시험 |
| 로그인·차량 등급·가치 평가 | 로그인은 시연용이며 현대자동차 계정 인증과 연결되지 않음. 차량 등급·가치 평가 등 예시 화면은 실차 진단 결과가 아님 |

급가속·급감속 후보는 연속 전진 속도에서 약 ±3 m/s² 이상의 변화가 0.8초 이상 이어지는 구간을 대상으로 하는 시연 기준입니다. 0.1~3초 간격의 유효 구간을 분석하며 누락·오류 구간은 제외합니다. 후보가 없다는 결과는 안전 운전 판정이 아닙니다. 속도 기록만으로 차량 상태, 잔존가치, 실제 부품 마모도 또는 연비를 검증하지 않습니다.

## 테스트 환경

- Windows / Android Studio
- Pleos Connect 에뮬레이터 **v2.1.0**, Android 14 / API 34, x86_64
- 화면 **2560 × 1440**, 160 dpi, 12.8인치 하드웨어 프로필
- 에뮬레이터 설정: RAM 4 GB, CPU 4 cores
- Pleos Vehicle SDK **2.3.2**
- 빌드: compileSdk 36.1 / targetSdk 36 / minSdk 28, AGP 9.1.1, Gradle 9.3.1

실차 및 배포 심사 적합성 검증은 수행하지 않았습니다. 일반 휴대폰은 차량 신호 연동 시험 환경이 아닙니다.

## APK 실행 방법

1. Pleos Connect v2.1.0 시스템 이미지와 가상 기기를 설치하고 부팅합니다.
2. APK를 해당 에뮬레이터에 설치합니다. ADB 사용 예: `adb install -r "apk/카로그_CarValueCoach_release.apk"`.
3. 앱 목록에서 **Car Value Coach**를 엽니다. 명령으로 실행할 경우:
   `adb shell am start -n com.aistudio.carvaluecoach.vkmq/com.example.MainActivity`
4. 로그인 화면은 시연용입니다. 실제 계정 비밀번호를 입력하지 말고 기본 예시 입력값으로 로그인합니다.
5. 우측 상단 설정(톱니바퀴) → **차량 연결 확인** → **연결 확인 열기**로 진단 화면을 엽니다. 속도 접근 권한을 허용하고, 필요 시 알림 권한도 허용합니다. 일반 휴대폰에서는 차량 신호를 받을 수 없으므로 연결 실패 안내가 나타날 수 있습니다.
6. 진단 화면에서 주차 상태에 `기록 시작`을 누릅니다. Pleos Virtual Console에서 속도를 변경합니다. 주행 중에는 앱 화면이 제한될 수 있으며 기록은 서비스에서 수행합니다.
7. 속도를 0으로 되돌리고 P 상태에서 진단 화면의 `기록 종료`를 누릅니다. 주행 코치의 `Pleos 수신 기록`에서 저장 기록·분석·코칭을 확인합니다.
8. 누적 주행거리를 변경하면 1호차의 표시와 정비 안내에 반영되는지 확인할 수 있습니다. 다른 차량의 예시 데이터와 구분해 주세요.

기존 debug APK와 제출 release APK는 서명이 다릅니다. 서명 불일치가 나면 **별도의 테스트 가상 기기**를 사용하는 것을 권장합니다. 기존 앱을 삭제하면 내부 주행 기록도 삭제될 수 있습니다.

Virtual Console은 에뮬레이터와 연결되는 로컬 도구입니다. `127.0.0.1` 링크는 심사자의 PC에 자동으로 서버를 만들지 않습니다. Pleos 개발 환경의 콘솔 실행 및 포트 연결 절차가 필요합니다. HTTP 오류가 나면 변경값이 실제 에뮬레이터 계기판에 반영되는지 먼저 확인하세요.

## 소스에서 빌드

Windows에서는 프로젝트를 `C:\dev\CarValueCoach`처럼 한글이 없는 경로에 복제하세요. Android Studio에서 저장소 루트의 `settings.gradle.kts`가 있는 폴더를 열고 SDK 및 Gradle 동기화를 완료합니다. Gradle daemon 설정은 JDK 25를 요청합니다. 필요한 JDK와 의존성 최초 다운로드에는 인터넷이 필요합니다.

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

debug 빌드는 Android 기본 로컬 debug 키를 사용합니다. release 빌드는 본인의 키를 만들고 다음 환경 변수를 설정한 터미널에서 실행합니다. 배포한 APK와 다른 키로 만든 APK는 기존 제출 APK에 업데이트 설치할 수 없습니다.

```powershell
$env:KEYSTORE_PATH = 'C:\private\my-release.jks'
$env:STORE_PASSWORD = '<본인 키 저장소 비밀번호>'
$env:KEY_PASSWORD = '<본인 키 비밀번호>'
# 키 별칭은 upload로 생성합니다.
.\gradlew.bat :app:assembleRelease
```

결과는 `app/build/outputs/apk/release/app-release.apk`입니다. 개인 서명 키, 비밀번호, `.env`, `local.properties`는 저장소에 올리지 않습니다. 본 프로토타입 실행에는 실제 현대차 계정이나 외부 AI API 키가 필요하지 않습니다.

## 소스 안내

- `MainActivity.kt`: 차량 신호 연동과 앱 화면
- `PleosDiagnosticsDialog.kt`: 설정에서 여는 차량 연결 확인 화면
- `ui/dialogs/SettingsDialog.kt`: 차량 연결 확인 진입 메뉴
- `DrivingRecordService.kt`: 주행 기록 서비스와 속도 수신
- `DriveJournal.kt`: 내부 CSV 기록 및 요약 읽기
- `DrivingEventAnalyzer.kt`: 속도 변화 후보 분석
- `DrivingCoaching.kt`: 규칙 기반 코칭
- `ui/screens/CoachingRecordsScreen.kt`: 실제 수신 기록 표시
- `app/src/test/`: 기록·분석·코칭 등 단위 테스트

앞으로는 차량별 정비 기준과 정비 이력, 데이터 품질 및 다양한 주행 시나리오를 검증하고, 차량 가치 평가의 근거와 한계를 명확히 하는 방향으로 개발합니다.


## 제출 버전 확인

빌드·서명·실행 확인 내역은 [VALIDATION.md](VALIDATION.md)를 참고하세요. 루트의 serve_apk.py와 public/는 초기 제작 과정의 보조 파일이며 Android 앱 빌드·실행에는 필요하지 않습니다.
