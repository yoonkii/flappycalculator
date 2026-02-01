# Flappy Calculator - Asset Requirements

## Wall Street Theme Edition

This document specifies all visual and audio assets needed for the game with a **Wall Street / Stock Exchange** inspired theme.

---

## Theme Overview

**Visual Style**: Modern financial district aesthetic
- **Bird**: Business analyst character (suit, tie, briefcase, or trading terminal)
- **Background**: New York City Wall Street skyline, stock tickers, financial district
- **Pipes/Obstacles**: Stock chart bars (red for down, blue/green for up)
- **Colors**: Professional blues, golds, with rded/green market colors

---

## Directory Structure

```
app/src/main/res/
├── drawable/                    # Vector drawables & XML
├── drawable-mdpi/              # 1x (baseline)
├── drawable-hdpi/              # 1.5x
├── drawable-xhdpi/             # 2x
├── drawable-xxhdpi/            # 3x (recommended primary)
├── drawable-xxxhdpi/           # 4x
├── font/                       # Custom fonts
└── raw/                        # Sound effects
```

Create assets at **xxhdpi (3x)** as the primary size.

---

## 1. Bird Sprites (Business Analyst Character)

### 1.1 Bird Animation Frames

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `bird_frame_1.png` | 72x72 px | Analyst with arms/wings up (bullish pose) |
| `bird_frame_2.png` | 72x72 px | Analyst neutral pose |
| `bird_frame_3.png` | 72x72 px | Analyst with arms/wings down |

**Character Design Requirements:**
- Small character wearing a business suit (navy blue or charcoal)
- Red or blue tie
- Optional: Small briefcase, trading terminal, or stock chart
- Face should show determination/focus
- Simple, clean pixel art style (16-32 colors)
- Transparent background (PNG with alpha)
- Character should face RIGHT
- Centered with ~4px padding

