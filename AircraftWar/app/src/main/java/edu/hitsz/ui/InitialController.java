package edu.hitsz.ui;

import edu.hitsz.enums.SceneEnums;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class InitialController {
    private final SceneManager manager = SceneManager.getInstance();

    @FXML
    private Label difficultyLayout;

    @FXML
    private void initialize() {
        difficultyLayout.setText(manager.getDifficulty().getName().toUpperCase());
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
