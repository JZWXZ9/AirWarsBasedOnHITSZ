package edu.hitsz.props;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shootStrategy.FanShapedShoot;
import edu.hitsz.shootStrategy.ScatterShoot;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.List;

public class BulletPlusProps extends AbstractProps{
    public BulletPlusProps(int locationX, int locationY) {
        super(locationX, locationY);
        imageWidth = (int) ImageManager.BULLET_PROP.getWidth();
    }

    public void functionOnHero() {
        System.out.println("Fire Supply!");
        Runnable r = () -> {
            try {
                if(heroAircraft.getShootNum() <= 5) {
                    heroAircraft.setShootNum(heroAircraft.getShootNum()+1);
                }
                heroAircraft.setShootStrategy(List.of(new FanShapedShoot(heroAircraft)));
                Thread.sleep(10000); //持续10s
                heroAircraft.setShootStrategy(List.of(new StraightShoot(heroAircraft)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(r, "ScatterShootThread").start();
    }

    @Override
    public void functionOnEnemy(List<AbstractEnemyAircraft> enemyAircrafts, List<BaseBullet> enemyBullet) {
        return;
    }
}
