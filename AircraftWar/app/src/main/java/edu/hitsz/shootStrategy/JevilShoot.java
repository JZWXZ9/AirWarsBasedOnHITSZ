package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.JevilBoss;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.PatternBullet;
import edu.hitsz.bullet.PatternBulletStyle;

import java.util.ArrayList;
import java.util.List;

/** Jevil's recognizable Chapter 1 attack sequence adapted to the battle arena. */
public final class JevilShoot extends AbstractShootStrategy {
    private static final int TELEGRAPH_TICKS = 20;
    private static final int NORMAL_ATTACK_TICKS = 112;
    private static final int FINAL_ATTACK_TICKS = 164;
    private static final int BULLET_POWER = 15;

    // Attack IDs follow Jevil's source attack table: 0–3, 4–7, 8–13, 12–15.
    private static final Pattern[][] PHASES = {
            {Pattern.TELEPORT_CARDS, Pattern.SPADE_RING, Pattern.SUIT_BOMBS, Pattern.CENTER_SCYTHE},
            {Pattern.CAROUSEL, Pattern.SUIT_BOMBS, Pattern.VERTICAL_DIAMONDS, Pattern.SPADE_RING},
            {Pattern.SUIT_BOMBS, Pattern.DARK_CLUBS, Pattern.CENTER_SCYTHE,
                    Pattern.TELEPORT_CARDS, Pattern.SUIT_BOMBS, Pattern.VERTICAL_DIAMONDS},
            {Pattern.TELEPORT_CARDS, Pattern.SUIT_BOMBS, Pattern.VERTICAL_DIAMONDS, Pattern.FINAL_CHAOS},
            {Pattern.FINAL_CHAOS}
    };

    private final JevilBoss boss;
    private int phase = -1;
    private int patternIndex;
    private int patternTick;
    private int volley;

    public JevilShoot(JevilBoss boss) {
        super(boss);
        this.boss = boss;
    }

    @Override
    public List<BaseBullet> shootStrategy() {
        updatePattern();
        patternTick++;

        if (patternTick <= TELEGRAPH_TICKS) {
            return List.of();
        }

        int attackTick = patternTick - TELEGRAPH_TICKS;
        Pattern attack = currentPattern();
        List<BaseBullet> bullets = new ArrayList<>();
        boolean enraged = boss.getHealthRatio() <= 0.4;

        switch (attack) {
            case TELEPORT_CARDS -> teleportCards(bullets, attackTick, enraged);
            case SPADE_RING -> spadeRing(bullets, attackTick, enraged);
            case SUIT_BOMBS -> suitBombs(bullets, attackTick, enraged);
            case CENTER_SCYTHE -> centerScythe(bullets, attackTick, enraged);
            case CAROUSEL -> carousel(bullets, attackTick, enraged);
            case VERTICAL_DIAMONDS -> verticalDiamonds(bullets, attackTick, enraged);
            case DARK_CLUBS -> darkClubs(bullets, attackTick, enraged);
            case FINAL_CHAOS -> finalChaos(bullets, attackTick);
        }

        if (!bullets.isEmpty()) {
            volley++;
        }
        return bullets;
    }

    public String getAttackName() {
        return currentPattern().displayName;
    }

    public boolean isTelegraphing() {
        return patternTick <= TELEGRAPH_TICKS;
    }

    private void updatePattern() {
        int healthPhase = phaseForHealth();
        if (healthPhase != phase) {
            phase = healthPhase;
            patternIndex = 0;
            patternTick = 0;
            volley = 0;
        }

        int duration = currentPattern() == Pattern.FINAL_CHAOS
                ? FINAL_ATTACK_TICKS : NORMAL_ATTACK_TICKS;
        if (patternTick >= duration) {
            patternTick = 0;
            patternIndex = (patternIndex + 1) % PHASES[phase].length;
            volley = 0;
        }
    }

    private int phaseForHealth() {
        double ratio = boss.getHealthRatio();
        if (ratio > 0.8) return 0;
        if (ratio > 0.6) return 1;
        if (ratio > 0.4) return 2;
        if (ratio > 0.15) return 3;
        return 4;
    }

    private Pattern currentPattern() {
        int activePhase = phase < 0 ? phaseForHealth() : phase;
        return PHASES[activePhase][patternIndex % PHASES[activePhase].length];
    }

    /** Rotating card shots appear from the arena's sides and corners, like Jevil's teleports. */
    private void teleportCards(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        int cadence = enraged ? 24 : 32;
        if (attackTick % cadence != 0) return;

        int arenaWidth = Main.BATTLE_ARENA_RIGHT - Main.BATTLE_ARENA_LEFT;
        int side = volley % 4;
        int x;
        int y;
        if (side == 0 || side == 1) {
            x = side == 0 ? Main.BATTLE_ARENA_LEFT - 12 : Main.BATTLE_ARENA_RIGHT + 12;
            y = Main.BATTLE_ARENA_TOP + 30 + (volley % 3) * 68;
        } else {
            x = Main.BATTLE_ARENA_LEFT + 30 + (volley * 73 % Math.max(1, arenaWidth - 60));
            y = side == 2 ? Main.BATTLE_ARENA_TOP - 12 : Main.BATTLE_ARENA_BOTTOM + 12;
        }

        HeroAircraft hero = HeroAircraft.getInstance();
        double aim = angleTo(x, y, hero.getLocationX(), hero.getLocationY());
        int count = enraged ? 5 : 3;
        fan(bullets, x, y, aim, count, Math.toRadians(10), 6.2,
                PatternBulletStyle.SPADE);
    }

