package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.shootStrategy.AbstractShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    //最大生命值
    protected int maxHp;
    protected int hp;
    protected List<AbstractShootStrategy> strategyList = new LinkedList<>();
    //每次射击发射子弹数量
    public int shootNum;
    //子弹威力
    protected int power;
    //子弹射击方向 (向上发射：-1，向下发射：1)
    protected int direction = 1;
    protected int shootSpeed = 30;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }


    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public List<BaseBullet> shoot() {
        List<BaseBullet> bullets= new LinkedList<>();
        for(AbstractShootStrategy strategy: strategyList) {
            bullets.addAll(strategy.shootStrategy());
        }

        return bullets;
    };

    public void setShootStrategy(List<AbstractShootStrategy> strategyList) {
        this.strategyList = strategyList;
    }

    public int getShootNum() {
        return shootNum;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    public int getDirection() {
        return direction;
    }

    public int getPower(){
        return power;
    }

    public int getShootSpeed () {
        return shootSpeed;
    };

    public int getSpeedXIncrement() {
        return 1;
    }

    public double getAngelIncrement() {
        return Math.PI / 24;
    }


}


