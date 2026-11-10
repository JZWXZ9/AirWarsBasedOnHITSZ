package edu.hitsz.application;

import edu.hitsz.dao.Record;
import edu.hitsz.dao.RecordDaoImpl;
import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.ui.ScoreInputDialog;
import edu.hitsz.ui.TableController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameResultHandler {

    private static int globalGameCount = 0;
    private static Stage primaryStage;  // 保存主舞台引用

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void handleGameOver(int score, DifficultyEnums difficulty) {

        System.out.println("\n========== Game Over! ==========");
        System.out.println("Current Score: " + score);

        // 增加游戏计数
        incrementGameCount();

        // 使用JavaFX线程显示输入对话框和排行榜
        Platform.runLater(() -> {
            // 显示姓名输入对话框
            ScoreInputDialog dialog = new ScoreInputDialog();
            boolean saved = dialog.showAndWait(score, difficulty.getName());

            if (saved) {
                System.out.println("Score Saved！Player: " + dialog.getPlayerName());
            } else {
                System.out.println("Filed to save score。");
            }

            // 跳转到排行榜界面
            showLeaderboardScreen(difficulty);
        });
    }

    private static void showLeaderboardScreen(DifficultyEnums difficulty) {
        if (primaryStage == null) {
            System.err.println("Error: primaryStage is null! Call setPrimaryStage() first.");
            return;
        }

        try {
            // 加载排行榜FXML
            FXMLLoader loader = new FXMLLoader(
                    GameResultHandler.class.getResource("/fxmls/scoreTable.fxml")
            );
            Parent root = loader.load();

            // 获取控制器并设置难度
            TableController controller = loader.getController();
            controller.setDifficulty(difficulty);
            controller.setStage(primaryStage);

            // 创建新场景并切换
            Scene scene = new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Aircraft War - Leaderboard");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load rank table: " + e.getMessage());
        }
    }

    public static void incrementGameCount() {
        globalGameCount++;
    }

    public static int getGlobalGameCount() {
        return globalGameCount;
    }
}