    /** A ring of spades closes in from the box edges and leaves lanes between the shots. */
    private void spadeRing(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        int cadence = enraged ? 30 : 38;
        if (attackTick % cadence != 0) return;

        HeroAircraft hero = HeroAircraft.getInstance();
        int left = Main.BATTLE_ARENA_LEFT - 18;
        int right = Main.BATTLE_ARENA_RIGHT + 18;
        int top = Main.BATTLE_ARENA_TOP + 14;
        int bottom = Main.BATTLE_ARENA_BOTTOM - 12;
        int[][] positions = {
                {left, top}, {(left + right) / 2, top}, {right, top},
                {right, (top + bottom) / 2}, {right, bottom}, {(left + right) / 2, bottom},
                {left, bottom}, {left, (top + bottom) / 2}
        };
        int phaseOffset = volley % 2;
        for (int i = phaseOffset; i < positions.length; i += 2) {
            int[] p = positions[i];
            addAimed(bullets, p[0], p[1], hero.getLocationX(), hero.getLocationY(),
                    enraged ? 7.0 : 6.0, PatternBulletStyle.SPADE);
        }
    }

    /** Suit bombs enter from alternating sides in aimed, staggered lanes. */
    private void suitBombs(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        int cadence = enraged ? 25 : 34;
        if (attackTick % cadence != 0) return;

        HeroAircraft hero = HeroAircraft.getInstance();
        boolean fromLeft = volley % 2 == 0;
        int x = fromLeft ? Main.BATTLE_ARENA_LEFT - 20 : Main.BATTLE_ARENA_RIGHT + 20;
        int laneShift = (volley % 3 - 1) * 42;
        int y = clamp((int) hero.getLocationY() + laneShift,
                Main.BATTLE_ARENA_TOP + 22, Main.BATTLE_ARENA_BOTTOM - 22);
        addAimed(bullets, x, y, hero.getLocationX(), hero.getLocationY(),
                enraged ? 7.2 : 6.2, PatternBulletStyle.ORB);

        if (enraged) {
            int secondY = clamp(y + (fromLeft ? 74 : -74),
                    Main.BATTLE_ARENA_TOP + 22, Main.BATTLE_ARENA_BOTTOM - 22);
            addAimed(bullets, x, secondY, hero.getLocationX(), hero.getLocationY(),
                    6.4, PatternBulletStyle.ORB);
        }
    }

    /** Large Devilsknife sweeps across the box at a telegraphed horizontal lane. */
    private void centerScythe(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        int cadence = enraged ? 48 : 62;
        if (attackTick % cadence != 0) return;

        HeroAircraft hero = HeroAircraft.getInstance();
        int direction = volley % 2 == 0 ? 1 : -1;
        int x = direction > 0 ? Main.BATTLE_ARENA_LEFT - 48 : Main.BATTLE_ARENA_RIGHT + 48;
        int laneOffset = (volley % 3 - 1) * 44;
        int y = clamp((int) hero.getLocationY() + laneOffset,
                Main.BATTLE_ARENA_TOP + 35, Main.BATTLE_ARENA_BOTTOM - 35);
        addBullet(bullets, x, y, direction > 0 ? 0 : Math.PI,
                enraged ? 9.0 : 7.5, PatternBulletStyle.SCYTHE);
    }

