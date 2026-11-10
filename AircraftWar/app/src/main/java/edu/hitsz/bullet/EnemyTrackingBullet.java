package edu.hitsz.bullet;

import edu.hitsz.aircraft.HeroAircraft;

public class EnemyTrackingBullet extends EnemyBullet{
    private final long SPAWN_TIME;
    private static final long DURATION_TIME = 5000;
    private static final HeroAircraft hero = HeroAircraft.getInstance();
    private final double CURRENT_SPEED;

    public EnemyTrackingBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
        this.SPAWN_TIME = System.currentTimeMillis();
        this.CURRENT_SPEED = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    // 判断是否应该消失，存活时间：10s
    private boolean shouldVanish() {
        return System.currentTimeMillis() - SPAWN_TIME > DURATION_TIME;
    }

    // 更新速度和消失逻辑
    public void update() {
        if(this.shouldVanish()) {
            this.vanish();
        }
        int heroLocationX = hero.getLocationX();
        int heroLocationY = hero.getLocationY();
        double distanceOfHeroAndBullet = Math.sqrt(Math.pow((heroLocationX - this.getLocationX()), 2) +
                Math.pow((heroLocationY - this.getLocationY()), 2));
        this.speedX = (int) (CURRENT_SPEED * (heroLocationX - this.getLocationX()) / distanceOfHeroAndBullet);
        this.speedY = (int) (CURRENT_SPEED * (heroLocationY - this.getLocationY()) / distanceOfHeroAndBullet);
    }

    @Override
    public void forward() {
        update();
        super.forward();
    }

}
