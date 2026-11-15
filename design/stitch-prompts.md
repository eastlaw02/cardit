# Stitch 프롬프트 — 카딧(CARDIT) 차량 앱

## 사용법
1. Stitch에서 **Web(가로형)** 모드를 선택한다. 차량 디스플레이는 가로형 와이드 화면이다.
2. **0번 디자인 시스템 프롬프트**로 첫 화면을 만든다. 그다음 같은 프로젝트에서 1~10번을 한 화면씩 입력한다. 한 번에 한 화면씩 넣어야 결과가 가장 안정적이다.
3. 각 화면 프롬프트 끝에는 "Keep the same design system as previous screens."가 붙어 있다. 그래야 톤이 유지된다.
4. 마음에 들지 않는 부분은 짧게 후속 수정 요청한다. 예: `Make the credit cards larger and reduce text`.
5. 프롬프트는 영어로 되어 있고, 화면에 들어갈 문구는 한국어로 따옴표 안에 넣었다. Stitch는 영어 지시를 더 정확히 따르기 때문이다.

> Pleos 가이드에서 반영한 내용
> - 시스템 UI는 앱이 그리지 않는다. 상단 상태바, 좌측 GNB, 드라이빙 뷰가 여기에 해당한다. 그래서 프롬프트는 **앱 윈도우 영역만** 그리도록 지시한다.
> - 앱 윈도우는 단독(2:0) 가로형과 1:1 분할 세로형을 모두 지원해야 한다.
> - 서체: Asta Sans (Pleos 시스템 폰트, 한/영)
> - 아이콘: 48dp 그리드, Filled/Outlined 두 스타일
> - 터치: 누르면 95%로 축소되고 배경색이 바뀐다.
> - 그림자 3단계: L1 y6 blur24 8%, L2 y8 blur32 16%, L3 y20 blur40 24%
> - 원칙: Easy & Safe, Glanceability(1~2초 안에 파악), 주행 중 영상·자동 스크롤 금지

---

## 0. 디자인 시스템 (첫 프롬프트)

```
Design an in-car infotainment app called "CARDIT" (카딧) for the Pleos Connect platform (Android Automotive, Hyundai Motor Group). The app turns a road trip's photos, videos and in-car moments into a movie-style ending credit and a post-credit "cookie" scene, watched together in the parked car.

Draw ONLY the app window area of a wide landscape car display (approx. 16:9). Do not draw the system status bar, side navigation bar or driving cluster — the OS provides those.

Visual direction: base it on the Pleos Connect in-vehicle design language (clean, calm, dark theme, translucent layered surfaces, large touch targets) but push it to feel modern and cinematic, like a premium streaming app meets a film festival:
- Dark theme. Background near-black with a subtle deep-navy tint (#0B0D12), soft radial glow behind key content, very subtle film-grain texture.
- Surfaces: frosted glass cards (white 6–10% opacity, background blur, 1px white 8% hairline border), corner radius 28px for cards, 20px for buttons, fully rounded pills for chips.
- Accent: warm cinema gold (#F5C518) as primary. Genre accents used only inside genre-themed contexts: Comedy = sunny coral (#FF8A5B), Action = electric red (#FF3B3B), Emotional = dusk lavender (#9B8CFF).
- Typography: Asta Sans (fallback Pretendard). Big confident headlines (48–64px bold), titles 32px, body 24px, labels min 18px. High contrast white text, secondary text white 60%.
- Layout: bento-grid cards, generous spacing (32px gutters), a maximum of 3–4 primary pieces of information per screen so it is glanceable in 1–2 seconds.
- Touch targets at least 80px tall; primary buttons are wide pill buttons with gold fill and dark text.
- Elevation: soft shadows only (y8 blur32 16% black), no harsh outlines.
- Icons: rounded outlined style on a 48px grid, filled style for active state.
- Imagery: warm, candid travel photos of a group of friends in their 20s (beach, food, road, car interior), with cinematic letterbox crops.

First screen — Home "여행 보관함":
- Top-left: CARDIT wordmark with a small film-perforation motif; top-right: circular profile avatar and a "+ 새 여행" pill button.
- Hero card (left, large): the ongoing trip "부산 우정여행" with a cinematic cover photo, a gold "진행 중" badge, participant avatar stack (4 people), and stats chips "128km · 사진 214 · 하이라이트 12".
- Right column: two smaller cards "지난 여행" with poster-style covers like movie posters (title, date, "엔딩 다시 보기" play button).
- Bottom: horizontal row of poster thumbnails of past trips labeled like a film series ("EP.1 강릉", "EP.2 여수").
```

