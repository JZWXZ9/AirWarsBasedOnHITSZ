package edu.hitsz.facroty;

import edu.hitsz.aircraft.JevilBoss;

import java.util.List;

/** Add a boss encounter by registering its stats and constructor here. */
public final class BossCatalog {
    private static final List<BossDefinition> ENCOUNTERS = List.of(
            new BossDefinition("jevil", JevilBoss.BASE_HP, JevilBoss::new)
    );

    private BossCatalog() {
    }

    public static BossDefinition forEncounter(int encounterNumber) {
        if (encounterNumber < 1) {
            throw new IllegalArgumentException("Encounter numbers start at 1");
        }
        return ENCOUNTERS.get((encounterNumber - 1) % ENCOUNTERS.size());
    }
}
