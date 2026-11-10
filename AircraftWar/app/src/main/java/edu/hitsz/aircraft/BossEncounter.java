package edu.hitsz.aircraft;

/** Shared information the game loop needs to present a boss encounter. */
public interface BossEncounter {
    String getBossName();

    String getAttackName();

    double getHealthRatio();

    boolean isAttackTelegraphing();
}
