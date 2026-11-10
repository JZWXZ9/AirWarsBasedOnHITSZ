package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.List;

public class EliteMobEnemy extends AbstractEnemyAircraft{
    public EliteMobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // 基本数值
        probabilityForEachProp = List.of(10, 10, 0, 0, 0);
        score = 100;
        shootCycle = 10;
        shootNum = 1;
        power = 15;
        direction = 1;
        strategyList.add(new StraightShoot(this));
        this.freezeDuration = 4000;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }
}
