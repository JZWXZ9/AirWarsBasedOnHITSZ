package edu.hitsz.application;


import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.EnemyTrackingBullet;
import edu.hitsz.bullet.HeroBullet;

import edu.hitsz.props.*;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Image> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Image PEACEFUL_BACKGROUND_IMAGE;
    public static Image EASY_BACKGROUND_IMAGE;
    public static Image NORMAL_BACKGROUND_IMAGE;
    public static Image HARD_BACKGROUND_IMAGE;

    public static Image HERO_BULLET_IMAGE;
    public static Image ENEMY_BULLET_IMAGE;
    public static Image ENEMY_TRACKING_BULLET_IMAGE;

    public static Image HERO_IMAGE;
    public static Image MOB_ENEMY_IMAGE;
    public static Image ELITE_MOB_ENEMY_IMAGE;
    public static Image ELITE_PRO_MOB_ENEMY_IMAGE;
    public static Image ELITE_PLUS_MOB_ENEMY_IMAGE;
    public static Image BOSS_ENEMY_IMAGE;

    public static Image BLOOD_PROP;
    public static Image BOMB_PROP;
    public static Image BULLET_PROP;
    public static Image BULLET_PLUS_PROP;
    public static Image FREEZE_PROP;

    static {
        try {

            PEACEFUL_BACKGROUND_IMAGE = loadFromResources("/images/bg.jpg");
            EASY_BACKGROUND_IMAGE = loadFromResources("/images/bg2.jpg");
            NORMAL_BACKGROUND_IMAGE = loadFromResources("/images/bg3.jpg");
            HARD_BACKGROUND_IMAGE = loadFromResources("/images/bg4.jpg");

            HERO_IMAGE = loadFromResources("/images/hero.png");
            MOB_ENEMY_IMAGE = loadFromResources("/images/mob.png");
            ELITE_MOB_ENEMY_IMAGE = loadFromResources("/images/elite.png");
            ELITE_PRO_MOB_ENEMY_IMAGE = loadFromResources("/images/elitePro.png");
            ELITE_PLUS_MOB_ENEMY_IMAGE = loadFromResources("/images/elitePlus.png");
            BOSS_ENEMY_IMAGE = loadFromResources("/images/boss.png");

            HERO_BULLET_IMAGE = loadFromResources("/images/bullet_hero.png");
            ENEMY_BULLET_IMAGE = loadFromResources("/images/bullet_enemy.png");
            ENEMY_TRACKING_BULLET_IMAGE = loadFromResources("/images/bullet_enemy.png");

            BLOOD_PROP = loadFromResources("/images/prop_blood.png");
            BOMB_PROP = loadFromResources("/images/prop_bomb.png");
            BULLET_PROP = loadFromResources("/images/prop_bullet.png");
            BULLET_PLUS_PROP = loadFromResources("/images/prop_bulletPlus.png");
            FREEZE_PROP = loadFromResources("/images/prop_freeze.png");

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteMobEnemy.class.getName(), ELITE_MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteTrackingMobEnemy.class.getName(), ELITE_MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteProMobEnemy.class.getName(), ELITE_PRO_MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusMobEnemy.class.getName(), ELITE_PLUS_MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);

            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyTrackingBullet.class.getName(), ENEMY_TRACKING_BULLET_IMAGE);

            CLASSNAME_IMAGE_MAP.put(BloodProps.class.getName(), BLOOD_PROP);
            CLASSNAME_IMAGE_MAP.put(BulletProps.class.getName(), BULLET_PROP);
            CLASSNAME_IMAGE_MAP.put(BulletPlusProps.class.getName(), BULLET_PLUS_PROP);
            CLASSNAME_IMAGE_MAP.put(BombProps.class.getName(), BOMB_PROP);
            CLASSNAME_IMAGE_MAP.put(FreezeProps.class.getName(), FREEZE_PROP);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static Image get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Image get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }


    private static Image loadFromResources(String resourcePath) throws IOException {
        try (InputStream input = ImageManager.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new Image(input);
        }
    }
}
