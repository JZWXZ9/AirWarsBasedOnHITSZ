package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import javafx.scene.image.Image;

/** Reusable themed projectile with optional sideways wave motion. */
public final class PatternBullet extends EnemyBullet {
    private final double waveAmplitude;
    private final double waveFrequency;
    private final double wavePhase;
    private final PatternBulletStyle style;
    private int age;

    public PatternBullet(int locationX, int locationY, int speedX, int speedY, int power,
                         PatternBulletStyle style) {
        this(locationX, locationY, speedX, speedY, power, style, 0.0, 0.0, 0.0);
    }

    public PatternBullet(int locationX, int locationY, int speedX, int speedY, int power,
                         PatternBulletStyle style, double waveAmplitude, double waveFrequency,
                         double wavePhase) {
        super(locationX, locationY, speedX, speedY, power);
        this.style = style;
        this.waveAmplitude = waveAmplitude;
        this.waveFrequency = waveFrequency;
        this.wavePhase = wavePhase;
        this.image = PatternBulletImages.get(style);
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
    }

    public PatternBulletStyle getStyle() {
        return style;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void forward() {
        if (isFrozen) {
            return;
        }
        if (waveAmplitude == 0.0) {
            super.forward();
            return;
        }

        age++;
        locationX += speedX + (int) Math.round(Math.sin(age * waveFrequency + wavePhase) * waveAmplitude);
        locationY += speedY;
        if (locationX < -width || locationX > Main.WINDOW_WIDTH + width
                || locationY < -height || locationY > Main.WINDOW_HEIGHT + height) {
            vanish();
        }
    }
}
