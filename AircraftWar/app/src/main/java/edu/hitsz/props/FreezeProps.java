package edu.hitsz.props;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.observer.ConcreteFreezeSubject;
import edu.hitsz.observer.FreezeObserver;
import edu.hitsz.observer.FreezeSubject;

import java.util.List;

public class FreezeProps extends AbstractProps {
    private final ConcreteFreezeSubject freezeSubject;

    public FreezeProps(int locationX, int locationY) {
        super(locationX, locationY);
        imageWidth = (int) ImageManager.FREEZE_PROP.getWidth();
        this.freezeSubject = new ConcreteFreezeSubject();
    }

    @Override
    public void functionOnHero() {
        return;
    }

    @Override
    public void functionOnEnemy(List<AbstractEnemyAircraft> enemyAircrafts, List<BaseBullet> enemyBullet) {
        // 将所有敌机注册为观察者
        for (AbstractEnemyAircraft enemy : enemyAircrafts) {
            freezeSubject.addObserver(enemy);
        }
        for (BaseBullet bullet : enemyBullet) {
            freezeSubject.addObserver(bullet);
        }

        freezeSubject.trigger();
    }
}