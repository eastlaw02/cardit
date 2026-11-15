# Cardit (카딧)

**함께 달린 여행을 차 안에서 한 편의 영화로 — 여행이 끝나면 동승자 모두가 주연인 엔딩 크레딧을 차량 디스플레이로 상영하는 Pleos Connect 차량 앱**

<p align="center"><img src="design/app_icon_1024.png" width="160" alt="Cardit 앱 아이콘"></p>

- 여행 중 차량 데이터(주행거리·배터리·와이퍼로 본 강우 시간)를 모아 크레딧 통계로 사용
- 동승자 활동 기록으로 역할(길잡이·분위기 메이커·맛집 탐험가·기록 담당)을 추천
- **주차 중에만 상영**: 주행이 시작되거나 신호를 알 수 없으면 즉시 일시정지하고, 주차하면 같은 지점부터 재개 (fail-safe)
- 상영 중에는 실내 앰비언트 조명을 시네마 밝기로 낮추고, 멈추면 원래 밝기로 복원

---

## APK

| 항목 | 위치 |
|---|---|
| 릴리스 APK (서명됨) | [`release/cardit-car-release-0.1.0.apk`](release/cardit-car-release-0.1.0.apk) |
| GitHub Release | [Releases › 최신 버전](../../releases/latest) |

- 패키지: `com.cardit.car` · versionName `0.1.0` (versionCode 1)
- 설치: `adb install release/cardit-car-release-0.1.0.apk`

## 실행 방법

### APK로 바로 실행

