// SceneManager.java - 中介者
package edu.hitsz.ui;

import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.enums.SceneEnums;
import edu.hitsz.game.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;
    private Scene scene;
    private DifficultyEnums difficulty = DifficultyEnums.PEACEFUL;

    private SceneManager() {
    }

    // 使用懒汉式
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void init(Stage stage) throws IOException{
        this.primaryStage = stage;
        switchTo(SceneEnums.INITIAL_CHOICE);
        primaryStage.setTitle("Aircraft War");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void switchTo(SceneEnums sceneEnum) throws IOException{
            Parent root = FXMLLoader.load((getClass().getResource(sceneEnum.getPath())));
            this.scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene);
    }

    public void setDifficulty(DifficultyEnums difficulty) {
        this.difficulty = difficulty;
    }

    public DifficultyEnums getDifficulty() {
        return this.difficulty;
    }

    // 在 SceneManager.java 中修改 switchToGame 方法

    public void switchToGame() {
        AbstractGame game;

        switch (difficulty) {
            case PEACEFUL:
                game = new PeacefulGame();
                break;
            case EASY:
                game = new EasyGame();
                break;
            case NORMAL:
                game = new NormalGame();
                break;
            case HARD:
                game = new HardGame();
                break;
            default:
                game = new NormalGame();
        }

        Scene scene = new Scene((Pane) game, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        game.action();
    }

    public Scene getCurrentScene() {
        return this.scene;
    }
}
