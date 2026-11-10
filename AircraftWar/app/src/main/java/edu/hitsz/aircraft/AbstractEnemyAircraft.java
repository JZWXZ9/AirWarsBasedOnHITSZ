package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.observer.FreezeObserver;
import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.PropsFactory;

import java.util.*;

public abstract class AbstractEnemyAircraft extends AbstractAircraft
        implements BombObserver, FreezeObserver {

    public boolean isFrozen = false;
    // 原有的 speedX 和 speedY 备份
    protected int originalSpeedX;
    protected int originalSpeedY;// 新增字段
    protected long freezeDuration = 0;  // 冰冻时间（毫秒），0表示永久
    static final List<String> PROPS_LIST = List.of("BloodProps", "BulletProps", "BulletPlusProps", "BombProps", "FreezeProps");
    protected List<Integer> probabilityForEachProp; //百分比概率
    static Random random = new Random();
    protected int score;
    protected int shootCounter = 0;
    protected int shootCycle = 0;

    public AbstractEnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // 如果没有就全部初始化为0
        if (probabilityForEachProp == null) {
            probabilityForEachProp = new ArrayList<>();
            for (int i=0; i<PROPS_LIST.size(); i++) {
                probabilityForEachProp.add(0);
            }
        }
    }

    public List<AbstractProps> getPropsIfVanish(){
        List<AbstractProps> props = new LinkedList<>();
        if(PROPS_LIST.size() != probabilityForEachProp.size()) {
            System.out.println(this.getClass().getName() + "has different size between props and probability!");
            return Collections.emptyList();
        }
        for (int i=0; i<PROPS_LIST.size(); i++) {
            if (random.nextInt(1, 101) <= probabilityForEachProp.get(i)) {
                    props.add(PropsFactory.createProps(PROPS_LIST.get(i), locationX, locationY));
            }
        }
        return props;
    }

    public List<BaseBullet> shootByTick() {
        if(!isFrozen) {
            if (shootCounter >= shootCycle) {
                shootCounter = 0;
                return shoot();
            } else {
                shootCounter++;
                return new LinkedList<>();
            }
        }
        else{
            return List.of();
        }
    }



    public int getScore() {
        return score;
    }

    public int getTrackingBulletCycle() {
        return 0;
    }


    @Override
    public void onBombExplosion() {
        this.vanish();
    }

    // 实现 FreezeObserver 接口
    @Override
    public void onFreezeStart() {
        int origSpeedX = this.speedX;
        int origSpeedY = this.speedY;
        this.speedX = 0;
        this.speedY = 0;
        this.isFrozen = true;

        // FreezeDuration == 0: 一直冰冻
        if (freezeDuration > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep(freezeDuration);
                    this.speedX = origSpeedX;
                    this.speedY = origSpeedY;
                    this.isFrozen = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
