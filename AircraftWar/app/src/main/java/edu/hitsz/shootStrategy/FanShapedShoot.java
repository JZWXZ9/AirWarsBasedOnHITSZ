package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public class FanShapedShoot extends AbstractShootStrategy {
    public FanShapedShoot(AbstractAircraft aircraft) {
        super(aircraft);
    }

    @Override
    public List<BaseBullet> shootStrategy() {
        List<BaseBullet> res = new LinkedList<>();
        for(int i=0; i< aircraft.getShootNum(); i++) {
            double angel = (2*i - aircraft.getShootNum() + 1) * aircraft.getAngelIncrement() + Math.toRadians(aircraft.getAngle());
            res.add(createHeroOrEnemyBullet(aircraft.getLocationX(), aircraft.getLocationY(),
                    (int) (aircraft.getShootSpeed()*Math.cos(angel)), (int)(aircraft.getShootSpeed()*Math.sin(angel))));
        }
        return res;
    }
}
