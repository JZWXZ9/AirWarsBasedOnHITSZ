package edu.hitsz.props;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class BloodProps extends AbstractProps{
    private final int INCREASEMENT_OF_HP;
    public BloodProps(int locationX, int locationY, int increasementOfHp) {
        super(locationX, locationY) ;
        this.INCREASEMENT_OF_HP = increasementOfHp;
        imageWidth = (int) ImageManager.BLOOD_PROP.getWidth();
    }

    @Override
    public void functionOnHero() {
        if (heroAircraft.getHp() == heroAircraft.getMaxHp()) {
            heroAircraft.increaseMaxHp(INCREASEMENT_OF_HP);
        }
        heroAircraft.increaseHp(INCREASEMENT_OF_HP);
    }

    @Override
    public void functionOnEnemy(List<AbstractEnemyAircraft> enemyAircrafts, List<BaseBullet> enemyBullet) {
        return;
    }
}
