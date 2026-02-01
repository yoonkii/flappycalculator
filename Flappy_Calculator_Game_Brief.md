# GAME BRIEF
## Flappy Calculator
*A math-powered twist on Flappy Bird*

**Platform:** Android (Mobile)
**Genre:** Casual / Arcade / Puzzle
**Target:** All Ages
**Price:** Free
**Draft Date:** January 31, 2026

---

## 1. Concept Overview

Flappy Calculator is a casual mobile arcade game that fuses the side-scrolling obstacle-dodging gameplay of Flappy Bird with real-time mental arithmetic. Instead of tapping to flap, the player must solve a math problem and submit the correct answer at the right moment. A correct submission triggers a flap; an incorrect or missing answer means the bird keeps falling.

The result is a game that is instantly recognizable but meaningfully different: players must split their attention between solving a problem and reading the timing of approaching pipes. Because this dual-task is inherently harder than a simple tap, the game compensates with forgiving physics and generous level design.

---

## 2. One-Line Pitch

> *"Flappy Bird, but your brain is the button."*

---

## 3. Core Game Loop

The core loop consists of three tightly interlocked steps that repeat for the duration of a run:

1. **SEE:** A math problem is displayed on screen (e.g., "24 + 13 = ?"). The bird is continuously falling under gentle gravity.
2. **SOLVE:** The player types the answer on an on-screen numeric keypad. A wrong answer has no effect — the bird simply does not flap, and the player can try again.
3. **FLAP:** On correct answer submission, the bird flaps upward. A new problem immediately appears. The player must time their next correct answer to navigate the next gap.

The challenge is dual: solve the math quickly, but also submit at the right moment so the flap carries the bird through the pipe gap. Submitting too early or too late — even with the right answer — may cause a collision.

---

## 4. Math Problem System

### 4.1 Problem Types & Difficulty Tiers

Problems are organized into tiers that unlock progressively as the player's score increases. All problems are generated dynamically at runtime.

| Tier | Score Range | Operations | Example |
|------|-------------|------------|---------|
| 1 | 0 – 9 | Addition (1–2 digits) | 7 + 5 = ? |
| 2 | 10 – 24 | Addition & Subtraction (1–3 digits) | 132 – 47 = ? |
| 3 | 25 – 49 | + Multiplication (1d × 1d) | 8 × 6 = ? |
| 4 | 50+ | + Division & Multiplication (2d × 1d) | 36 ÷ 4 = ? / 14 × 7 = ? |

### 4.2 Problem Constraints

- All answers are positive whole numbers (no negatives, no fractions).
- Subtraction problems guarantee the first operand is larger.
- Division problems guarantee a clean integer result (no remainders).
- Consecutive problems avoid using the same operator twice in a row where possible.

### 4.3 Input Method

The initial release uses an on-screen numeric keypad (0–9, backspace, submit). The keypad occupies the bottom portion of the screen, similar to a calculator layout. This approach prioritizes skill expression and deliberate input over guessing.

*Alternative input (multiple choice) is flagged for post-launch evaluation via playtesting. If playtest data shows the keypad creates too much friction for casual players, a toggle or adaptive mode may be introduced.*

---

## 5. Physics & Difficulty Tuning

Because the cognitive cost per flap is significantly higher than a simple tap, Flappy Calculator uses softer physics and wider gaps compared to classic Flappy Bird.

| Parameter | Flappy Bird (Reference) | Flappy Calculator |
|-----------|------------------------|-------------------|
| Gravity | Steep / punishing | Gentle / floaty |
| Fall speed | Fast acceleration | Slow, capped terminal velocity |
| Flap impulse | Sharp upward burst | Smooth, moderate lift |
| Pipe gap size | Tight (~3.5x bird height) | Generous (~5–6x bird height) |
| Pipe spacing (horizontal) | Close together | Wider apart (more time to solve) |
| Scroll speed | Constant / fast | Starts slow, increases gradually |

The guiding principle is that a player who can solve basic addition in ~2 seconds should be able to survive the early game comfortably. Difficulty ramps through a combination of harder math, faster scrolling, and slightly tighter pipe gaps.

