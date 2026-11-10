# Repository guidance

## DELTARUNE installation files are read-only

Treat every file under `D:\steam\steamapps\common\DELTARUNE` as read-only. It may be inspected as a reference, but must never be edited, patched, overwritten, renamed, deleted, or used as a destination for generated or temporary files. Save all project code, assets, and generated output inside this repository. Any DELTARUNE-inspired encounter should be implemented in this project without changing the game installation.

## Project structure

- The Gradle project is in `AircraftWar/`.
- Keep source, resources, tests, Gradle wrapper files, and useful project documentation under version control.
- Keep build output, IDE state, and local score records out of commits; see `.gitignore`.
