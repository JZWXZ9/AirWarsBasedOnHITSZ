package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class StraightShoot extends AbstractShootStrategy{
    public StraightShoot(AbstractAircraft aircraft) {
        super(aircraft);
    }

    @Override
    public List<BaseBullet> shootStrategy() {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection()*2;
        // 子弹发射时候根据飞机的角度调整
        int speedX = (int) (aircraft.getShootSpeed() * Math.cos(Math.toRadians(aircraft.getAngle())));
        int speedY = (int) (aircraft.getShootSpeed() * Math.sin(Math.toRadians(aircraft.getAngle())));
        BaseBullet bullet;
//        System.out.println("SpeedX, SpeedY" + speedX + " " + speedY);
        for(int i=0; i<aircraft.shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            // 步长为2
            bullet = createHeroOrEnemyBullet(x + (i*2 - aircraft.shootNum + 1)*10, y, speedX, speedY);
            res.add(bullet);
        }
        return res;
    }
}
