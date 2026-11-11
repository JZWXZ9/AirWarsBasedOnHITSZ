package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.enums.DifficultyEnums;

import java.util.LinkedList;
import java.util.List;

public class EnemyGenerator {
    public final List<AbstractEnemyFactory> FACTORY = new LinkedList<>();
    private static EnemyGenerator generator;
    private EnemyGenerator() {
        FACTORY.add(new EliteProMobEnemyFactory());
        FACTORY.add(new ElitePlusMobEnemyFactory());
        FACTORY.add(new EliteTrackingMobEnemyFactory());
        FACTORY.add(new EliteMobEnemyFactory());
        FACTORY.add(new MobEnemyFactory());
    }

    public static EnemyGenerator getInstance() {
        if (generator == null) {
            synchronized (HeroAircraft.class) {
                if (generator == null) {
                    generator = new EnemyGenerator();
                }
            }
        }
        return generator;
    }

    public AbstractEnemyAircraft generateEnemy(int difficultyFactor) {
        return generateEnemy(difficultyFactor, DifficultyEnums.NORMAL);
    }

    public AbstractEnemyAircraft generateEnemy(int difficultyFactor, DifficultyEnums difficulty) {
        AbstractEnemyAircraft enemy = null;
        for(AbstractEnemyFactory factory: FACTORY) {
            enemy = factory.createEnemyByProbability(difficultyFactor, difficulty);
            if (enemy != null) {
                break;
            }

        }
        return enemy;
    }

    public void resetForNewRun() {
        for (AbstractEnemyFactory factory : FACTORY) {
            factory.resetForNewRun();
        }
    }
}