---

## 1. 새 여행 만들기 (프로젝트 생성)

```
Screen: "새 여행 만들기" — create a new trip project. Landscape app window only.
- Left half: large poster preview that updates live — a movie-poster style card with the trip title "부산 우정여행" in a bold cinematic font, date "2026.11.21 – 11.22", and placeholder cast names at the bottom like a real film poster billing block.
- Right half: simple form on a glass card: "여행 제목" text field, "목적지" field with location icon ("부산 해운대"), "함께하는 사람" section showing 4 participant chips with avatars and a "+ 초대" chip, and a permission selector per person as small segmented pills: "이름만", "게스트", "멤버".
- Bottom-right: wide gold pill button "여행 시작하기".
- A small consent notice row with a microphone icon: "차량 내 대화 기록은 참여자 전원 동의 후에만 켜집니다".
Keep the same design system as previous screens.
```

## 2. 여행 중 — 주행 중 화면 (안전 우선)

```
Screen: trip in progress while DRIVING. This screen must follow strict driver-distraction rules: no video, no animation, no auto-scrolling text, very few elements, huge type.
- Center: large calm status "여행 중" with a slowly breathing gold dot (static in the design), trip title "부산 우정여행" below in white 60%.
- Three big glanceable stat tiles in a row: "주행 126km", "하이라이트 12", "녹음 중" (with a red recording dot and "참여자 동의됨" caption).
- One single large button at the bottom: "지금 이 순간 쿠키로 저장" with a cookie/star icon — big enough to tap with one hand (at least 96px tall).
- Mostly empty dark background with a very subtle road-line gradient. Minimal, safe, premium.
Keep the same design system as previous screens.
```

## 3. 주행 중 상영 차단 화면

```
Screen: playback blocked because the car is moving. Calm, not alarming.
- Centered composition: a large outlined film-reel icon with a pause symbol, headline "주차 후에 상영할 수 있어요", sub text "안전을 위해 주행 중에는 엔딩 크레딧이 재생되지 않습니다. 정차하면 이어서 재생돼요."
- Below: a small progress indicator showing where playback was paused: "02:14 / 04:30에서 일시정지됨".
- Dimmed blurred still of the credit video in the background (heavily darkened, 85% black overlay).
- No buttons except a subtle text button "홈으로".
Keep the same design system as previous screens.
```

## 4. 여행 종료 — 여행 요약 리포트

```
Screen: "여행이 끝났어요" — trip wrap-up summary, shown after parking. Feels like the end of a film festival screening ticket.
- Headline top-left: "부산 우정여행, 촬영 종료" with a clapperboard icon.
- Bento grid of stat cards using vehicle data: "총 주행거리 312km" (with a thin route line illustration Ulsan → Busan → Ulsan), "비 오는 길 42분" (rain/wiper icon), "배터리 사용 64%" (battery gauge), "사진·영상 286개", "하이라이트 18개", "차 안의 웃음 23회" (waveform visual).
- One card shows a mini photo collage of the best moments.
- Bottom-right primary button "엔딩 크레딧 만들기"; secondary text button "나중에 하기".
Keep the same design system as previous screens.
```

## 5. 영화 장르(스타일) 선택

```
Screen: choose the film style for the ending credit. Three large selectable cards side by side, each like a movie poster with its own genre mood:
- "코미디" — sunny coral accent, playful rounded typography, bright candid photo of friends laughing, tag "통통 튀는 전환 · 경쾌한 BGM".
- "액션" — electric red accent, bold condensed italic typography, motion-blur road photo, tag "빠른 컷 · 강렬한 비트".
- "감성" — dusk lavender accent, elegant serif-like typography, golden-hour seaside photo, tag "느린 페이드 · 잔잔한 피아노".
- Selected card is slightly larger with a glowing accent border and a check badge; others are dimmed to 60%.
- Top: title "어떤 영화로 만들까요?"; bottom-right: "다음" gold pill button; bottom-left: small "미리듣기" play chip for BGM.
Keep the same design system as previous screens.
```

## 6. 출연진(역할) 정하기

