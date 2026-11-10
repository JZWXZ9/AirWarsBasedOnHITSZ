package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.AbstractBossEnemy;
import edu.hitsz.application.Main;
import edu.hitsz.enums.DifficultyEnums;

public class BossFactory extends AbstractEnemyFactory{
    private static final double HARD_HP_INCREASE_PER_ENCOUNTER = 0.08;
    private static final double HARD_HP_INCREASE_CAP = 0.30;

    private final BossDefinition definition;

    public BossFactory(DifficultyEnums difficulty) {
        this(difficulty, 1);
    }

    public BossFactory(DifficultyEnums difficulty, int encounterNumber) {
        this.probabilityForEnemySpawning = 100;
        this.defaultSpawningProbability = 100;
        this.probabilityIncrement = 0;
        this.speedX = random.nextInt(3,8);
        this.speedY = 0;
        this.definition = BossCatalog.forEncounter(encounterNumber);
        double hardIncrease = Math.min(HARD_HP_INCREASE_CAP,
                Math.max(0, encounterNumber - 1) * HARD_HP_INCREASE_PER_ENCOUNTER);
        this.hp = (int) Math.round(definition.baseHp()
                * (difficulty == DifficultyEnums.HARD ? 1.0 + hardIncrease : 1.0));
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        int y = (int) (random.nextDouble() * Main.WINDOW_HEIGHT * 0.05);
        AbstractBossEnemy boss = definition.creator().create(0, y, speedX, speedY, hp);
        int maxX = Math.max(0, Main.WINDOW_WIDTH - boss.getWidth());
        boss.setLocation((int) (random.nextDouble() * (maxX + 1)), y);
        return boss;
    }

    @Override
    protected void checkDifficultyFactorAndUpdate(int difficultyFactor) {
        // Boss health is determined by the encounter number, not spawn-attempt level.
    }
}

