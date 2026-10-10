# 오늘 할 일

Android 6.0 이상에서 실행되는 작은 할 일 목록 앱입니다.

- 할 일 추가, 완료 표시, 삭제
- 앱을 종료해도 기기 내 저장 유지
- 인터넷 연결 및 별도 계정 불필요
- 한국어 화면과 빈 목록 안내

## APK 만들기

GitHub에 프로젝트를 올리면 Actions → Build Android APK에서 빌드합니다.
초록색으로 완료된 실행의 Artifacts에서 daily-todo-debug-apk를 내려받고 ZIP을 풀면 설치 가능한 app-debug.apk가 있습니다.
이는 개발용 서명 APK입니다. Play Store 배포에는 별도의 릴리스 서명 구성이 필요합니다.

## 개발 환경

JDK 17, Android SDK 35, Gradle 8.11.1, Android Gradle Plugin 8.9.2.
Android Studio에서 프로젝트 폴더를 열거나 Gradle 8.11.1 설치 후 다음을 실행합니다.

```sh
gradle wrapper --gradle-version 8.11.1 --distribution-type bin
./gradlew assembleDebug lintDebug
```

Windows에서는 마지막 명령의 ./gradlew 대신 gradlew.bat를 사용합니다.
첫 CI 빌드에서 공식 Gradle로 wrapper를 생성하며, 저장소에 wrapper 바이너리는 아직 포함하지 않습니다.
