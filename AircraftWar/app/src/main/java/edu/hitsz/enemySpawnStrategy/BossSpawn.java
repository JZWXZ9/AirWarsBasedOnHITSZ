package edu.hitsz.enemySpawnStrategy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.facroty.BossFactory;

import java.util.LinkedList;
import java.util.List;

public class BossSpawn extends AbstractSpawnStrategy{
    private boolean isSpawningSuccessful = false;
    private BossFactory bossGenerator;
    private DifficultyEnums difficulty;

    public BossSpawn(DifficultyEnums difficulty) {
        this(difficulty, 1);
    }

    public BossSpawn(DifficultyEnums difficulty, int encounterNumber) {
        super();
        this.difficulty = difficulty;
        bossGenerator = new BossFactory(this.difficulty, encounterNumber);
    }

    @Override
    public List<AbstractEnemyAircraft> spawnEnemy(int difficultyFactor){
        if(!isSpawningSuccessful) {
            isSpawningSuccessful = true;
            List<AbstractEnemyAircraft> result = new LinkedList<>();
            result.add(bossGenerator.createEnemyByProbability(difficultyFactor));
            return result;
        }
        return List.of();
    }
}
