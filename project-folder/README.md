# Jetpack Joyride Java Remake

A Java/Swing remake of Jetpack Joyride, refreshed into a portfolio-ready project with testing, Gradle packaging, procedural generation, and richer game feedback.

## Highlights

- 8 handcrafted campaign levels loaded from text files (`Final1` ... `Final8`)
- Endless procedural levels after campaign completion (level 9+)
- Difficulty presets (`Easy`, `Normal`, `Hard`) affecting:
  - Starting lives
  - Missile speed
  - Procedural obstacle density
- Obstacle variety:
  - Normal barriers
  - Electric barriers
  - Rotating barriers
  - Laser barriers
- Missile variety:
  - Non-tracking
  - Tracking
  - Sin-wave
  - Random movement
- Sprite-style animation polish:
  - Animated hero tilt + bobbing
  - Thruster flame animation while flying
  - Pulsing coin animation
- Sound effects (synthesized, no external sound files required):
  - Coin pickup
  - Damage
  - Level transition
  - Pause toggle
  - Game over
- Score + persistent best score (`best_score.txt`)
- Unit tests for parser and score logic

## Controls

- `UP ARROW` - Fly up
- `R` - Restart from level 1
- `P` - Pause / resume
- `1` - Switch to Easy (restarts run)
- `2` - Switch to Normal (restarts run)
- `3` - Switch to Hard (restarts run)
- `u` - Debug: next level
- `d` - Debug: previous level

## Project Structure

- `src/mainApp/MainApp.java` - Entry point
- `src/mainApp/GameMenu.java` - Menu and difficulty selection
- `src/mainApp/GameComponent.java` - Main game state, loop, rendering
- `src/mainApp/Hero.java` - Player movement, collision, animation
- `src/mainApp/LevelParser.java` - Validated level parsing
- `src/mainApp/LevelConstructor.java` - Campaign + procedural level loading
- `src/mainApp/ProceduralLevelGenerator.java` - Deterministic procedural level builder
- `src/mainApp/ScoreManager.java` - Score calculation and persistence
- `src/mainApp/SoundManager.java` - Runtime sound effects
- `src/test/java/mainApp/*` - JUnit tests

## Build, Test, Run (Gradle)

From project root:

```powershell
gradle clean test build
gradle run
```

Create distribution archives:

```powershell
gradle distZip
```

Artifacts are generated under `build/`.

## CI

GitHub Actions workflow is included at `.github/workflows/ci.yml`.
It runs:

- `gradle clean test build`

on push and pull requests.

## Notes

- Local environment in this refresh did not have Gradle preinstalled, so Gradle tasks were configured but not executed locally.
- Java source still lives in `src/mainApp` for continuity with the original project; Gradle source sets are configured to support this layout.
