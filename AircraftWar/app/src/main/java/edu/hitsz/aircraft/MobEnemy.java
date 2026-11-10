package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.props.AbstractProps;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击、不掉落道具
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemyAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        score = 10;
        this.freezeDuration = 0; // 永久冰冻
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public List<AbstractProps> getPropsIfVanish() { return new LinkedList<>(); }

}
