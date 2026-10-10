# 산책하는 강아지

홈 화면과 다른 앱 위에서 작은 강아지가 뛰어다니는 Android 앱입니다.

1. 앱의 표시 권한 설정에서 '다른 앱 위에 표시'를 허용하세요.
2. 앱으로 돌아와 강아지 시작을 누르고 홈 화면으로 나가세요.
3. 강아지를 누르면 점프하고 드래그하면 원하는 위치로 이동합니다.
4. 앱의 강아지 멈추기 또는 알림의 멈추기로 종료하세요.

기존 오늘 할 일 앱을 업데이트하면 이 앱으로 바뀝니다. 인터넷 권한은 사용하지 않습니다.
강아지는 Canvas 캐릭터이며 약 30fps로 움직입니다. 사용자가 시작할 때만 실행하며 재부팅 후 자동 시작하지 않습니다.
일부 보안 화면은 Android가 강아지 표시를 숨길 수 있습니다.

## APK

Actions의 성공한 Build Android APK 실행에서 Artifacts → walking-dog-debug-apk를 내려받고 압축을 풀어 app-debug.apk를 설치하세요.
개발용 APK이며 실제 기기에서 표시 권한, 홈 화면 이동, 드래그, 점프, 화면 회전, 알림 및 앱 버튼으로 종료를 확인해야 합니다.

## 개발 환경

JDK 17, SDK 35, AGP 8.9.2, Gradle 8.11.1.
CI에서 Gradle wrapper를 생성하고 assembleDebug 및 lintDebug를 실행합니다.
로컬에서는 Gradle 8.11.1 설치 후 `gradle wrapper --gradle-version 8.11.1`을 실행하고 `./gradlew assembleDebug lintDebug`로 빌드합니다.
