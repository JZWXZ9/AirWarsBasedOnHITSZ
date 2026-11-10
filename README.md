# Air Wars Based on HITSZ

JavaFX airplane shooter course project. The Gradle build lives in `AircraftWar/` and targets Java 21 with JavaFX 21.0.2.

## Run

From the repository root on Windows:

```powershell
.\AircraftWar\gradlew.bat -p .\AircraftWar :app:run
```

The Gradle wrapper is included so contributors can use the project's configured Gradle version without installing Gradle separately. Local score records are saved under `AircraftWar/app/resources/records/` and are intentionally excluded from Git.
