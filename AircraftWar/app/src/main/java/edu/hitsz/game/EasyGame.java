package edu.hitsz.game;

import edu.hitsz.application.ImageManager;
import edu.hitsz.enums.DifficultyEnums;

public class EasyGame extends AbstractGame {

    @Override
    protected void initDifficultyParams() {
        this.enemyMaxNumber = 4;
        this.enemySpawnCycle = 28;
        this.heroShootCycle = 1.8;
        this.bossSpawnScore = 0;
        this.allowBoss = false;
        this.difficulty = DifficultyEnums.EASY;
        this.backGroundImage = ImageManager.EASY_BACKGROUND_IMAGE;
    }
}