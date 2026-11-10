package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;

import java.util.Random;

public abstract class AbstractEnemyFactory {
    private static final int MAX_DIFFICULTY_LEVEL = 13;
    private static final double HP_INCREASE_PER_LEVEL = 0.04;
    private static final double HP_INCREASE_CAP = 0.50;
    private static final double SPEED_INCREASE_PER_LEVEL = 0.02;
    private static final double SPEED_INCREASE_CAP = 0.25;
    private static final int SPAWN_CHANCE_INCREASE_PER_LEVEL = 3;

    static final Random random = new Random();
    int probabilityForEnemySpawning;
    protected int speedX;
    protected int speedY;
    protected int hp;
    protected int defaultSpawningProbability;
    protected int probabilityIncrement;
    protected int currentDifficultyFactor = 1;
    protected int maxSpawnProbability = 50;
    private int baseHp;
    private int baseSpeedX;
    private int baseSpeedY;
    private int baseSpawnProbability;
    private int baseSpawningProbability;

    public abstract AbstractEnemyAircraft createEnemy();

    public AbstractEnemyAircraft createEnemyByProbability(int difficultyFactor) {
        checkDifficultyFactorAndUpdate(difficultyFactor);

        if (random.nextInt(1, 101) <= probabilityForEnemySpawning) {
            probabilityForEnemySpawning = defaultSpawningProbability;
            return createEnemy();
        }
        else {
            probabilityForEnemySpawning = probabilityForEnemySpawning >= 100 ?
                    probabilityForEnemySpawning : probabilityForEnemySpawning+probabilityIncrement;
            return null;
        }
    }

    protected void checkDifficultyFactorAndUpdate(int difficultyFactor) {
        int boundedLevel = Math.max(1, Math.min(MAX_DIFFICULTY_LEVEL, difficultyFactor));
        if (currentDifficultyFactor != boundedLevel) {
            currentDifficultyFactor = boundedLevel;
            updateDifficulty();
        }
    }

    protected final void rememberBaseStats() {
        baseHp = hp;
        baseSpeedX = speedX;
        baseSpeedY = speedY;
        baseSpawnProbability = defaultSpawningProbability;
        baseSpawningProbability = probabilityForEnemySpawning;
    }

    public final void resetForNewRun() {
        currentDifficultyFactor = 1;
        hp = baseHp;
        speedX = baseSpeedX;
        speedY = baseSpeedY;
        defaultSpawningProbability = baseSpawnProbability;
        probabilityForEnemySpawning = baseSpawningProbability;
    }

    protected final int scaleSpeedX(int baseSpeed) {
        return (int) Math.round(baseSpeed * speedMultiplier());
    }

    protected final int scaleSpeedY() {
        return scaleSpeedX(baseSpeedY);
    }

    private void updateDifficulty() {
        int levelSteps = currentDifficultyFactor - 1;
        double hpMultiplier = 1.0 + Math.min(HP_INCREASE_CAP, levelSteps * HP_INCREASE_PER_LEVEL);
        hp = Math.max(1, (int) Math.round(baseHp * hpMultiplier));
        speedX = scaleSpeedX(baseSpeedX);
        speedY = scaleSpeedY();

        if (baseSpawnProbability >= 100) {
            defaultSpawningProbability = 100;
        } else {
            defaultSpawningProbability = Math.min(maxSpawnProbability,
                    baseSpawnProbability + levelSteps * SPAWN_CHANCE_INCREASE_PER_LEVEL);
        }
        probabilityForEnemySpawning = Math.min(100,
                Math.max(probabilityForEnemySpawning, defaultSpawningProbability));
    }

    private double speedMultiplier() {
        int levelSteps = currentDifficultyFactor - 1;
        return 1.0 + Math.min(SPEED_INCREASE_CAP, levelSteps * SPEED_INCREASE_PER_LEVEL);
    }
}

