package edu.hitsz.ui;

import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.enums.SceneEnums;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class InitialController {
    private final SceneManager manager = SceneManager.getInstance();

    @FXML
    private Label difficultyLayout;

    @FXML
    private Button chooseDifficulty;

    @FXML
    private Button start;

    @FXML
    private Button settings;

    @FXML
    private void initialize() {
        difficultyLayout.setText("CurrentDifficulty: " +  manager.getDifficulty().getName());
    }

    @FXML
    public void switchToDifficultyChoice() throws IOException {
        manager.switchTo(SceneEnums.DIFFICULTY_CHOICE);
    }

    @FXML
    public void switchToGame() {
        manager.switchToGame();
    }



}
