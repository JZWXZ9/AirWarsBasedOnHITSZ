package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEncounter;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.GameResultHandler;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.enemySpawnStrategy.AbstractSpawnStrategy;
import edu.hitsz.enemySpawnStrategy.BossSpawn;
import edu.hitsz.enemySpawnStrategy.NormalSpawn;
import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.enums.MusicEnums;
import edu.hitsz.facroty.EnemyGenerator;
import edu.hitsz.musicManager.MusicThread;
import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.BombProps;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 抽象游戏类 - 模板模式
 * 定义游戏主循环的算法骨架，具体参数由子类决定
 */
public abstract class AbstractGame extends Pane {
    protected int backGroundTop = 0;
    protected final int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemyAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProps> props;
    protected final List<AbstractEnemyAircraft> bossList;

    Iterable<AbstractEnemyAircraft> allEnemies;

    protected final Canvas canvas;
    protected final GraphicsContext gc;
    protected Image backGroundImage;

    protected AnimationTimer timer;
    protected long lastNanoTime = 0L;
    protected long nanoAccumulator = 0L;

    protected Random random = new Random();
    protected MusicThread musicThread;

    protected int score = 0;
    protected boolean gameOverFlag = false;
    protected int bossCounter = 0;

    protected AbstractSpawnStrategy enemyGenerator;
    protected int enemySpawnCounter = 0;

    protected int heroShootCounter = 0;

    // ==================== 难度参数（子类必须配置） ====================
    protected int enemyMaxNumber;           // 屏幕最大敌机数量
    protected double enemySpawnCycle;       // 敌机生成周期（帧数）
    protected double heroShootCycle;        // 英雄射击周期（帧数）
    protected int bossSpawnScore;           // Boss出现所需分数
    protected boolean allowBoss;            // 是否允许生成Boss
    protected DifficultyEnums difficulty;        // 难度名称
    protected int difficultyFactor = 1;


    public AbstractGame() {
        setPrefSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        setPickOnBounds(true);

        canvas = new Canvas(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);

        heroAircraft = HeroAircraft.getInstance();
        heroAircraft.resetForNewRun();
        EnemyGenerator.getInstance().resetForNewRun();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        bossList = new LinkedList<>();

        new HeroController(this, heroAircraft);

        // 初始化难度参数（子类实现）
        initDifficultyParams();

        // 初始化敌人生成器
        enemyGenerator = new NormalSpawn();

        // 启动背景音乐
        startBackgroundMusic();

        printDifficultyInfo();
    }

    protected abstract void initDifficultyParams();

    protected void updateDifficultyProgression() {
        // 简单/和平难度不递进
    }

    public final void action() {
        if (timer != null) {
            return;
        }

        timer =  new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOverFlag) {
                    stop();
                    return;
                }
                if (lastNanoTime == 0L) {
                    lastNanoTime = now;
                    return;
                }

                long delta = now - lastNanoTime;
                lastNanoTime = now;
                nanoAccumulator += delta;

