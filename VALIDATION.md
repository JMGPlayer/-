# 제출 버전 검증 기록

검증일: 2026-11-15 (한국 날짜)

- release APK 빌드 및 lintVitalRelease 통과.
- debug 단위 테스트 21개 통과, 실패 0개. release 빌드의 런타임 확인은 아래 별도로 수행.
- apksigner 검증 통과, APK Signature Scheme v2 서명 확인. debuggable APK 아님.
- Pleos Connect v2.1.0 / API 34의 임시 읽기 전용 에뮬레이터에서 release APK 신규 설치 및 콜드 실행 성공.
- 시연용 로그인, 알림·속도 권한 허용, 기록 시작·종료 확인.
- 정지 상태 속도 0.0 km/h 수신, 135개 샘플 CSV 저장 및 저장 기록 분석 화면 표시 확인.
- 이번 release 확인은 정지 상태 기본 동작 시험이다. 속도 변화·주행 중 화면 제한·주행거리 연동은 개발 과정에서 시험했으며, 모든 시나리오를 release에서 재시험한 것은 아니다.
- 실차, 공식 계정 인증, 공식 정비 이력 및 차량 가치 평가 검증은 포함하지 않는다.
- 일부 예시 화면의 현대차 계정·연동 문구는 프로토타입 표현이며 실제 인증·공식 제휴를 의미하지 않는다.

APK SHA-256:
`c4b36f9c918fae89cca97f022e69c93c9cd10ea0b60c0287b514ad4551df7043`

서명 인증서 SHA-256:
`7015f8c1d2a402ea42a56ddaabc7f03ce9c79a5db5eb4c970a52acf99f42bfd9`
