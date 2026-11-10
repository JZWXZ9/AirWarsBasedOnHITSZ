package edu.hitsz.facroty;


import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.ElitePlusMobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class ElitePlusMobEnemyFactory extends AbstractEnemyFactory{
    public ElitePlusMobEnemyFactory() {
        this.probabilityForEnemySpawning = -20;
        this.defaultSpawningProbability = -20;
        this.probabilityIncrement = 1;
        this.speedY = 5;
        this.hp = 300;
        this.maxSpawnProbability = 40;
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        {
            return new ElitePlusMobEnemy(
                    (int) (random.nextDouble() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                    (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05),
                    random.nextInt(10) - random.nextInt(10),
                    speedY,
                    hp);
        }
    }
}

