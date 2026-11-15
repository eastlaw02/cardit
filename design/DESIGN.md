---
name: Cinematic Cockpit
colors:
  surface: '#111318'
  surface-dim: '#111318'
  surface-bright: '#37393f'
  surface-container-lowest: '#0c0e13'
  surface-container-low: '#1a1b21'
  surface-container: '#1e1f25'
  surface-container-high: '#282a2f'
  surface-container-highest: '#33353a'
  on-surface: '#e2e2e9'
  on-surface-variant: '#d1c5ac'
  inverse-surface: '#e2e2e9'
  inverse-on-surface: '#2e3036'
  outline: '#9a9078'
  outline-variant: '#4e4633'
  surface-tint: '#f0c110'
  primary: '#ffe5a0'
  on-primary: '#3d2f00'
  primary-container: '#f5c518'
  on-primary-container: '#695200'
  inverse-primary: '#745b00'
  secondary: '#c8bfff'
  on-secondary: '#2e158c'
  secondary-container: '#4533a3'
  on-secondary-container: '#b6abff'
  tertiary: '#ffe0d6'
  on-tertiary: '#5a1b00'
  tertiary-container: '#ffbba2'
  on-tertiary-container: '#93390f'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffe08b'
  primary-fixed-dim: '#f0c110'
  on-primary-fixed: '#241a00'
  on-primary-fixed-variant: '#584400'
  secondary-fixed: '#e5deff'
  secondary-fixed-dim: '#c8bfff'
  on-secondary-fixed: '#190064'
  on-secondary-fixed-variant: '#4533a3'
  tertiary-fixed: '#ffdbce'
  tertiary-fixed-dim: '#ffb599'
  on-tertiary-fixed: '#370e00'
  on-tertiary-fixed-variant: '#7f2b01'
  background: '#111318'
  on-background: '#e2e2e9'
  surface-variant: '#33353a'
typography:
  display-lg:
    fontFamily: Space Grotesk
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  display-md:
    fontFamily: Space Grotesk
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.01em
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 26px
    fontWeight: '600'
    lineHeight: 34px
  headline-sm:
    fontFamily: Space Grotesk
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  body-xl:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '500'
    lineHeight: 28px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 26px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-lg:
    fontFamily: Space Grotesk
    fontSize: 18px
    fontWeight: '700'
    lineHeight: 24px
    letterSpacing: 0.04em
  label-md:
    fontFamily: Space Grotesk
    fontSize: 15px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.06em
  label-sm:
    fontFamily: Space Grotesk
    fontSize: 13px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.08em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1.5rem
  gutter-sm: 1rem
  margin: 2.5rem
  margin-sm: 1.5rem
  space-xs: 0.5rem
  space-sm: 0.75rem
  space-md: 1.25rem
  space-lg: 2rem
  space-xl: 3rem
---

## Brand & Style

This design system delivers an in-vehicle cinematic discovery experience tailored for Android Automotive OS (AAOS). It synthesizes film festival prestige, golden-era screening credits, and automotive-grade ergonomic rigor. The interface bridges the luxury of high-end entertainment curation with the uncompromising demands of vehicular interaction—optimizing glanceability, minimizing cognitive load, and prioritizing driver safety while stationary or passenger browsing.

The visual style blends **Glassmorphic Bento Grid** architecture with **Modern Editorial Cinema**. Translucent smoked-glass panels rest against a deep atmospheric void, accented by rich cinema gold and expressive genre-coded bursts. Interactive regions feel physical, commanding, and luminous, mimicking tactile high-end projection equipment and pristine theater marquees.

### Core Tenets
- **Glanceable Immersion:** Information hierarchy is stark, direct, and readable from a 0.8-second glance at an arm's reach.
- **Festival Prestige:** Film festival typography, curated badges, and subtle projection glow replace generic media dashboard cliches.
- **In-Cabin Tactility:** Massive touch zones, bold focus states for physical rotary dials or steering controls, and high physical predictability under varying cabin lighting conditions.

## Colors

The palette simulates an intimate, darkened private screening room. The visual bedrock is a near-black, deep-navy tone (`#0B0D12`) designed to absorb ambient glare, conserve OLED battery, and blend into vehicle dashboard bezels.

### Palette Architecture
- **Canvas Base (`#0B0D12`):** Ground foundation for zero-distraction in-cabin operation.
- **Cinema Gold (`#F5C518`):** The primary focal accent. Reserved for premiere status, active selections, master media controls, and curated laurels.
- **Emotional Lavender (`#9B8CFF`):** Secondary genre accent for drama, arthouse, and reflective cinema narratives.
- **Comedy Amber-Coral (`#FF8A5B`):** Tertiary genre accent used for animation, lighthearted curation, and variety entertainment.
- **Action Crimson (`#FF3B3B`):** Auxiliary genre alert used strictly for thrillers, blockbuster action, and urgent live event indicators.
- **Text & Contrast Tiers:**
  - High-Contrast Primary: `#FFFFFF` (100% white for titles, runtime, critical glance data).
  - Ambient Secondary: `rgba(255, 255, 255, 0.60)` for metadata, cast credits, and subtitles.
  - De-emphasized Tertiary: `rgba(255, 255, 255, 0.38)` for inactive states and non-interactive borders.

