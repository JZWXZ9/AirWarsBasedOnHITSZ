package edu.hitsz.aircraft;

import edu.hitsz.application.Main;

public abstract class AbstractTrackingEnemyAircraft extends AbstractEnemyAircraft{
    private boolean isTracking = true;
    private final long SPAWN_TIME;
    private static final long TRACKING_TIME = 1800; // 1.8秒
    static final HeroAircraft HERO_AIRCRAFT = HeroAircraft.getInstance();
    private final double TOTAL_SPEED;
    private final int BULLET_SPEED = 20;
    private int trackingBulletCounter;
    private final static int TRACKING_BULLET_CYCLE = 3;

    public AbstractTrackingEnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.SPAWN_TIME = System.currentTimeMillis();
        this.TOTAL_SPEED = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    public boolean keepTracking() {
        if(System.currentTimeMillis() - SPAWN_TIME >= TRACKING_TIME) {
            isTracking = false;
        }
        return isTracking;
    }

    @Override
    public int getTrackingBulletCycle() {
        return TRACKING_BULLET_CYCLE;
    }

    @Override
    public void forward() {
        // 更新速度
        if (keepTracking()) {
            double xDistance = HERO_AIRCRAFT.getLocationX() - this.getLocationX();
            double yDistance = HERO_AIRCRAFT.getLocationY() - this.getLocationY();
            double totalDistance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
            speedX = (int) (TOTAL_SPEED * xDistance / totalDistance);
            speedY = (int) (TOTAL_SPEED * yDistance / totalDistance);
        }
        super.forward();
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH || locationY >= Main.WINDOW_HEIGHT) {
            // 横向超出边界后反向
            vanish();
        }
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }

    }
}
