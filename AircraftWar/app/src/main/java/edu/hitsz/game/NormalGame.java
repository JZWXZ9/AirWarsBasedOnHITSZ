package edu.hitsz.game;

import edu.hitsz.application.ImageManager;
import edu.hitsz.enums.DifficultyEnums;

/**
 * 普通难度
 * - 有Boss（血量不变）
 * - 难度递进：敌机生成周期变短、敌机速度增加
 */
public class NormalGame extends AbstractGame {

    // 难度递进参数
    private double originalEnemySpawnCycle;
    private static final double MIN_SPAWN_CYCLE = 10;
    private static final int PROGRESSION_SCORE_INTERVAL = 2000;

    @Override
    protected void initDifficultyParams() {
        this.enemyMaxNumber = 6;
        this.enemySpawnCycle = 22;
        this.originalEnemySpawnCycle = 22;
        this.heroShootCycle = 2.0;
        this.bossSpawnScore = 24000;
        this.allowBoss = true;
        this.difficulty = DifficultyEnums.NORMAL;
        this.backGroundImage = ImageManager.NORMAL_BACKGROUND_IMAGE;
    }

    @Override
    protected void updateDifficultyProgression() {
        int newLevel = Math.max(1, score / PROGRESSION_SCORE_INTERVAL + 1);

        if (newLevel > difficultyFactor) {
            difficultyFactor = newLevel;

            // 敌机生成周期递进（周期越小，生成越快）
            int progressionSteps = difficultyFactor - 1;
            double newCycle = originalEnemySpawnCycle - progressionSteps * 1.2;
            if (newCycle < MIN_SPAWN_CYCLE) {
                newCycle = MIN_SPAWN_CYCLE;
            }
            enemySpawnCycle = newCycle;

            System.out.println("[难度递进] 普通模式 - 分数: " + score +
                    ", 等级: " + difficultyFactor +
                    ", 敌机生成周期: " + String.format("%.1f", enemySpawnCycle));
        }
    }
}
