package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shootStrategy.AbstractShootStrategy;
import edu.hitsz.shootStrategy.ScatterShoot;
import edu.hitsz.shootStrategy.StraightShoot;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    @Test
    void shoot() {
        HeroAircraft aircraft = HeroAircraft.getInstance();
        List<AbstractShootStrategy> strategies = Arrays.asList(
                new StraightShoot(aircraft)
        );
        aircraft.setShootStrategy(strategies);

        List<BaseBullet> bullets = aircraft.shoot();
        assertEquals(1, bullets.size());

        strategies = Arrays.asList(
                new ScatterShoot(aircraft)
        );
        aircraft.setShootNum(3);
        aircraft.setShootStrategy(strategies);

        bullets = aircraft.shoot();
        assertEquals(3, bullets.size());
    }

    @Test
    void setShootStrategy() {
        HeroAircraft aircraft = HeroAircraft.getInstance();
        List<AbstractShootStrategy> oldStrategies = Arrays.asList(
                new StraightShoot(aircraft)
        );
        List<AbstractShootStrategy> newStrategies = Arrays.asList(
                new ScatterShoot(aircraft)
        );

        aircraft.setShootStrategy(oldStrategies);
        assertEquals(oldStrategies, aircraft.strategyList);

        aircraft.setShootStrategy(newStrategies);
        assertEquals(newStrategies, aircraft.strategyList);
    }

    @Test
    void getInstance() {
        HeroAircraft instance1 = HeroAircraft.getInstance();
        HeroAircraft instance2 = HeroAircraft.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void increaseHp() {
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.hp = hero.getMaxHp() - 10; // 假设反射或提供getter
        hero.increaseHp(20);

        assertEquals(hero.getMaxHp(), hero.hp); // 不应超过满血
    }

    @Test
    void invincibleController() {
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.setInvincible(true);
        hero.setExpectedInvincibleTime(5);

        for (int i = 0; i < 4; i++) {
            hero.invincibleController();
            assertTrue(hero.isInvincible()); // 前5帧仍然无敌
        }

        hero.invincibleController(); // 第6帧
        assertFalse(hero.isInvincible()); // 无敌结束
    }
}