package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.EliteMobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteMobEnemyFactory extends AbstractEnemyFactory{
    public EliteMobEnemyFactory() {
        this.probabilityForEnemySpawning = 0;
        this.defaultSpawningProbability = 0;
        this.probabilityIncrement = 10;
        this.speedX = 0;
        this.speedY = 10;
        this.hp = 300;
        configureHardHpScaling(0.01, 0.12);
        rememberBaseStats();
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        return new EliteMobEnemy(
                (int) (random.nextDouble() * (Main.WINDOW_WIDTH - ImageManager.ELITE_MOB_ENEMY_IMAGE.getWidth())),
                (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05),
                speedX,
                speedY,
                hp);
    }
}
