package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.EliteTrackingMobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteTrackingMobEnemyFactory extends AbstractEnemyFactory{
    public EliteTrackingMobEnemyFactory() {
        this.probabilityForEnemySpawning = 0;
        this.defaultSpawningProbability = 0;
        this.probabilityIncrement = 1;
        this.speedX = 0;
        this.speedY = 10;
        this.hp = 300;
        configureHardHpScaling(0.01, 0.12);
        rememberBaseStats();
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        return new EliteTrackingMobEnemy(
                (int) (random.nextDouble() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05),
                speedX,
                speedY,
                hp);
    }
}
