# Air Wars Based on HITSZ

JavaFX airplane shooter course project. The Gradle build lives in `AircraftWar/` and targets Java 21 with JavaFX 21.0.2.

## Optional Jevil soundtrack

The game uses `AircraftWar/app/src/main/resources/videos/jevil_world_revolving.wav` for Jevil's battle music when that local file is present. The track is excluded from Git; without it, the game falls back to the bundled boss music. The file can be made from `mus/joker.ogg` in a local DELTARUNE installation. Keep the installation itself read-only.

## Run

From the repository root on Windows:

```powershell
.\AircraftWar\gradlew.bat -p .\AircraftWar :app:run
```

The Gradle wrapper is included so contributors can use the project's configured Gradle version without installing Gradle separately. Local score records are saved under `AircraftWar/app/resources/records/` and are intentionally excluded from Git.
