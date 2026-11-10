package edu.hitsz.shootStrategy;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.JevilBoss;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.PatternBullet;
import edu.hitsz.bullet.PatternBulletStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Jevil's attack cycle, translated into mouse-dodging patterns for the shooter. */
public final class JevilShoot extends AbstractShootStrategy {
    private static final int PATTERN_DURATION = 84;
    private static final int TELEGRAPH_DURATION = 18;
    private static final double BULLET_POWER = 15;

    private static final Pattern[][] PHASE_PATTERNS = {
            {Pattern.CHAOS_DARTS, Pattern.SPADE_RING, Pattern.CARNIVAL_BOMBS, Pattern.CENTER_SCYTHE},
            {Pattern.CAROUSEL, Pattern.CARNIVAL_BOMBS, Pattern.DIAMOND_RAIN, Pattern.SPADE_RING},
            {Pattern.SPIRAL_SPADES, Pattern.CENTER_SCYTHE, Pattern.CAROUSEL, Pattern.CHAOS_DARTS},
            {Pattern.FINAL_CHAOS, Pattern.CAROUSEL, Pattern.DIAMOND_RAIN},
            {Pattern.FINAL_CHAOS}
    };

    private final JevilBoss boss;
    private int phase = -1;
    private int patternIndex;
    private int patternFrame;
    private int volley;

    public JevilShoot(JevilBoss boss) {
        super(boss);
        this.boss = boss;
    }

    @Override
    public List<BaseBullet> shootStrategy() {
        updatePattern();
        patternFrame++;

        if (patternFrame <= TELEGRAPH_DURATION) {
            return List.of();
        }

        int attackFrame = patternFrame - TELEGRAPH_DURATION;
        Pattern pattern = currentPattern();
        List<BaseBullet> bullets = new ArrayList<>();
        int haste = boss.getHealthRatio() <= 0.4 ? 2 : 0;

        switch (pattern) {
            case CHAOS_DARTS -> {
                if (attackFrame % Math.max(9, 18 - haste) == 0) {
                    aimedFan(bullets, 5, 8.0, 12.0, PatternBulletStyle.DIAMOND);
                }
            }
            case SPADE_RING -> {
                if (attackFrame % Math.max(24, 36 - haste * 3) == 0) {
                    radialWave(bullets, 10, 6.5, PatternBulletStyle.SPADE, volley * 0.12);
                }
            }
            case CARNIVAL_BOMBS -> {
                if (attackFrame % Math.max(12, 18 - haste * 2) == 0) {
                    carnivalBombs(bullets);
                }
            }
            case CENTER_SCYTHE -> {
                if (attackFrame % Math.max(24, 32 - haste * 2) == 0) {
                    scytheSweep(bullets);
                }
            }
            case CAROUSEL -> {
                if (attackFrame % Math.max(28, 40 - haste * 3) == 0) {
                    carousel(bullets);
                }
            }
            case DIAMOND_RAIN -> {
                if (attackFrame % Math.max(18, 26 - haste * 2) == 0) {
                    diamondRain(bullets);
                }
            }
            case SPIRAL_SPADES -> {
                if (attackFrame % Math.max(9, 12 - haste) == 0) {
                    radialWave(bullets, 8, 5.8, PatternBulletStyle.SPADE, volley * 0.28);
                }
            }
            case FINAL_CHAOS -> {
                if (attackFrame % Math.max(9, 12 - haste) == 0) {
                    radialWave(bullets, 12, 6.2, PatternBulletStyle.SCYTHE, volley * 0.16);
                    aimedFan(bullets, 3, 10.0, 8.5, PatternBulletStyle.DIAMOND);
                }
            }
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
        return patternFrame <= TELEGRAPH_DURATION;
    }

    private void updatePattern() {
        int nextPhase = phaseForHealth();
        if (nextPhase != phase) {
            phase = nextPhase;
            patternIndex = 0;
            patternFrame = 0;
            volley = 0;
        }
        if (patternFrame >= PATTERN_DURATION) {
            patternFrame = 0;
            patternIndex = (patternIndex + 1) % PHASE_PATTERNS[phase].length;
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
        return PHASE_PATTERNS[activePhase][patternIndex % PHASE_PATTERNS[activePhase].length];
    }

    private void aimedFan(List<BaseBullet> bullets, int count, double spreadDegrees, double speed,
                          PatternBulletStyle style) {
        HeroAircraft hero = HeroAircraft.getInstance();
        double dx = hero.getLocationX() - boss.getLocationX();
        double dy = hero.getLocationY() - boss.getLocationY();
        double aim = Math.atan2(dy, dx);
        double spread = Math.toRadians(spreadDegrees);

        for (int i = 0; i < count; i++) {
            double offset = count == 1 ? 0.0 : (i - (count - 1) / 2.0) * spread;
            addBullet(bullets, boss.getLocationX(), boss.getLocationY(), aim + offset,
                    speed, style, 0.0, 0.0, 0.0);
        }
    }

    private void radialWave(List<BaseBullet> bullets, int count, double speed,
                            PatternBulletStyle style, double angleOffset) {
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2.0 * i / count) + angleOffset;
            addBullet(bullets, boss.getLocationX(), boss.getLocationY(), angle,
                    speed, style, 0.0, 0.0, 0.0);
        }
    }

