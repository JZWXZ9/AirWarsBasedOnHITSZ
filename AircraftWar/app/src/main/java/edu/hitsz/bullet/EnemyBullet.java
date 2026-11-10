package edu.hitsz.bullet;

/**
 * 敌机子弹
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet {
    // 注意敌机子弹是向下飞的，速度为负值
    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

}
