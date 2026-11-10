package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;

import java.util.Random;

public abstract class AbstractEnemyFactory {
    /*
    工厂模式：把每种敌机的创建方法类给定义出来，在主程序中调用
    两种敌机实现
     */
    static Random random = new Random();
    int probabilityForEnemySpawning;
    protected int speedX;
    protected int speedY;
    protected int hp;
    protected int defaultSpawningProbability;
    protected int probabilityIncrement;
    protected double currentDifficultyFactor = 1;
    protected int maxSpawnProbability = 50;


    public abstract AbstractEnemyAircraft createEnemy() ;

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
        if(currentDifficultyFactor != difficultyFactor) {
            currentDifficultyFactor = difficultyFactor;
            updateDifficulty();
        }
    }

    private void updateDifficulty() {
        if (this.defaultSpawningProbability < maxSpawnProbability) {
            this.defaultSpawningProbability += 5;
        }
        this.hp *= (int) (1 + 1 / Math.pow(this.currentDifficultyFactor, 2)); // 无穷级数 (1 + 1/n^2)乘积收敛到约3.6
        this.speedX *= (int) (1 + Math.pow(this.hp, 2));

    }



}