    private void carnivalBombs(List<BaseBullet> bullets) {
        HeroAircraft hero = HeroAircraft.getInstance();
        int direction = volley % 2 == 0 ? -1 : 1;
        int offset = 150 + ThreadLocalRandom.current().nextInt(0, 55);
        int x = clamp(hero.getLocationX() + direction * offset, 20, Main.WINDOW_WIDTH - 20);
        addBullet(bullets, x, -8, Math.PI / 2, 7.0, PatternBulletStyle.ORB, 0.0, 0.0, 0.0);
        addBullet(bullets, clamp(x + direction * 56, 20, Main.WINDOW_WIDTH - 20), -42,
                Math.PI / 2, 6.0, PatternBulletStyle.ORB, 0.0, 0.0, 0.0);
    }

    private void scytheSweep(List<BaseBullet> bullets) {
        HeroAircraft hero = HeroAircraft.getInstance();
        int horizontalDirection = volley % 2 == 0 ? 1 : -1;
        int spawnX = horizontalDirection > 0 ? -10 : Main.WINDOW_WIDTH + 10;
        int centerY = clamp(hero.getLocationY() + ((volley % 3) - 1) * 100, 100,
                Main.WINDOW_HEIGHT - 100);
        for (int i = -2; i <= 2; i++) {
            int y = centerY + i * 22;
            double angle = horizontalDirection > 0 ? 0.0 : Math.PI;
            addBullet(bullets, spawnX, y, angle, 9.0, PatternBulletStyle.SCYTHE,
                    0.0, 0.0, 0.0);
        }
    }

    private void carousel(List<BaseBullet> bullets) {
        int columns = 7;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < columns; column++) {
                int x = 30 + column * (Main.WINDOW_WIDTH - 60) / (columns - 1);
                int y = boss.getLocationY() + 28 + row * 22;
                int speedX = ((column + row) % 2 == 0) ? 1 : -1;
                addBullet(bullets, x, y, Math.atan2(6.0, speedX), 6.0,
                        PatternBulletStyle.CAROUSEL, 2.0, 0.11, column * 0.8 + row);
            }
        }
    }

    private void diamondRain(List<BaseBullet> bullets) {
        int columns = 8;
        int firstGap = (volley * 3) % columns;
        int secondGap = (firstGap + 1) % columns;
        for (int column = 0; column < columns; column++) {
            if (column == firstGap || column == secondGap) {
                continue;
            }
            int x = 22 + column * (Main.WINDOW_WIDTH - 44) / (columns - 1);
            addBullet(bullets, x, -12 - (column % 2) * 26, Math.PI / 2,
                    7.2, PatternBulletStyle.DIAMOND, 0.0, 0.0, 0.0);
        }
    }

    private void addBullet(List<BaseBullet> bullets, int x, int y, double angle, double speed,
                           PatternBulletStyle style, double waveAmplitude, double waveFrequency,
                           double wavePhase) {
        int speedX = (int) Math.round(Math.cos(angle) * speed);
        int speedY = (int) Math.round(Math.sin(angle) * speed);
        bullets.add(new PatternBullet(x, y, speedX, speedY, (int) BULLET_POWER, style,
                waveAmplitude, waveFrequency, wavePhase));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private enum Pattern {
        CHAOS_DARTS("CHAOS DARTS"),
        SPADE_RING("SPADE RING"),
        CARNIVAL_BOMBS("CARNIVAL BOMBS"),
        CENTER_SCYTHE("DEVILSKNIFE SWEEP"),
        CAROUSEL("CAROUSEL"),
        DIAMOND_RAIN("DIAMOND RAIN"),
        SPIRAL_SPADES("SPIRAL SPADES"),
        FINAL_CHAOS("FINAL CHAOS");

        private final String displayName;

        Pattern(String displayName) {
            this.displayName = displayName;
        }
    }
}
