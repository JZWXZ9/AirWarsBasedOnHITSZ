package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteMobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.enums.DifficultyEnums;

public class BossFactory extends AbstractEnemyFactory{
    private static int bossCount = 1;
    private DifficultyEnums difficulty;
    private int origHp;
    public BossFactory(DifficultyEnums difficulty) {
        this.probabilityForEnemySpawning = 100;
        this.defaultSpawningProbability = 100;
        this.probabilityIncrement = 0;
        this.speedX = random.nextInt(3,8);
        this.speedY = 0;
        this.hp = 10000;
        this.origHp = this.hp;
        this.difficulty = difficulty;
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        {
            bossCount++;
            return new BossEnemy(
                    (int) (random.nextDouble() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                    (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05),
                    speedX,
                    speedY,
                    hp);
        }
    }

    // 更改逻辑，不采用difficultyFactor因子计算
    @Override
    protected void checkDifficultyFactorAndUpdate(int difficultyFactor) {
        if(this.difficulty == DifficultyEnums.HARD) {
            this.hp = (int)(this.origHp + bossCount*0.2);
        }
    }
}

