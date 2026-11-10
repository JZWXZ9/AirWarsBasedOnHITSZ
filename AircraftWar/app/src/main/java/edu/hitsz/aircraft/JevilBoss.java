package edu.hitsz.aircraft;

import edu.hitsz.shootStrategy.JevilShoot;
import edu.hitsz.application.ImageManager;

import java.util.List;

/** First special encounter, using a fast sequence of distinct bullet patterns. */
public final class JevilBoss extends AbstractBossEnemy {
    public static final int BASE_HP = 8_000;

    private final JevilShoot attack;

    public JevilBoss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 2_500);
        this.image = ImageManager.JEVIL_BOSS_IMAGE;
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.shootNum = 10;
        this.power = 15;
        this.direction = 1;
        this.attack = new JevilShoot(this);
        this.strategyList = List.of(attack);
    }

    @Override
    public String getBossName() {
        return "JEVIL";
    }

    @Override
    public String getAttackName() {
        return attack.getAttackName();
    }

    @Override
    public boolean isAttackTelegraphing() {
        return attack.isTelegraphing();
    }
}