                long stepNanos = timeInterval * 1_000_000L;
                while (nanoAccumulator >= stepNanos && !gameOverFlag) {
                    nanoAccumulator -= stepNanos;
                    tick();
                }
                render();
            }
        };
        timer.start();
    }

    protected void tick() {
        updateDifficultyProgression();

        enemySpawnCounter++;
        if (enemySpawnCounter >= enemySpawnCycle && enemyAircrafts.size() + bossList.size() < enemyMaxNumber) {
            generateEnemy();
            enemySpawnCounter = 0;
        }

        // 射击
        shootAction();

        // 移动
        bulletsMoveAction();
        aircraftMoveAction();
        propsMoveAction();

        // 碰撞检测
        crashCheckAction();

        // 无敌帧控制
        heroAircraft.invincibleController();

        // 清理无效对象
        postProcessAction();

        // 游戏结束检查
        checkGameOver();
    }

    protected void shootAction() {
        heroShootCounter++;
        if (heroShootCounter >= heroShootCycle) {
            heroShootCounter = 0;
            heroBullets.addAll(heroAircraft.shoot());
        }

        for (AbstractEnemyAircraft enemy : getAllEnemies()) {
            enemyBullets.addAll(enemy.shootByTick());
        }
    }

    protected void bulletsMoveAction() {
        heroBullets.forEach(BaseBullet::forward);
        enemyBullets.forEach(BaseBullet::forward);
    }

    protected void aircraftMoveAction() {
        getAllEnemies().forEach(AbstractEnemyAircraft::forward);
    }

    protected void propsMoveAction() {
        props.forEach(AbstractProps::forward);
    }

    protected void crashCheckAction() {
        // 敌机子弹击中英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid() || heroAircraft.isInvincible()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                heroAircraft.setInvincible(true);
                heroAircraft.setExpectedInvincibleTime(40);
                bullet.vanish();
                playSound(MusicEnums.BULLET_HIT);
            }
        }

        // 英雄子弹击中敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;

            for (AbstractEnemyAircraft enemy : getAllEnemies()) {
                if (enemy.notValid()) continue;

                if (enemy.crash(bullet)) {
                    enemy.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    playSound(MusicEnums.BULLET_HIT);

                    if (enemy.notValid()) {
                        score += enemy.getScore();
                        props.addAll(enemy.getPropsIfVanish());
                    }
                }

                // 英雄与敌机相撞
                if (enemy.crash(heroAircraft) || heroAircraft.crash(enemy)) {
                    enemy.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 道具拾取
        for (AbstractProps prop : props) {
            if (prop.crash(heroAircraft)) {
                prop.functionOnEnemy(getAllEnemies(), enemyBullets);
                prop.functionOnHero();

                if (prop instanceof BombProps) {
                    for (AbstractEnemyAircraft enemy : getAllEnemies()) {
                        if (enemy.notValid()) {
                            score += enemy.getScore();
                        }
                    }
                }

                prop.vanish();
                playSound(MusicEnums.GET_SUPPLY);
            }
        }
    }

    protected void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        bossList.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractProps::notValid);
    }

    protected void checkGameOver() {
        if (heroAircraft.getHp() <= 0 && !gameOverFlag) {
            gameOverFlag = true;

            if (musicThread != null) {
                musicThread.forceStop();
            }
            playSound(MusicEnums.GAME_OVER);

            GameResultHandler.handleGameOver(this.score, this.difficulty);
        }
    }


    protected void generateEnemy() {
        // 检查是否需要生成Boss
        if (allowBoss && score >= bossSpawnScore && score / bossSpawnScore > bossCounter) {
            enemyGenerator = new BossSpawn(this.difficulty, bossCounter + 1);
            bossCounter++;
            switchToBossMusic();
        } else {
            if (!(enemyGenerator instanceof NormalSpawn) && !isBossMode()) {
                enemyGenerator = new NormalSpawn();
                switchToNormalMusic();
            }
        }

        List<AbstractEnemyAircraft> result = enemyGenerator.spawnEnemy(difficultyFactor);
        for (AbstractEnemyAircraft enemy : result) {
            if (enemy instanceof BossEncounter) {
                bossList.add(enemy);
            } else {
                enemyAircrafts.add(enemy);
            }
        }
    }

    // ==================== 辅助方法 ====================

    protected List<AbstractEnemyAircraft> getAllEnemies() {
        List<AbstractEnemyAircraft> all = new LinkedList<>();
        all.addAll(enemyAircrafts);
        all.addAll(bossList);
        return all;
    }

    protected boolean isBossMode() {
        return !bossList.isEmpty();
    }

    protected void playSound(MusicEnums music) {
        MusicThread sound = new MusicThread(music.getPath(), false);
        sound.start();
    }

    protected void startBackgroundMusic() {
        musicThread = new MusicThread(MusicEnums.BGM.getPath(), true);
        musicThread.start();
    }

    protected void switchToBossMusic() {
        if (musicThread != null) {
            musicThread.forceStop();
        }
        musicThread = new MusicThread(MusicEnums.BGM_BOSS.getPath(), true);
        musicThread.start();
    }

    protected void switchToNormalMusic() {
        if (musicThread != null) {
            musicThread.forceStop();
        }
        musicThread = new MusicThread(MusicEnums.BGM.getPath(), true);
        musicThread.start();
    }

    protected void printDifficultyInfo() {
        System.out.println("========================================");
        System.out.println("[难度] " + difficulty.getName());
        System.out.println("[参数] 最大敌机数: " + enemyMaxNumber);
        System.out.println("[参数] 敌机生成周期: " + enemySpawnCycle);
        System.out.println("[参数] 英雄射击周期: " + heroShootCycle);
        System.out.println("[参数] Boss生成分数: " + (allowBoss ? bossSpawnScore : "无Boss"));
        System.out.println("========================================");
    }

    // ==================== 渲染 ====================

    protected void render() {
        gc.clearRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        // 滚动背景
        gc.drawImage(backGroundImage, 0, backGroundTop - Main.WINDOW_HEIGHT);
        gc.drawImage(backGroundImage, 0, backGroundTop);
        backGroundTop += 1;
        if (backGroundTop == Main.WINDOW_HEIGHT) {
            backGroundTop = 0;
        }

        // 绘制对象
        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);
        paintImageWithPositionRevised(enemyAircrafts);
        paintImageWithPositionRevised(bossList);
        paintImageWithPositionRevised(props);

        // 绘制英雄机
        gc.drawImage(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2);

        // 绘制UI
        paintScoreAndLife();
        paintBossStatus();
    }

    protected void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        for (AbstractFlyingObject obj : objects) {
            Image image = obj.getImage();
            if (image == null) continue;

            if (obj instanceof BaseBullet || obj instanceof edu.hitsz.aircraft.AbstractTrackingEnemyAircraft) {
                gc.save();
                gc.translate(obj.getLocationX(), obj.getLocationY());
                gc.rotate(obj.getAngle() - 90);
                gc.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
                gc.restore();
            } else {
                gc.drawImage(image,
                        obj.getLocationX() - image.getWidth() / 2,
                        obj.getLocationY() - image.getHeight() / 2);
            }
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 25;
        gc.setFill(Color.rgb(0, 0, 255));
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        gc.fillText("SCORE:" + this.score, x, y);
        y = y + 20;
        gc.fillText("LIFE:" + this.heroAircraft.getHp(), x, y);
        y = y + 20;
        gc.fillText("MAX_LIFE:" + this.heroAircraft.getMaxHp(), x, y);
    }

    private void paintBossStatus() {
        BossEncounter boss = null;
        for (AbstractEnemyAircraft enemy : bossList) {
            if (!enemy.notValid() && enemy instanceof BossEncounter encounter) {
                boss = encounter;
                break;
            }
        }
        if (boss == null) {
            return;
        }

        double x = Main.WINDOW_WIDTH - 240;
        double width = 224;
        double y = 28;
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 16));
        gc.setFill(Color.WHITE);
        gc.fillText(boss.getBossName(), x, y);

        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        gc.setFill(boss.isAttackTelegraphing() ? Color.GOLD : Color.LIGHTSKYBLUE);
        gc.fillText(boss.isAttackTelegraphing() ? "NEXT: " + boss.getAttackName() : boss.getAttackName(),
                x, y + 17);

        gc.setFill(Color.rgb(52, 27, 66));
        gc.fillRoundRect(x, y + 24, width, 12, 6, 6);
        gc.setFill(boss.getHealthRatio() <= 0.2 ? Color.CRIMSON : Color.MEDIUMPURPLE);
        gc.fillRoundRect(x, y + 24, width * Math.max(0.0, Math.min(1.0, boss.getHealthRatio())), 12, 6, 6);
    }


    public int getScore() {
        return score;
    }

    public String getDifficultyName() {
        return difficulty.getName();
    }
}
