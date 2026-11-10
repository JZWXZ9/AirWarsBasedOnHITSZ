package edu.hitsz.props;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;
import java.util.Random;

public abstract class AbstractProps extends AbstractFlyingObject {
    private final int SPAWN_LOCATION_Y;
    private final int CENTER;
    private int originalY = 0;
    static final HeroAircraft heroAircraft = HeroAircraft.getInstance();
    protected int imageWidth;

    public AbstractProps(int locationX, int locationY) {
        super(locationX, locationY, 0, 0);
        SPAWN_LOCATION_Y = locationY;
        Random random = new Random();
        boolean value1 = random.nextBoolean();
        speedY = value1 ? -1 : 1;
        CENTER = value1 ? -5 : 5;
        speedX = random.nextInt(5) - random.nextInt(5);
    }

    // 掉落物逻辑：呈二次函数，先往上走一段距离再
    @Override
    public void forward() {
        locationX += speedX;
        originalY += speedY;
        int y = (int) ((Math.pow((originalY - CENTER), 2) - Math.pow(CENTER, 2)));
        // spawnLocationY - y = 50(x - 1) ^ 2 - 50
        locationY = y + SPAWN_LOCATION_Y;

        if(locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
        if(locationX >= Main.WINDOW_WIDTH - imageWidth || locationX <= imageWidth){
            speedX = 0;
        };
    }

    public abstract void functionOnHero();
    public abstract void functionOnEnemy(List<AbstractEnemyAircraft> enemyAircrafts, List<BaseBullet> enemyBullet);
}
