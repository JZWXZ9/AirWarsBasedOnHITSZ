package edu.hitsz.bullet;

import edu.hitsz.application.ImageManager;

/**
 * 英雄机子弹
 * @Author hitsz
 */
public class HeroBullet extends BaseBullet {

    public HeroBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
        if (ImageManager.HERO_BULLET_FACES_RIGHT) {
            // The sideways source sprite is drawn facing up for the usual shot.
            width = (int) ImageManager.HERO_BULLET_IMAGE.getHeight();
            height = (int) ImageManager.HERO_BULLET_IMAGE.getWidth();
        }
    }

}
