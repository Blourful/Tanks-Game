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

### Tank Movement and Turns

- Players take turns. Only the highlighted tank can move, aim, change power,
  or fire.
- Arrow keys move the tank and use fuel. A tank stops moving when it reaches the
  edge of the map or runs out of fuel.
- A tank cannot be controlled while it is falling.
- Pressing `Space` fires and immediately passes the turn to the next tank.
- Each tank begins with full health, a limited amount of fuel, and medium firing
  power. Taking damage also limits how much firing power that tank can use.

### Aiming, Wind, and Projectiles

- Use the arrow keys to aim higher or lower, and use `W` or `S` to change the
  firing power.
- A higher firing power sends the projectile farther, but it may overshoot a
  nearby target.
- Projectiles follow a curved path rather than travelling in a straight line.
- Wind changes after every shot and pushes projectiles sideways. Check the wind
  indicator before firing and adjust the angle or power when necessary.
- A projectile explodes when it hits the terrain. The explosion also digs a hole
  in the terrain, so the battlefield changes throughout the game.

### Explosions and Damage

- An explosion is strongest at its centre and becomes weaker farther away.
- Every tank close to the explosion can be damaged, including the tank that
  fired the projectile.
- A direct hit can remove a large amount of health. A tank with no health left
  is eliminated.
- An explosion can also knock a nearby tank off the terrain. This can happen even
  when the tank takes little or no direct damage.

### Parachutes and Falling

- Every tank starts with 3 parachutes, and unused parachutes carry over to the
  next level.
- When an explosion knocks a tank off the terrain, it starts falling and cannot
  be controlled.
- With a parachute, the tank falls slowly and does not lose health from falling.
- Without a parachute, the tank falls faster and continuously loses health.
- A parachute is used when a falling tank is eliminated. Falling off the bottom
  of the screen also eliminates the tank.
- A falling tank can still be removed by losing all of its health.

### Elimination and Level Progression

- A tank is eliminated when its health reaches zero or when it falls off the
  bottom of the screen.
- Eliminated tanks leave the turn order and create a final explosion. A tank
  that falls creates a larger explosion than a tank destroyed on the ground.
- This final explosion can damage other tanks and change the terrain.
- When only one tank remains, the current level ends and the next level begins.
- After the last level, the game displays the final scores.

### Scoring and Recovery

- Players earn score by damaging opponents.
- Damage to your own tank does not give you score.
- Knocking an opponent off the terrain can also award score, especially when the
  opponent is falling without a parachute.
- Press `R` to spend 20 score and recover 20 health, up to full health.
- Press `F` to spend 10 score and restore 200 fuel.
- On the final game-over screen, pressing `R` starts the game again instead of
  repairing the tank.

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
