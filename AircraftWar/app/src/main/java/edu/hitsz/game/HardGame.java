package edu.hitsz.game;

import edu.hitsz.application.ImageManager;
import edu.hitsz.enums.DifficultyEnums;

/**
 * 困难难度
 * - 有Boss（每次召唤血量递增）
 * - 难度递进：敌机生成周期、敌机速度、英雄射击周期（变慢）
 */
public class HardGame extends AbstractGame {

    // 难度递进参数
    private double originalEnemySpawnCycle;
    private double originalHeroShootCycle;

    private static final double MIN_SPAWN_CYCLE = 6;
    private static final double MAX_HERO_SHOOT_CYCLE = 3;
    private static final int PROGRESSION_SCORE_INTERVAL = 1000;

    @Override
    protected void initDifficultyParams() {
        this.enemyMaxNumber = 8;
        this.enemySpawnCycle = 18;
        this.originalEnemySpawnCycle = 18;
        this.originalHeroShootCycle = 2;
        this.heroShootCycle = originalHeroShootCycle;
        this.bossSpawnScore = 15000;
        this.allowBoss = true;
        this.difficulty = DifficultyEnums.HARD;
        this.backGroundImage = ImageManager.HARD_BACKGROUND_IMAGE;
    }

    @Override
    protected void updateDifficultyProgression() {
        int newLevel = Math.max(1, score / PROGRESSION_SCORE_INTERVAL + 1);

        if (newLevel > difficultyFactor) {
            difficultyFactor = newLevel;

            int progressionSteps = difficultyFactor - 1;
            double newSpawnCycle = originalEnemySpawnCycle - progressionSteps * 1.5;
            if (newSpawnCycle < MIN_SPAWN_CYCLE) {
                newSpawnCycle = MIN_SPAWN_CYCLE;
            }
            enemySpawnCycle = newSpawnCycle;

            double newHeroCycle = originalHeroShootCycle + progressionSteps * 0.2;
            if (newHeroCycle > MAX_HERO_SHOOT_CYCLE) {
                newHeroCycle = MAX_HERO_SHOOT_CYCLE;
            }
            heroShootCycle = newHeroCycle;
            System.out.println("[难度递进] 困难模式 - 分数: " + score +
                    ", 等级: " + difficultyFactor +
                    ", 敌机生成周期: " + String.format("%.1f", enemySpawnCycle));
        }
    }
}
