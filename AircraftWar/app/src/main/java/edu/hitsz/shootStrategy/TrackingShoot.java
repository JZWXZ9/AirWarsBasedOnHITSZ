package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.AbstractTrackingEnemyAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyTrackingBullet;

import java.util.LinkedList;
import java.util.List;


public class TrackingShoot extends AbstractShootStrategy{
    private int trackingBulletCounter = 0;
    private final int TRACKING_BULLET_CYCLE;

    public TrackingShoot(AbstractEnemyAircraft aircraft) {
        super(aircraft);
        TRACKING_BULLET_CYCLE = aircraft.getTrackingBulletCycle();
    }



    @Override
    public List<BaseBullet> shootStrategy(){
        List<BaseBullet> bulletList = new LinkedList<>();
        trackingBulletCounter++;
        if(trackingBulletCounter >= TRACKING_BULLET_CYCLE) {
            trackingBulletCounter = 0;
            // 难度太大暂时不考虑添加
            EnemyTrackingBullet bullet2 = new EnemyTrackingBullet(aircraft.getLocationX(), aircraft.getLocationY(),
                    0, 8, aircraft.getPower());
            bulletList.add(bullet2);
        }

        return bulletList;

    }
}

