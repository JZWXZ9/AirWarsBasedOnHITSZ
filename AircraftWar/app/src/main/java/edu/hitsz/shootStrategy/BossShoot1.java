package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public class BossShoot1 extends AbstractShootStrategy{
    public BossShoot1(BossEnemy aircraft) {
        super(aircraft);
    }

    private enum ShootPhase {
        IDLE,           // 等待下一轮攻击
        FIRST_SHOOT,    // 第一次射击
        WAIT_SECOND,    // 等待第二次射击
        SECOND_SHOOT    // 第二次射击
    }
    private BossShoot1.ShootPhase shootPhase = BossShoot1.ShootPhase.IDLE;
    private int phaseCounter = 0;      // 阶段计数器（用于帧计数）

    // 射击参数（帧计数，假设 40ms/帧）
    private static final int IDLE_DURATION = 38;        // 1.5秒等待 (40ms * 38 ≈ 1520ms)
    private static final int WAIT_DURATION = 6;         // 两次射击间隔0.25秒 (40ms * 6 = 240ms)
    private final AbstractShootStrategy fanShapedShoot = new FanShapedShoot(aircraft);
    private final AbstractShootStrategy trackingShoot = new TrackingShoot((AbstractEnemyAircraft) aircraft);

    @Override
    public List<BaseBullet> shootStrategy(){

        List<BaseBullet> bullets = new LinkedList<>();

        // 运用状态机来射击
            switch (shootPhase) {
            case IDLE:
                phaseCounter++;
                if (phaseCounter >= IDLE_DURATION) {
                    // 等待结束，开始第一次射击
                    shootPhase = BossShoot1.ShootPhase.FIRST_SHOOT;
                    phaseCounter = 0;
                }
                break;

            case FIRST_SHOOT:
                // 发射第一次扇形子弹
                bullets.addAll(fanShapedShoot.shootStrategy());
                shootPhase = BossShoot1.ShootPhase.WAIT_SECOND;
                phaseCounter = 0;
                break;

            case WAIT_SECOND:
                bullets.addAll(trackingShoot.shootStrategy());
                phaseCounter++;
                if (phaseCounter >= WAIT_DURATION) {
                    // 等待结束，开始第二次射击
                    shootPhase = BossShoot1.ShootPhase.SECOND_SHOOT;
                    phaseCounter = 0;
                }
                break;

            case SECOND_SHOOT:
                // 发射第二次扇形子弹
                bullets.addAll(fanShapedShoot.shootStrategy());
                // 攻击结束，回到空闲状态
                shootPhase = BossShoot1.ShootPhase.IDLE;
                phaseCounter = 0;
                break;
        }

            return bullets;
    }
}

