package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.observer.BombSubject;
import edu.hitsz.observer.FreezeObserver;
import edu.hitsz.observer.FreezeSubject;

/**
 * 子弹基类
 * @author hitsz
 */
public abstract class BaseBullet extends AbstractFlyingObject implements BombObserver, FreezeObserver {

    private int power = 10;
    protected boolean isFrozen = false;


    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        if(isFrozen) {
            return;
        }

        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }

    @Override
    public void onBombExplosion() {
        this.vanish();
    }

    @Override
    public void onFreezeStart() {
        this.isFrozen = true;
        // 5秒后恢复
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                this.isFrozen = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