**Color Palette:**
- Suit: Navy blue (#1E3A5F, #2C5282)
- Shirt: White (#FFFFFF)
- Tie: Red (#E53E3E) or Gold (#D69E2E)
- Skin: Warm tones (#F6AD55, #ED8936)
- Hair: Dark (#2D3748)

### 1.2 Character States (Optional)

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `bird_bullish.png` | 72x72 px | Happy expression (market up!) |
| `bird_bearish.png` | 72x72 px | Worried expression (market down!) |

---

## 2. Pipe/Obstacle Sprites (Stock Chart Bars)

### 2.1 Stock Bar Obstacles

Design pipes as **vertical stock chart bars** like those seen on trading terminals.

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `pipe_red_body.png` | 96x32 px | Red bar body (down/bearish) - tileable |
| `pipe_red_cap.png` | 108x36 px | Red bar cap/top |
| `pipe_blue_body.png` | 96x32 px | Blue bar body (up/bullish) - tileable |
| `pipe_blue_cap.png` | 108x36 px | Blue bar cap/top |

**Alternative: Full Bar Images**

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `pipe_red_top.png` | 96x480 px | Full red bar (top obstacle) |
| `pipe_red_bottom.png` | 96x480 px | Full red bar (bottom obstacle) |
| `pipe_blue_top.png` | 96x480 px | Full blue bar (top obstacle) |
| `pipe_blue_bottom.png` | 96x480 px | Full blue bar (bottom obstacle) |

**Color Palette:**
- Red Bar (Bearish):
  - Main: #E53E3E
  - Highlight: #FC8181
  - Shadow: #C53030
- Blue Bar (Bullish):
  - Main: #3182CE
  - Highlight: #63B3ED
  - Shadow: #2B6CB0
- Green Bar (Alternative Bullish):
  - Main: #38A169
  - Highlight: #68D391
  - Shadow: #2F855A

**Design Notes:**
- Add subtle gradient for 3D effect
- Include small "candlestick" wick lines at ends (optional)
- Can add subtle grid lines like trading charts

---

## 3. Background Assets (Wall Street Skyline)

### 3.1 Main Background

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `background_wallstreet.png` | 1080x1920 px | NYC financial district skyline |

**Design Requirements:**
- Silhouette or simplified NYC skyline (Freedom Tower, skyscrapers)
- Dawn/dusk gradient sky (professional, not too dark)
- Optional: Stock ticker tape scrolling effect at top
- Should tile horizontally for parallax scrolling

**Color Palette:**
- Sky gradient: Dark blue (#1A365D) to lighter blue (#4A5568)
- Buildings: Dark silhouettes (#2D3748, #1A202C)
- Accent lights: Warm gold (#D69E2E) for windows
- Ticker: Green text on black (#48BB78 on #1A202C)

### 3.2 Parallax Layers (Optional)

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `bg_layer_far.png` | 1080x600 px | Distant skyscrapers (slowest scroll) |
| `bg_layer_mid.png` | 1080x800 px | Mid-distance buildings |
| `bg_layer_near.png` | 1080x400 px | Close buildings/street level |

### 3.3 Ground (Trading Floor)

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `ground.png` | 1080x60 px | Trading floor pattern - tileable |

**Design Requirements:**
- Dark marble or professional floor texture
- Can include subtle stock ticker/grid pattern
- Tileable horizontally

---

## 4. UI Elements

### 4.1 Stock Ticker Display (Score)

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `ticker_background.9.png` | 300x72 px | LED ticker board style |

**Design**: Dark background with digital/LED font showing score like a stock price

### 4.2 Math Problem Display

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `problem_background.9.png` | 480x120 px | Trading terminal screen style |

**Design**: Dark terminal with green/amber text, slight screen glow effect

### 4.3 Keypad Buttons (Optional Enhancement)

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `button_terminal.9.png` | 144x144 px | Trading terminal button style |
| `button_terminal_pressed.9.png` | 144x144 px | Pressed state |

---

## 5. Screen Backgrounds

### 5.1 Title Screen

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `title_logo.png` | 720x240 px | "Flappy Calculator" in financial font |
| `title_character.png` | 144x144 px | Large analyst character |

**Logo Style:**
- "Flappy" in gold/brass (#D69E2E)
- "Calculator" in white or silver
- Font: Bold serif (like financial newspapers)
- Optional: Stock chart line underneath

### 5.2 Game Over Screen

| File Name | Size (xxhdpi) | Description |
|-----------|---------------|-------------|
| `gameover_panel.9.png` | 600x720 px | Trading terminal crash screen |
| `icon_retry.png` | 72x72 px | Refresh/reload icon |
| `icon_menu.png` | 72x72 px | Home/menu icon |
| `icon_star.png` | 48x48 px | Star rating |
| `icon_star_gold.png` | 48x48 px | Gold star for high score |

---

## 6. Launcher Icons

### 6.1 App Icon

| File Name | Sizes Required |
|-----------|----------------|
| `ic_launcher.png` | 48/72/96/144/192 px |
| `ic_launcher_round.png` | Same sizes |
| `ic_launcher_foreground.png` | 432x432 px (xxxhdpi) |
| `ic_launcher_background.png` | 432x432 px or solid color |

**Design:**
- Foreground: Analyst character with chart/calculator
- Background: Navy blue (#1E3A5F) or gold (#D69E2E)

---

## 7. Sound Effects

Location: `app/src/main/res/raw/`

| File Name | Duration | Description |
|-----------|----------|-------------|
| `flap.ogg` | ~100ms | Whoosh or paper shuffle sound |
| `correct.ogg` | ~200ms | Cash register "cha-ching" or success chime |
| `wrong.ogg` | ~200ms | Buzzer or "market down" sound |
| `score.ogg` | ~150ms | Coin sound or stock ticker beep |
| `hit.ogg` | ~300ms | Market crash/bell sound |
| `keypress.ogg` | ~50ms | Trading terminal key click |
| `gameover.ogg` | ~500ms | Market closing bell |

**Audio Requirements:**
- Format: OGG (preferred) or WAV
- Sample rate: 44100 Hz
- Bit depth: 16-bit
- Channels: Mono
- Keep files small (< 50KB each)

---

## 8. Fonts

Location: `app/src/main/res/font/`

| File Name | Description |
|-----------|-------------|
| `trading_font.ttf` | Main game font (LED/digital or financial style) |

**Font Suggestions:**
- Digital-7 (LED display style)
- Roboto Mono (clean terminal look)
- IBM Plex Mono (professional)
- Source Code Pro (readable)

---

## 9. Asset Checklist

### Minimum Viable (Required)
- [ ] `bird_frame_1.png` - Analyst character (72x72)
- [ ] `bird_frame_2.png` - Analyst alt frame (72x72)
- [ ] `pipe_red_top.png` - Red stock bar (96x480)
- [ ] `pipe_red_bottom.png` - Red stock bar (96x480)
- [ ] `pipe_blue_top.png` - Blue stock bar (96x480)
- [ ] `pipe_blue_bottom.png` - Blue stock bar (96x480)
- [ ] `ground.png` - Trading floor (1080x60)
- [ ] Launcher icons (all sizes)

### Recommended
- [ ] `bird_frame_3.png` - Third animation frame
- [ ] `background_wallstreet.png` - NYC skyline (1080x1920)
- [ ] `title_logo.png` - Game logo (720x240)
- [ ] `trading_font.ttf` - Custom font
- [ ] All sound effects

### Nice to Have
- [ ] Parallax background layers
- [ ] Custom terminal-style UI buttons
- [ ] Character expression variants
- [ ] Stock ticker animation overlay

---

## 10. Color Reference

### Primary Palette

| Element | Hex | Usage |
|---------|-----|-------|
| Navy Blue | #1E3A5F | Suit, background |
| Gold | #D69E2E | Accents, logo |
| White | #FFFFFF | Shirt, text |
| Dark Gray | #2D3748 | Buildings, shadows |

### Market Colors

| Element | Hex | Usage |
|---------|-----|-------|
| Bull Green | #38A169 | Positive, up |
| Bear Red | #E53E3E | Negative, down |
| Bull Blue | #3182CE | Alternative positive |

### Terminal Colors

| Element | Hex | Usage |
|---------|-----|-------|
| Terminal Black | #1A202C | Backgrounds |
| Terminal Green | #48BB78 | Text, tickers |
| Terminal Amber | #ED8936 | Highlights |

---

## 11. Image Generation Prompts

Use these prompts with image generation tools (Gemini, DALL-E, Midjourney, etc.):

### Bird/Character
```
Pixel art style, 72x72 pixels, small business analyst character wearing navy blue suit and red tie, simple cute design, facing right, transparent background, game sprite, 16-bit style
```

### Background
```
Pixel art New York City Wall Street financial district skyline at dusk, dark blue gradient sky, silhouette skyscrapers, warm window lights, stock ticker tape at top, game background, 1080x1920, retro 16-bit style
```

### Stock Bar Pipes
```
Pixel art vertical stock chart bar, red candlestick bar, 3D effect with gradient, clean edges, game obstacle sprite, 96x480 pixels, transparent background, 16-bit style
```

---

## 12. Implementation Notes

To use red/blue bars randomly or based on market conditions:

```kotlin
// In PipeSpawner or GameEngine
val isUpMarket = Random.nextBoolean()
val pipeDrawable = if (isUpMarket) R.drawable.pipe_blue else R.drawable.pipe_red
```

To animate stock ticker in background:
```kotlin
// Use infinite horizontal scroll animation
val tickerOffset by rememberInfiniteTransition().animateFloat(
    initialValue = 0f,
    targetValue = -tickerWidth,
    animationSpec = infiniteRepeatable(
        animation = tween(10000, easing = LinearEasing)
    )
)
```

---

## Quick Start

Create these 4 files first:

1. **`bird_frame_1.png`** (72x72) - Analyst in suit facing right
2. **`pipe_red_body.png`** (96x32) - Red stock bar, tileable
3. **`pipe_blue_body.png`** (96x32) - Blue stock bar, tileable
4. **`ground.png`** (1080x60) - Dark trading floor, tileable

Place in `app/src/main/res/drawable-xxhdpi/` and update the renderers to use them.
