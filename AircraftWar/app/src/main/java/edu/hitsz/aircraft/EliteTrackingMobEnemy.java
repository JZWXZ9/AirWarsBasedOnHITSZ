package edu.hitsz.aircraft;

import edu.hitsz.shootStrategy.StraightShoot;
import edu.hitsz.shootStrategy.TrackingShoot;

import java.util.List;

public class EliteTrackingMobEnemy extends AbstractTrackingEnemyAircraft{
    public EliteTrackingMobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        probabilityForEachProp = List.of(20, 10, 0, 0, 0);
        shootCycle = 3;
        score = 150;
        shootNum = 1;
        shootSpeed = 15;
        power = 10;
        direction = 1;
        strategyList.add(new StraightShoot(this));
        strategyList.add(new TrackingShoot(this));
        this.freezeDuration = 4000;
    }
}
