package edu.hitsz.aircraft;

import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.PropsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Common boss behavior; each concrete boss supplies its own attack strategy. */
public abstract class AbstractBossEnemy extends AbstractEnemyAircraft implements BossEncounter {
    protected AbstractBossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score;
        this.shootCycle = 0;
    }

    @Override
    public String getBossName() {
        return "BOSS";
    }

    @Override
    public String getAttackName() {
        return "INCOMING";
    }

    @Override
    public double getHealthRatio() {
        return maxHp <= 0 ? 0.0 : (double) hp / maxHp;
    }

    @Override
    public boolean isAttackTelegraphing() {
        return false;
    }

    @Override
    public List<AbstractProps> getPropsIfVanish() {
        List<AbstractProps> drops = new ArrayList<>(3);
        List<Integer> indexes = new ArrayList<>(List.of(0, 1, 2, 3, 4));
        for (int i = 0; i < 3; i++) {
            int picked = ThreadLocalRandom.current().nextInt(i, indexes.size());
            int value = indexes.get(picked);
            indexes.set(picked, indexes.get(i));
            drops.add(PropsFactory.createProps(PROPS_LIST.get(value), locationX, locationY));
        }
        return drops;
    }

    @Override
    public void onFreezeStart() {
        // Boss attacks keep their rhythm; freeze only affects ordinary enemies.
    }

    @Override
    public void onBombExplosion() {
        // Bosses are immune to the screen-clearing bomb.
    }
}
