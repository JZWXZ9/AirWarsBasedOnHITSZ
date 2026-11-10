package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shootStrategy.AbstractShootStrategy;
import edu.hitsz.shootStrategy.FanShapedShoot;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    // 无敌帧，道具相关
    private boolean isInvincible = false;
    // 计算无敌帧时间
    private int invincibleTime = 0;
    // 期望的无敌时间
    private int expectedInvincibleTime = 0;

    private volatile static HeroAircraft hero;
    private HeroAircraft() {
        super(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - (int) ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 500);
        shootNum = 1;
        power = 30;
        direction = -1;
        strategyList.add(new StraightShoot(this));
    };

    // 双重线程锁
    public static HeroAircraft getInstance() {
        if (hero == null) {
            synchronized (HeroAircraft.class) {
                if (hero == null) {
                    hero = new HeroAircraft();
                }
            }
        }
        return hero;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }



    // 加血
    public void increaseHp(int hpIncreasement) {
        hp += hpIncreasement;
        // 满血检测
        if (hp >= maxHp) {
            hp = maxHp;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void increaseMaxHp(int increasementOfMaxHp) {
        this.maxHp += increasementOfMaxHp;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    public void setExpectedInvincibleTime(int expectedInvincibleTime) {
        this.expectedInvincibleTime = expectedInvincibleTime;
    }

    public void invincibleController() {
        if(isInvincible) {
            invincibleTime++;
            if (invincibleTime >= expectedInvincibleTime) {
                invincibleTime = 0;
                expectedInvincibleTime = 0;
                isInvincible = false;
            }
        }
    }

    @Override
    public double getAngle() {
        return Math.toDegrees(3 * Math.PI / 2);
    }

    @Override
    public double getAngelIncrement() {
        return Math.PI / 40;
    }

}
