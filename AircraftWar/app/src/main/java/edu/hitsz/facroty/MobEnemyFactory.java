package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class MobEnemyFactory extends AbstractEnemyFactory{
    public MobEnemyFactory() {
        this.probabilityForEnemySpawning = 100;
        this.defaultSpawningProbability = 100;
        this.probabilityIncrement = 0;
        this.speedX = 0;
        this.speedY = 10;
        this.hp = 30;
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        {
            return new MobEnemy(
                    (int) (random.nextDouble() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                    (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05),
                    speedX,
                    speedY,
                    hp);
        }
    }
}