---

## 6. Wrong Answer & Failure

Flappy Calculator treats wrong answers leniently:

- **Wrong answer:** The input field clears and shakes briefly (visual feedback). The bird does not flap. The same problem remains on screen. The player can immediately retry.
- **No additional penalty:** No speed increase, no extra gravity, no gap shrinkage. The only consequence is lost time — the bird keeps falling while the player re-attempts.
- **Death:** Occurs only on collision with a pipe or the ground/ceiling, same as classic Flappy Bird.

---

## 7. Scoring

The scoring system is simple and transparent:

- +1 point for each pipe successfully passed.
- High score is stored locally and displayed on the game-over screen.
- Stretch goal: a "Math Accuracy" stat shown post-run (% of first-attempt correct answers).

---

## 8. UI & Screen Layout

### 8.1 Gameplay Screen

The screen is divided into two functional zones:

**Top ~60% — Game View:** The bird, pipes, background, score counter, and current math problem. The math problem is displayed prominently near the top-center so players can read it while watching the bird.

**Bottom ~40% — Keypad:** A compact numeric keypad (digits 0–9, backspace/clear, submit). Large touch targets for fast, error-free input. The answer-in-progress is shown just above the keypad.

### 8.2 Other Screens

- **Title Screen:** Game logo, "Tap to Start" prompt, high score display.
- **Game Over Screen:** Score, high score, math accuracy (stretch), "Retry" and "Menu" buttons.

---

## 9. Art Direction & Audio

The visual style should be clean, colorful, and playful — approachable for all ages without skewing too childish. Think flat/semi-flat 2D with bold outlines, bright background colors, and a charming bird character.

- **Bird:** Simple, expressive (reacts to correct/wrong answers with micro-animations).
- **Pipes:** Could be styled as pencils, rulers, or other math/school-themed objects.
- **Audio:** Satisfying "ding" on correct answer, soft buzz on wrong answer, upbeat background music, classic whoosh on flap.

---

## 10. Technical Notes

| Item | Detail |
|------|--------|
| Platform | Android (minimum SDK TBD, target latest) |
| Engine / Framework | TBD (Godot, Unity, or native Kotlin/Compose are all viable) |
| Orientation | Portrait only |
| Monetization | None at launch (free, no ads) |
| Persistence | Local high score only (SharedPreferences or equivalent) |

---

## 11. Open Questions & Playtest Priorities

The following items are deliberately deferred to playtesting. They should be evaluated during the first internal playtest round:

| # | Question | Notes |
|---|----------|-------|
| 1 | Keypad vs. multiple choice? | Keypad ships first. If playtest shows >60% of casual testers find it too hard, prototype a multiple-choice mode. |
| 2 | Optimal gravity & gap values? | Start generous and tighten based on data. Target: average player survives 5–10 pipes on first run. |
| 3 | Difficulty curve pacing? | When should multiplication/division appear? Current proposal: score 25+. Validate. |
| 4 | Problem visibility timing? | Should the next problem appear instantly after a correct answer or after a brief delay? Instant might overwhelm; delay might bore. |
| 5 | Bird fall speed sweet spot? | Needs to feel urgent enough to create tension but slow enough that solving a 2-second problem is viable. |

---

## 12. MVP Scope

The minimum viable version to validate the concept includes:

1. Functional bird with gentle gravity and flap mechanic triggered by correct answer submission.
2. Scrolling pipes with generous gaps.
3. Dynamic math problem generation (Tiers 1–2 at minimum).
4. On-screen numeric keypad with submit and clear buttons.
5. Score tracking (current run + local high score).
6. Game-over and retry flow.
7. Placeholder art and basic sound effects.

---

## 13. Stretch Goals (Post-MVP)

- Multiple-choice input mode as an alternative or accessibility option.
- Leaderboard (Google Play Games or similar).
- Unlockable bird skins earned via score milestones.
- "Daily Challenge" mode with a fixed problem sequence for competitive comparison.
- "Kids Mode" with only single-digit addition and extra-wide gaps.
- Share score as image to social media.
- Sound and music polish, screen-shake, particle effects.
