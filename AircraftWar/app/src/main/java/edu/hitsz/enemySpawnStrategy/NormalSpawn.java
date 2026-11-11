package edu.hitsz.enemySpawnStrategy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.facroty.EnemyGenerator;

import java.util.LinkedList;
import java.util.List;

public class NormalSpawn extends AbstractSpawnStrategy{
    private static EnemyGenerator generator = EnemyGenerator.getInstance();
    private final DifficultyEnums difficulty;

    public NormalSpawn() {
        this(DifficultyEnums.NORMAL);
    }

    public NormalSpawn(DifficultyEnums difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public List<AbstractEnemyAircraft> spawnEnemy(int difficultyFactor) {
        List<AbstractEnemyAircraft> result = new LinkedList<>();
        result.add(generator.generateEnemy(difficultyFactor, difficulty));
        return result;
    }


}
