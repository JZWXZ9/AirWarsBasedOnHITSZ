package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.enums.DifficultyEnums;

import java.util.Random;

public abstract class AbstractEnemyFactory {
    private static final int MAX_DIFFICULTY_LEVEL = 13;
    private static final double HP_INCREASE_PER_LEVEL = 0.04;
    private static final double HP_INCREASE_CAP = 0.50;
    private static final double SPEED_INCREASE_PER_LEVEL = 0.02;
    private static final double SPEED_INCREASE_CAP = 0.25;
    private static final int SPAWN_CHANCE_INCREASE_PER_LEVEL = 3;
    private static final DifficultyEnums DEFAULT_DIFFICULTY = DifficultyEnums.NORMAL;

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
    private int hardBaseHp = -1;
    private double hardHpIncreasePerLevel = HP_INCREASE_PER_LEVEL;
    private double hardHpIncreaseCap = HP_INCREASE_CAP;
    private DifficultyEnums currentDifficulty = DEFAULT_DIFFICULTY;
    private int baseSpeedX;
    private int baseSpeedY;
    private int baseSpawnProbability;
    private int baseSpawningProbability;

    public abstract AbstractEnemyAircraft createEnemy();

    public AbstractEnemyAircraft createEnemyByProbability(int difficultyFactor) {
        checkDifficultyFactorAndUpdate(difficultyFactor);
        return createByCurrentProbability();
    }

    public AbstractEnemyAircraft createEnemyByProbability(int difficultyFactor, DifficultyEnums difficulty) {
        checkDifficultyFactorAndUpdate(difficultyFactor, difficulty);
        return createByCurrentProbability();
    }

    private AbstractEnemyAircraft createByCurrentProbability() {
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
        updateDifficultyState(difficultyFactor, currentDifficulty);
    }

    protected void checkDifficultyFactorAndUpdate(int difficultyFactor, DifficultyEnums difficulty) {
        updateDifficultyState(difficultyFactor, difficulty);
    }

    private void updateDifficultyState(int difficultyFactor, DifficultyEnums difficulty) {
        int boundedLevel = Math.max(1, Math.min(MAX_DIFFICULTY_LEVEL, difficultyFactor));
        DifficultyEnums requestedDifficulty = difficulty == null ? DEFAULT_DIFFICULTY : difficulty;
        if (currentDifficultyFactor != boundedLevel || currentDifficulty != requestedDifficulty) {
            currentDifficultyFactor = boundedLevel;
            currentDifficulty = requestedDifficulty;
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

    protected final void configureHardHpScaling(double increasePerLevel, double increaseCap) {
        hardHpIncreasePerLevel = increasePerLevel;
        hardHpIncreaseCap = increaseCap;
    }

    protected final void configureHardBaseHp(int hardBaseHp) {
        this.hardBaseHp = hardBaseHp;
    }

    public final void resetForNewRun() {
        currentDifficultyFactor = 1;
        currentDifficulty = DEFAULT_DIFFICULTY;
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
        double hpIncreasePerLevel = currentDifficulty == DifficultyEnums.HARD ?
                hardHpIncreasePerLevel : HP_INCREASE_PER_LEVEL;
        double hpIncreaseCap = currentDifficulty == DifficultyEnums.HARD ?
                hardHpIncreaseCap : HP_INCREASE_CAP;
        double hpMultiplier = 1.0 + Math.min(hpIncreaseCap, levelSteps * hpIncreasePerLevel);
        int difficultyBaseHp = currentDifficulty == DifficultyEnums.HARD && hardBaseHp > 0 ?
                hardBaseHp : baseHp;
        hp = Math.max(1, (int) Math.round(difficultyBaseHp * hpMultiplier));
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