```
Screen: "출연진" — cast and role assignment, styled like a film's cast page.
- Four large portrait cards in a row, each with a photo of the person, their name (e.g. "민지", "준호", "서연", "태윤"), and an AI-recommended role in gold: "길잡이", "분위기 메이커", "맛집 탐험가", "기록 담당".
- Under each role a tiny reason caption: e.g. "길 안내 기록 9회", "하이라이트 지정 7회", "식당 사진 31장".
- Each card has an edit pencil icon button and a small sparkle "추천" badge.
- A bottom sheet style row of extra role chips to swap in: "감독", "조연출", "음악 감독", "스턴트", "카메오".
- Primary button "다음: 장면 확인".
Keep the same design system as previous screens.
```

## 7. 장면 순서 편집 (타임라인)

```
Screen: scene editor with a simple timeline — large and touch-friendly, not a pro video editor.
- Top area: large preview frame (16:9, letterboxed) showing the selected scene with the credit text overlay "길잡이  민지".
- Bottom: horizontal timeline of chapter blocks with clear labels: "오프닝" → "여행 장면" → "크레딧" → "THE END" → "쿠키 영상" → "TO BE CONTINUED". Each block shows thumbnails; the cookie block has a gold star marker.
- Scene thumbnails are big (at least 120px tall) with drag handles; one is shown mid-drag with elevation shadow.
- Right side panel: "AI 추천 하이라이트" list with waveform icons, e.g. "차 안 웃음 폭발 · 00:42", each with a "+" add button.
- Top-right: "시연판 1분 / 정식판 4분" segmented toggle and "완성하기" gold pill button.
Keep the same design system as previous screens.
```

## 8. 상영 대기 (차 안 영화관 모드)

```
Screen: pre-show screen — the car becomes a small movie theater. Only shown while parked.
- Full-bleed dark cinematic scene: a stylized theater curtain gradient or a soft spotlight glow from the top.
- Center: movie title card "부산 우정여행" in the selected genre's typography, small caption "A CARDIT ORIGINAL · 2026", rating-like badge "4인 출연".
- A row of status chips: "주차 확인됨" (green check), "실내 조명 연출 켜짐" (ambient light icon with gold glow), "오디오 준비됨".
- One huge circular gold play button with text "상영 시작".
- Subtle text at bottom: "주행을 시작하면 자동으로 일시정지됩니다".
Keep the same design system as previous screens.
```

## 9. 엔딩 크레딧 재생 화면

```
Screen: the ending credit playing, full app window, cinematic.
- Black background, letterboxed layout: left 55% shows a large photo/video frame of a friend mid-laugh with a soft vignette; right 45% shows rolling credit text in classic film-credit layout: small uppercase role on the left, name on the right, e.g. "길잡이 ........ 민지", "분위기 메이커 ........ 준호", "맛집 탐험가 ........ 서연".
- A small gray-scale caption above credits: "촬영지  부산 해운대 · 광안리 · 감천문화마을", "총 주행  312km".
- Minimal translucent playback controls at the bottom that look auto-hidden: thin progress bar in gold, pause, 10s back, volume.
- Top-right: tiny "쿠키 영상 있음" badge with a star.
Keep the same design system as previous screens.
```

## 10. 쿠키 영상 + 마무리

```
Screen: post-credit cookie scene and finale.
- Center: big text "THE END" fading, then a playful card that says "잠깐! 쿠키 영상이 있어요 🍪".
- Below: a waveform-based audio moment card "차 안에서 — 준호의 노래 실력 (00:42)", with a large sound-wave visualization in coral and a play button.
- After that, final frame text "TO BE CONTINUED..." with a hint card "다음 여행의 오프닝으로 이어집니다".
- Bottom action row of three large pill buttons: "다시 보기", "휴대폰으로 보내기" (QR icon), "SNS 공유".
Keep the same design system as previous screens.
```

---

## 추가: 1:1 분할(세로형) 대응 확인용

```
Create a portrait half-screen version (1:1 split window, the app occupies a tall narrow window) of the "여행 보관함" home screen and the "상영 대기" screen. Stack the cards vertically, keep the same hierarchy and the same design system, and keep all touch targets at least 80px tall.
```

## 추가: 모바일 웹 (참여자용)

```
Design a mobile web companion (iPhone size, dark theme, same CARDIT design system: near-black background, frosted glass cards, cinema gold accent, Asta Sans/Pretendard) for trip participants:
1) Invite landing: movie-poster style card "민지님이 '부산 우정여행'에 캐스팅했어요" with "참여하기" button.
2) Upload screen: grid of photos with selection checkmarks, a big "사진·영상 올리기" button, and a highlight star toggle on each item.
3) Floating "쿠키 저장" button that saves the current moment, with a toast "지금 이 순간을 쿠키로 저장했어요".
```
