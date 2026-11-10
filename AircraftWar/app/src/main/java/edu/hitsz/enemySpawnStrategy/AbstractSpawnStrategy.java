package edu.hitsz.enemySpawnStrategy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;

import java.util.List;

public abstract class AbstractSpawnStrategy {


    public abstract List<AbstractEnemyAircraft> spawnEnemy(int difficultyFactor);
}
