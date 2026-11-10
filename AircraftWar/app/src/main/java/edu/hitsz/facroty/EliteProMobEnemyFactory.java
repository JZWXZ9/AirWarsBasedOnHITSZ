package edu.hitsz.facroty;


import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.EliteProMobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteProMobEnemyFactory extends AbstractEnemyFactory{
    public EliteProMobEnemyFactory() {
        this.probabilityForEnemySpawning = -40;
        this.defaultSpawningProbability = -40;
        this.probabilityIncrement = 1;
        this.speedY = 2;
        this.hp = 5000;
        this.maxSpawnProbability = 20;
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        {
            return new EliteProMobEnemy(
                    (int) (random.nextDouble() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                    (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05),
                    random.nextInt(5) - random.nextInt(5),
                    speedY,
                    hp);
        }
    }
}


