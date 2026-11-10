package edu.hitsz.facroty;

import edu.hitsz.aircraft.AbstractBossEnemy;

public record BossDefinition(String id, int baseHp, BossCreator creator) {
    public BossDefinition {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Boss id must not be blank");
        }
        if (baseHp <= 0) {
            throw new IllegalArgumentException("Boss HP must be positive");
        }
        if (creator == null) {
            throw new IllegalArgumentException("Boss creator is required");
        }
    }

    @FunctionalInterface
    public interface BossCreator {
        AbstractBossEnemy create(int x, int y, int speedX, int speedY, int hp);
    }
}
