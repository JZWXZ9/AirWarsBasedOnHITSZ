package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.PropsFactory;
import edu.hitsz.shootStrategy.BossShoot1;
import edu.hitsz.shootStrategy.FanShapedShoot;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.*;

public class BossEnemy extends AbstractEnemyAircraft{
    private final int TRACKING_BULLET_CYCLE;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // 基本数值
        score = 1500;
        shootCycle = 0;  // 禁用父类的周期射击
        shootNum = 10;    // 每次10颗子弹
        power = 15;
        direction = 1;
        TRACKING_BULLET_CYCLE = 0;
        strategyList.add(new BossShoot1(this));  // 留下拓展空间，打算做成三种形态
    }

    public int getTrackingBulletCycle() {
        return TRACKING_BULLET_CYCLE;
    }

    @Override
    public double getAngle() {
        return Math.toDegrees(Math.PI / 2);
    }

    @Override
    public List<AbstractProps> getPropsIfVanish() {
        List<AbstractProps> props = new LinkedList<>();
        Set<Integer> set = new HashSet<>();

// 生成三个不同的随机数
        while (set.size() < 3) {
            set.add(random.nextInt(5));  // 1-100范围
        }

// 转换为数组或列表
        List<Integer> list = new ArrayList<>(set);
        for(int i : list) {
            props.add(PropsFactory.createProps(PROPS_LIST.get(i), locationX, locationY));
        }
        return props;
    }

    @Override
    public void onFreezeStart() {
        return;
    }

    @Override
    public void onBombExplosion() {
        return;
    }
}

