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
