package edu.hitsz.application;

import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.enums.SceneEnums;
import edu.hitsz.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 程序入口
 * @author hitsz & doctxing
 */
public class Main extends Application {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    public static final int BATTLE_ARENA_LEFT = 82;
    public static final int BATTLE_ARENA_TOP = 450;
    public static final int BATTLE_ARENA_RIGHT = 430;
    public static final int BATTLE_ARENA_BOTTOM = 692;

    private final SceneManager sceneManager = SceneManager.getInstance();

    public static void main(String[] args) {
        System.out.println("Aircraft War Start!");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GameResultHandler.setPrimaryStage(primaryStage);

        sceneManager.init(primaryStage);

        System.out.println("Game Started!");
    }
}
