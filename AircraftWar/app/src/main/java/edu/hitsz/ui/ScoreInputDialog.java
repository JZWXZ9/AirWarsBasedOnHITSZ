package edu.hitsz.ui;

import edu.hitsz.dao.Record;
import edu.hitsz.dao.RecordDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 游戏结束时的得分记录对话框
 * 让玩家输入姓名并保存记录
 */
public class ScoreInputDialog {

    private String playerName;
    private boolean confirmed = false;

    /**
     * 显示得分记录对话框
     * @param score 最终得分
     * @param difficulty 难度
     * @return 是否确认保存
     */
    public boolean showAndWait(int score, String difficulty) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("GameOver - RecordScore");
        dialog.setResizable(false);

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #3498db);");

        // 得分显示
        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        // 恭喜信息
        Label congratsLabel = new Label("Good Game！");
        congratsLabel.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 20px;");

        // 输入区域
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Player_Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        TextField nameField = new TextField();
        nameField.setPromptText("Please Enter Your Name");
        nameField.setPrefWidth(180);
        nameField.setStyle("-fx-font-size: 14px;");

        // 默认玩家名（按顺序）
        int gameCount = RecordDaoImpl.getGlobalGameCount();
        nameField.setText("Player_" + (gameCount + 1));

        inputBox.getChildren().addAll(nameLabel, nameField);

        // 按钮区域
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save Record");
        saveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        saveButton.setOnAction(e -> {
            playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
                playerName = "Anonymous";
            }
            confirmed = true;

            // 保存记录
            RecordDaoImpl recordDao = new RecordDaoImpl(difficulty);
            Record record = new Record(playerName, score);
            recordDao.addRecord(record);

            dialog.close();
        });

        Button cancelButton = new Button("Don't save");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        cancelButton.setOnAction(e -> {
            confirmed = false;
            dialog.close();
        });

        buttonBox.getChildren().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll(scoreLabel, congratsLabel, inputBox, buttonBox);

        Scene scene = new Scene(vbox, 350, 250);
        dialog.setScene(scene);
        dialog.showAndWait();

        return confirmed;
    }

    public String getPlayerName() {
        return playerName;
    }
}