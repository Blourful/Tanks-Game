# Tanks

A multiplayer, turn-based tank game built with Java and Processing. Players control
tanks, adjust their firing angle and power, and attack opponents across different
terrains and wind conditions.

## Features

- Multiplayer, turn-based tank battles
- Three configurable levels
- Different terrain, background, and tree assets
- Wind that affects projectile trajectories
- Tank movement, projectile firing, and explosions
- Health, fuel, and score systems
- Score-based health and fuel recovery
- Background music and success sound effects

## Tech Stack

- Java
- Gradle
- Processing 3.3.7
- JUnit 5

## Requirements

- JDK 8 or later
- Gradle 5.6 or later
- An operating system with graphical interface support

This project currently does not include the Gradle Wrapper, so Gradle must be
installed locally.

## Running the Game

Run the following command from the project root:

```bash
gradle run
```

The application must be started from the project root so that it can find
`config.json` and the assets in `src/main/resources/Tanks/`.

You can also run the `Tanks.App` main class from an IDE.

For the Windows ZIP release, open a terminal in the extracted folder and run:

```bash
java -jar Tanks-1.0.jar
```

## Controls

| Key | Action |
| --- | --- |
| `Left Arrow` / `Right Arrow` | Move the current tank and consume fuel |
| `Up Arrow` / `Down Arrow` | Adjust the firing angle |
| `W` / `S` | Increase or decrease firing power |
| `Space` | Fire a projectile and switch to the next player |
| `Q` | Switch to the next level |
| `R` | Spend score to restore health; restart after game over |
| `F` | Spend score to refuel |

## Game Mechanics

### Tank Attributes

- Every tank starts with 100 health, 250 fuel, and 50 firing power.
- Firing power is limited by the tank's current health. When a tank is damaged,
	its maximum available firing power is reduced accordingly.
- The firing angle is limited to 0 to 180 degrees.
- Holding `Left Arrow` or `Right Arrow` moves the tank by 2 pixels per frame
	and consumes 2 fuel per frame. Movement stops when the tank reaches the edge
	of the screen or has no fuel remaining.
- Tanks cannot be controlled while they are dropping.

### Turns and Firing

- Only the current tank can move, aim, adjust power, or fire.
- Pressing `Space` creates a projectile and immediately advances the turn to the
	next remaining tank.
- After each shot, the wind changes by a random amount between -5 and +5.
- The projectile's initial speed is calculated from firing power as:

	```text
	speed = firingPower / 100 * 16 + 2
	```

- Projectile motion uses gravity of 0.24 pixels per frame squared.
- Wind changes horizontal velocity by `wind * 0.03` each frame. Aiming and
	power selection are therefore both important for long-distance shots.

### Explosions and Damage

- A projectile explodes when it reaches the terrain surface.
- Each projectile destroys terrain within a 30-pixel radius.
- Tanks inside the same 30-pixel explosion radius take distance-based damage:
	- 60 damage at the explosion centre;
	- damage decreases linearly with distance;
	- damage reaches 0 at the edge of the radius.
- The damage is applied to every tank in the radius, including the firing tank
	if it is caught by its own explosion.
- A tank with health reduced to 0 is immediately eligible for elimination.
- A tank can also be knocked into a drop when an explosion occurs horizontally
	within 30 pixels of it, even if the tank is not directly damaged by the
	explosion.

### Parachutes and Falling

- Each tank starts with 3 parachutes. Remaining parachutes carry over between
	levels.
- When an explosion knocks a tank from the terrain, the tank enters a dropping
	state and can no longer be controlled.
- A tank with a parachute descends at 2 pixels per frame and does not lose health
	from the fall.
- A tank without a parachute descends at 4 pixels per frame and loses 4 health
	per frame while dropping.
- One parachute is consumed when a dropping tank is eliminated, either by
	reaching the bottom of the screen or by losing all health during the drop.
- Dropping tanks can still be affected by the game's elimination and explosion
	rules.

### Elimination and Level Progression

- A tank is eliminated when its health is 0 or below, or when its vertical
	position passes the bottom of the screen at 640 pixels.
- An eliminated tank is removed from the active turn order.
- A tank eliminated by health loss creates a smaller 15-pixel explosion.
- A tank that falls off the screen creates a larger 30-pixel explosion.
- The explosion caused by an eliminated tank can damage other tanks and modify
	the terrain.
- A level ends when only one tank remains. The game then loads the next level.
- The final game ends after the last configured level, where the final scores
	are displayed.

### Scoring and Recovery

- Damage dealt to another tank is added to the attacker's score.
- Damage to the firing tank does not award score.
- Causing a tank to drop without a parachute awards score while the tank loses
	health; knocking it completely off the screen awards the remaining health as
	score.
- Pressing `R` spends 20 score to restore 20 health, up to the 100-health
	maximum. On the final game-over screen, `R` restarts the game instead.
- Pressing `F` spends 10 score to restore 200 fuel.

## Testing

Run the unit tests:

```bash
gradle test
```

Generate the test coverage report:

```bash
gradle test jacocoTestReport
```

Test reports are generated under `build/reports/tests/test/index.html`, while
the coverage report is generated under
`build/reports/jacoco/test/html/index.html`.

## Project Structure

```text
.
├── build.gradle                       # Gradle build configuration
├── config.json                        # Level and player color configuration
├── level1.txt                         # Level layouts
├── level2.txt
├── level3.txt
├── level4.txt
├── src
│   ├── main
│   │   ├── java/Tanks                # Game source code
│   │   └── resources/Tanks            # Image and audio assets
│   └── test/java/Tanks                # Unit tests
└── README.md
```

## Level Configuration

Levels and player colors are configured in `config.json`:

- `layout`: Level layout file
- `background`: Background image
- `foreground-colour`: Terrain color
- `trees`: Tree image
- `player_colours`: Player tank colors

Image and audio assets are stored in `src/main/resources/Tanks/`.
