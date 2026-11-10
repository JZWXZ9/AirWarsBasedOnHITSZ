package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.shootStrategy.FanShapedShoot;
import edu.hitsz.shootStrategy.ScatterShoot;

import java.util.LinkedList;
import java.util.List;

public class EliteProMobEnemy extends AbstractEnemyAircraft{
    private final int BULLET_TOTAL_SPEED = 10;
    private final int SHOOT_NUM = 3;
    private int maxHp;
    public EliteProMobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.maxHp = hp;
        probabilityForEachProp = List.of(100, 50, 25, 10, 10);
        shootCycle = 2;
        score = 1000;
        shootNum = 3;
        shootSpeed = 20;
        power = 15;
        direction = 1;
        strategyList.add(new ScatterShoot(this));
        this.freezeDuration = 3000;
    }

    @Override
    public List<BaseBullet> shoot(){
        List<BaseBullet> res = new LinkedList<>();
        for(int i=0; i< SHOOT_NUM; i++) {
            double angel = (2*i - SHOOT_NUM + 1) * Math.PI / 18;
            EnemyBullet bullet = new EnemyBullet(locationX, locationY,
                    (int) (BULLET_TOTAL_SPEED*Math.sin(angel)), (int)(BULLET_TOTAL_SPEED*Math.cos(angel)), 30);
            res.add(bullet);
        }
        return res;
    }

    @Override
    public int getSpeedXIncrement() {
        return 10;
    }


    @Override
    public void onFreezeStart() {
        int origSpeedX = this.speedX;
        int origSpeedY = this.speedY;
        this.speedX = origSpeedX / 2;
        this.speedY = origSpeedY / 2;
        // 不设置 isFrozen，所以能继续射击

        new Thread(() -> {
            try {
                Thread.sleep(freezeDuration);
                this.speedX = origSpeedX;
                this.speedY = origSpeedY;
                System.out.println("Ace enemy recovered!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void onBombExplosion() {
        decreaseHp((int)(this.maxHp * 0.5));
    }
}