## Typography

The type scale addresses the physical separation between passenger-seat eyelines and the central console. Typesetting avoids ultra-thin or hairline weights to prevent vibrations from degrading legibility.

- **Headline System (`Space Grotesk`):** Technical, authoritative, and cinematic. Delivers a movie-reel, archival edge with geometric clarity. Applied in uppercase to labels, laurels, genre headers, and hero feature titles.
- **Body & Metadata System (`Plus Jakarta Sans`):** Exceptional legibility in multi-script Asian and Latin environments (matching Pretendard proportions). Clean counters and open apertures make synopses and credit lists effortless to parse while on the road.
- **Letter Spacing:** All sub-labels and metadata tags employ deliberate positive letter spacing (+0.04em to +0.08em) evoking 35mm film billing blocks.

## Layout & Spacing

The system utilizes an asymmetrical **Bento Grid** layout model optimized for automotive horizontal displays (ranging from 12-inch landscape consoles to 15.6-inch pillar-to-pillar displays).

### Grid Philosophy
- **Horizontal Landscape Bias:** 8-column or 12-column dynamic grid with fixed column heights to avoid vertical endless scroll traps that distract drivers.
- **Target Reachability:** Critical interactive drivers (Playback controls, Volume, Quick Filters) cluster within the closest 40% of the screen nearest the steering wheel. Passenger-specific browsing spans the outer 60%.
- **Glance Rhythms:** Minimum gap between interactive elements is set to `space-md` (20px / 1.25rem) to completely eliminate mis-taps during road vibration.

## Elevation & Depth

Visual hierarchy does not rely on traditional dropped drop-shadows, which wash out in bright daylight driving scenarios. Instead, depth is structured through **luminous smoked glass and selective edge-lighting**.

### Tiered Translucency Levels
1. **Base Cavity (`#0B0D12`):** Direct vehicular backdrop. Deep, void-like, non-interactive.
2. **Bento Tile Surface:** `rgba(255, 255, 255, 0.08)` fill layered over a `backdrop-filter: blur(32px)`. Enclosed by a crisp `1px solid rgba(255, 255, 255, 0.12)` boundary outline.
3. **Elevated / Interactive Glass:** `rgba(255, 255, 255, 0.14)` fill with an inner edge specular highlight `1px inset rgba(255, 255, 255, 0.25)`.
4. **Focused / Cinema Marquee Tier:** `rgba(245, 197, 24, 0.12)` halo fill framed by a solid `2px #F5C518` illuminated perimeter with an external ambient gold bleed (`0 0 24px rgba(245, 197, 24, 0.25)`).

## Shapes

The geometric signature is defined by pronounced, architectural corner radiuses tailored to automotive hardware cutouts.

- **Bento Tiles:** Custom 28px continuous curvature (`rounded-2xl` equivalent) for primary content tiles and movie cards.
- **Hero & Modal Sheets:** 32px roundedness to echo modern vehicle cockpit bezels.
- **Interactive Controls (Buttons, Inputs):** Continuous squircle rounding (16px to 24px) for rapid tactile finger landing.
- **Genre Pills & Meta Chips:** Fully pill-shaped (9999px) for clear differentiation between content groupings and actionable items.

## Components

### 1. Primary Action & Media Buttons
- **Touch Target:** Minimum height and width of 80px for primary playback and driver-side actions. Secondary actions never drop below 64px.
- **Visuals:** Cinema Gold (`#F5C518`) background with solid `#0B0D12` bold iconography. Pressed state triggers a downscale to 96% with internal warm glow.

### 2. Frosted Bento Cards
- **Construction:** 28px corner radius, `rgba(255, 255, 255, 0.08)` background, 32px backdrop blur, 1px perimeter border (`rgba(255, 255, 255, 0.12)`).
- **Metadata Layout:** Poster art occupies the left or top zone; right zone houses full-contrast title, release laurel badges, rating, and runtime in 60% white text.

### 3. Genre Accent Tags & Laurels
- **Format:** Pill-shaped badges (36px height) with 12px horizontal padding.
- **Palette Coding:**
  - Comedy: Border and text `#FF8A5B`, surface `rgba(255, 138, 91, 0.15)`.
  - Action: Border and text `#FF3B3B`, surface `rgba(255, 59, 59, 0.15)`.
  - Arthouse / Emotional: Border and text `#9B8CFF`, surface `rgba(155, 140, 255, 0.15)`.

### 4. Rotary & Steering Wheel Focus States
- Visual outline: 3px solid `#F5C518` offset by 4px from the element edge.
- Subtle outer bloom ensures the driver identifies selected items purely through peripheral vision.

### 5. Media Scrubber & Vehicle Sound Mode Selector
- Track height of 12px with a 32px circular thumb for effortless finger adjustment during passenger transit.
- Active elapsed bar illuminated in Cinema Gold `#F5C518`.