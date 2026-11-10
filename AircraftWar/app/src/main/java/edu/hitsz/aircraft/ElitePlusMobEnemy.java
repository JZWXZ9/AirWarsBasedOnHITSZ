package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.LinkedList;
import java.util.List;

public class ElitePlusMobEnemy extends AbstractEnemyAircraft {
    public ElitePlusMobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        probabilityForEachProp = List.of(25, 25, 25, 25, 25);
        shootCycle = 2;
        score = 500;
        shootNum = 2;
        shootSpeed = 20;
        power = 15;
        direction = 1;
        strategyList.add(new StraightShoot(this));
        this.freezeDuration = 3000;
    }

    @Override
    public double getAngle() {
        return Math.toDegrees(Math.PI / 2);
    }
}

