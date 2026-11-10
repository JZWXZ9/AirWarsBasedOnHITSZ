package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatterShoot extends AbstractShootStrategy{
    public ScatterShoot(AbstractAircraft aircraft) {
        super(aircraft);
    }

    @Override
    public List<BaseBullet> shootStrategy() {
        List<BaseBullet> res = new LinkedList<>();
        int speedXIncrement = 10;
        for(int i=0; i< aircraft.getShootNum(); i++) {
            res.add(createHeroOrEnemyBullet(aircraft.getLocationX(), aircraft.getLocationY(),
                    (i*2 - aircraft.getShootNum() + 1) * aircraft.getSpeedXIncrement(), aircraft.getShootSpeed()*aircraft.getDirection()));
        }
        return res;
    }
}