    /** Three staggered rows of carousel horses descend while weaving across the box. */
    private void carousel(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        if (attackTick != 8) return;

        int columns = 7;
        int left = Main.BATTLE_ARENA_LEFT + 14;
        int width = Main.BATTLE_ARENA_RIGHT - Main.BATTLE_ARENA_LEFT - 28;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < columns; column++) {
                int x = left + column * width / (columns - 1);
                int y = Main.BATTLE_ARENA_TOP - 20 - row * 32;
                double phase = column * Math.PI / 3 + row * 0.8;
                addBullet(bullets, x, y, Math.PI / 2,
                        enraged ? 5.4 : 4.6, PatternBulletStyle.CAROUSEL,
                        20.0, 0.075, phase);
            }
        }
    }

    /** Falling diamond columns leave a moving two-column gap for the soul to pass through. */
    private void verticalDiamonds(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        int cadence = enraged ? 28 : 38;
        if (attackTick % cadence != 0) return;

        int columns = 8;
        int gapA = (volley * 3 + 1) % columns;
        int gapB = (gapA + 1 + volley % (columns - 1)) % columns;
        int left = Main.BATTLE_ARENA_LEFT + 13;
        int width = Main.BATTLE_ARENA_RIGHT - Main.BATTLE_ARENA_LEFT - 26;
        for (int column = 0; column < columns; column++) {
            if (column == gapA || column == gapB) continue;
            int x = left + column * width / (columns - 1);
            addBullet(bullets, x, Main.BATTLE_ARENA_TOP - 12 - (column % 2) * 24,
                    Math.PI / 2, enraged ? 7.4 : 6.4, PatternBulletStyle.DIAMOND);
        }
    }

    /** Dark clubs push inward from both sides, alternating to keep a safe moving lane. */
    private void darkClubs(List<BaseBullet> bullets, int attackTick, boolean enraged) {
        int cadence = enraged ? 24 : 32;
        if (attackTick % cadence != 0) return;

        HeroAircraft hero = HeroAircraft.getInstance();
        boolean fromLeft = volley % 2 == 0;
        int x = fromLeft ? Main.BATTLE_ARENA_LEFT - 14 : Main.BATTLE_ARENA_RIGHT + 14;
        for (int offset : new int[]{-52, 0, 52}) {
            int y = clamp((int) hero.getLocationY() + offset,
                    Main.BATTLE_ARENA_TOP + 12, Main.BATTLE_ARENA_BOTTOM - 12);
            addAimed(bullets, x, y, hero.getLocationX(), hero.getLocationY(),
                    enraged ? 7.0 : 6.0, PatternBulletStyle.CLUB);
        }
    }

    /** The final attack combines rotating cards, diamonds and the crossing Devilsknife. */
    private void finalChaos(List<BaseBullet> bullets, int attackTick) {
        if (attackTick % 24 == 0) {
            int x = Main.BATTLE_ARENA_LEFT + 24
                    + (volley * 97 % (Main.BATTLE_ARENA_RIGHT - Main.BATTLE_ARENA_LEFT - 48));
            int y = Main.BATTLE_ARENA_TOP - 12;
            HeroAircraft hero = HeroAircraft.getInstance();
            double aim = angleTo(x, y, hero.getLocationX(), hero.getLocationY());
            fan(bullets, x, y, aim, 5, Math.toRadians(13), 7.0,
                    PatternBulletStyle.SPADE);
        }
        if (attackTick % 52 == 0) {
            boolean fromLeft = (attackTick / 52) % 2 == 0;
            int x = fromLeft ? Main.BATTLE_ARENA_LEFT - 46 : Main.BATTLE_ARENA_RIGHT + 46;
            int y = clamp((int) HeroAircraft.getInstance().getLocationY(),
                    Main.BATTLE_ARENA_TOP + 40, Main.BATTLE_ARENA_BOTTOM - 40);
            addBullet(bullets, x, y, fromLeft ? 0 : Math.PI, 8.8,
                    PatternBulletStyle.SCYTHE);
        }
    }

    private void fan(List<BaseBullet> bullets, int x, int y, double centerAngle, int count,
                     double stepAngle, double speed, PatternBulletStyle style) {
        for (int i = 0; i < count; i++) {
            double offset = (i - (count - 1) / 2.0) * stepAngle;
            addBullet(bullets, x, y, centerAngle + offset, speed, style);
        }
    }

    private void addAimed(List<BaseBullet> bullets, int x, int y, double targetX, double targetY,
                          double speed, PatternBulletStyle style) {
        addBullet(bullets, x, y, angleTo(x, y, targetX, targetY), speed, style);
    }

    private void addBullet(List<BaseBullet> bullets, int x, int y, double angle, double speed,
                           PatternBulletStyle style) {
        addBullet(bullets, x, y, angle, speed, style, 0.0, 0.0, 0.0);
    }

    private void addBullet(List<BaseBullet> bullets, int x, int y, double angle, double speed,
                           PatternBulletStyle style, double waveAmplitude, double waveFrequency,
                           double wavePhase) {
        int speedX = (int) Math.round(Math.cos(angle) * speed);
        int speedY = (int) Math.round(Math.sin(angle) * speed);
        bullets.add(new PatternBullet(x, y, speedX, speedY, BULLET_POWER, style,
                waveAmplitude, waveFrequency, wavePhase));
    }

    private static double angleTo(double x, double y, double targetX, double targetY) {
        return Math.atan2(targetY - y, targetX - x);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private enum Pattern {
        TELEPORT_CARDS("TELEPORT CARDS"),
        SPADE_RING("SPADE RING"),
        SUIT_BOMBS("SUIT BOMBS"),
        CENTER_SCYTHE("DEVILSKNIFE"),
        CAROUSEL("CAROUSEL"),
        VERTICAL_DIAMONDS("DIAMOND WALL"),
        DARK_CLUBS("DARK CLUBS"),
        FINAL_CHAOS("THE WORLD REVOLVING");

        private final String displayName;

        Pattern(String displayName) {
            this.displayName = displayName;
        }
    }
}