1. 에뮬레이터(아래 [테스트 환경](#테스트-환경-및-필요한-설정) 참고)를 실행합니다.
2. `adb install release/cardit-car-release-0.1.0.apk`
3. 앱 목록에서 **Cardit** 아이콘을 눌러 실행합니다.

### 소스에서 빌드

```bash
./gradlew assembleDebug        # app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug         # 연결된 에뮬레이터/기기에 설치
./gradlew testDebugUnitTest    # 단위 테스트 23개 (게이트·강우·역할 추천·크레딧·조명)
./gradlew assembleRelease      # app/build/outputs/apk/release/app-release.apk
```

Android Studio에서는 이 폴더를 열고 Pleos Connect AVD를 선택해 **Run** 하면 됩니다.

> 릴리스 서명 키(`keystore.properties`, `keystore/*.jks`)는 저장소에 포함하지 않습니다. 키 파일이 없으면 `assembleRelease`는 서명되지 않은 APK를 만듭니다.

### 시연 순서

1. 앱 실행 → 좌측 하단 **⚙(점검 패널)**: 실차 신호가 없으면 "판정 불가 — 상영 차단(fail-safe)"
   - Pleos 에뮬레이터 + Virtual Console이면 실차 신호(CarUxRestrictions)를 그대로 사용
   - 일반 에뮬레이터라면 **시연 신호**를 선택 → 주차/주행/신호 끊김을 직접 전환
2. **출연진**: 활동 기록 기반 추천 역할과 근거 확인
3. **다음: 장면 확인** → 타임라인 → **완성하기** → 상영 대기 → **상영 시작**
4. 크레딧 재생 중 상단 제목을 눌러 점검 패널 → **주행**: 즉시 일시정지 + 차단 화면, 조명 복원
5. 차단 화면 좌상단 칩 → **주차**: 같은 지점부터 자동 재개
6. 재생이 끝나면 쿠키 영상으로 이어짐 (또는 **쿠키 바로보기**)

## 테스트 환경 및 필요한 설정

### 개발 환경

| 항목 | 값 |
|---|---|
| Android Studio | 2025.1.2 이상 (내장 JBR 21 사용) |
| Gradle / AGP | 8.11.1 / 8.10.1 |
| Kotlin | 2.1.21 (Jetpack Compose) |
| compileSdk / targetSdk / minSdk | 36 / 34 / 28 (Pleos 요구: minSdk ≥ 28) |
| Pleos Vehicle SDK | `ai.pleos.playground:Vehicle:2.3.2` |
| Maven 저장소 | `https://nexus-playground.pleos.ai/repository/maven-releases/` |

> ⚠️ 프로젝트 경로에 한글·공백 등 비 ASCII 문자가 있으면 빌드/단위 테스트가 실패합니다 (AGP 경로 검사).
> 명령줄 빌드 시 `JAVA_HOME`을 Android Studio JBR로 지정하세요. 예: `C:\Program Files\Android\Android Studio\jbr`

### Pleos Connect 에뮬레이터 버전과 화면 규격

| 항목 | 값 |
|---|---|
| 에뮬레이터 | **Pleos Connect v2.1.0** (Automotive, API 34 – Pleos Connect 시스템 이미지) |
| 화면 규격 | **1600 × 1280 (가로), 160 dpi (mdpi)** — UI는 이 규격의 Stitch 시안 기준으로 제작 (`ui/theme/DesignScale.kt`가 비율 유지) |
| AVD 설정 | Default boot = **Cold** (Quick boot면 VHAL 사용 불가), RAM 4 GB |
| 차량 신호 | Pleos Connect **Virtual Console**로 기어/주행 상태 조작 |

#### Pleos Connect 에뮬레이터 설치 (최초 1회)

1. Android Studio › **Tools › SDK Manager › SDK Update Sites** › `+`
   - Name: `Pleos Connect System Image`
   - URL (Windows): `https://nexus-playground.pleos.ai/repository/raw-releases/connect-avd/2.x/crp-sys-img.xml`
2. **SDK Platforms** 탭 › `Show Package Details` › **API 34 – Pleos Connect** 시스템 이미지 설치 후 Android Studio 재시작
3. **Tools › Device Manager › Create device** › Form Factor **Automotive** › **Pleos Connect v2.1.0**
4. 언어는 기본 한국어 (Vehicle Settings › General › Language)

#### 일반 Android 에뮬레이터에서 확인할 때

Pleos 이미지 없이도 앱은 설치·실행됩니다. 차량 화면 규격을 맞추고 점검 패널에서 **시연 신호**를 사용하세요.

```bash
adb shell wm size 1600x1280
adb shell wm density 160
# 원복: adb shell wm size reset && adb shell wm density reset
```

이 경우 Vehicle SDK(주행거리·배터리·와이퍼·조명)는 응답하지 않으므로 시연 데이터를 사용하고, 조명 연출은 화면 가장자리 앰비언트 효과로 대체됩니다.

### 필요한 권한

| 권한 | 용도 |
|---|---|
| (별도 권한 없음) CarUxRestrictionsManager | 주행 상태 판정 (상영 게이트) |
| `pleos.car.permission.CAR_DRIVE` | 누적 주행거리 (Odometer) |
| `pleos.car.permission.CAR_ENERGY` | 배터리 잔량 (EvBattery) |
| `pleos.car.permission.CAR_WINDOWS` | 와이퍼 상태 → 강우 시간 |
| `pleos.car.permission.CAR_LIGHTS` / `CONTROL_CAR_LIGHTS` | 앰비언트 조명 조회 / 제어 |

Vehicle SDK는 Vehicle Service **2.3.13 이상**이 설치된 환경(에뮬레이터/차량)에서 동작합니다.

---

## 구조

```
com.cardit.car
├── signal/    신호 계층 — DrivingSignalSource
│   ├── CarUxDrivingSignalSource   AAOS CarUxRestrictionsManager (실차/에뮬레이터)
│   └── FakeDrivingSignalSource    시연용 신호
├── gate/      게이트 로직 — PlaybackGate (순수 함수, fail-safe)
├── vehicle/   Pleos Vehicle SDK — 스냅샷(Odometer/EvBattery/Wiper), 강우 누적, 앰비언트 조명
├── credits/   역할 추천(RoleRecommender), 크레딧 데이터(CreditBuilder)
├── playback/  MediaSession 노출(ScreeningSession)
├── data/      시연 데이터·모델
├── CarditViewModel  화면 전환 · 게이트 연동 · 재생 타이머
└── ui/        Stitch "Cinematic Cockpit" 디자인 (design/DESIGN.md)
```

## 구현 기능

| 기능 | 위치 | 동작 |
|---|---|---|
| 상영 게이트 (fail-safe) | `gate/PlaybackGate.kt` | 주행·신호 없음 → 차단, 주차 → 같은 지점부터 재개 |
| 여행 데이터 집계 | `CarditViewModel.wrapUp()` | 시작/종료 스냅샷 차이로 주행거리·배터리 계산 |
| 강우 시간 | `vehicle/RainAccumulator.kt` | 여행 중 10초마다 와이퍼 상태를 샘플링해 ON 구간 누적 |
| 역할 추천 | `credits/RoleRecommender.kt` | 활동 기록 규칙 기반, 역할 중복 없음 |
| 크레딧 데이터 | `credits/CreditBuilder.kt` | 미지원 통계 제외, '이름만' 권한은 사진 제외, 상영 시 `files/credits.json` 저장 |
| 조명 연출 | `vehicle/CabinLight.kt` | 재생 중 앰비언트 밝기를 하위 20%로, 정지·차단 시 즉시 복원. 미지원이면 화면 효과로 대체 |
| 재생 상태 노출 | `playback/ScreeningSession.kt` | 표준 MediaSession (시스템 재생/일시정지 버튼도 게이트를 거침) |

## 화면 (Stitch 시안 → Compose)

| 화면 | 파일 |
|---|---|
| 여행 보관함(홈) | `HomeScreen.kt` |
| 새 여행 만들기 | `NewTripScreen.kt` |
| 주행 중 (Safe Glance) / 상영 차단 | `DriveScreens.kt` |
| 여행 요약 | `WrapUpScreen.kt` |
| 장르 선택 / 출연진 | `StyleCastScreens.kt` |
| 장면 순서 편집 | `TimelineScreen.kt` |
| 상영 대기 / 크레딧 / 쿠키 | `PlaybackScreens.kt` |

## Vehicle SDK 사용 API (SDK 2.3.2)

| 용도 | API | 권한 |
|---|---|---|
| 누적 주행거리 | `Odometer.getCurrentDistanceDriven` | `CAR_DRIVE` |
| 와이퍼 상태 | `Wiper.getCurrentWiperState(WindowArea(FRONT_WINDSHIELD))` | `CAR_WINDOWS` |
| 배터리 잔량 | `EvBattery.getEvBatteryStateOfCharge` | `CAR_ENERGY` |
| 앰비언트 조명 | `Light.set/getAmbientLightBrightness`, `checkAmbientLightBrightnessCapabilities` | `CAR_LIGHTS` / `CONTROL_CAR_LIGHTS` |

모든 호출은 비동기(`onComplete` / `onFailed`)이며 3초 타임아웃 후 미지원으로 처리합니다.
