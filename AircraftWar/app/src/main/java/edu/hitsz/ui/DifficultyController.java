package edu.hitsz.ui;

import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.enums.SceneEnums;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class DifficultyController {
    private final SceneManager manager= SceneManager.getInstance();
    @FXML
    private Button Peaceful;

    @FXML
    private Button Easy;

    @FXML
    private Button Normal;

    @FXML
    private Button Hard;

    @FXML
    private void initialize() {
        Button selected = switch (manager.getDifficulty()) {
            case PEACEFUL -> Peaceful;
            case EASY -> Easy;
            case NORMAL -> Normal;
            case HARD -> Hard;
        };
        selected.getStyleClass().add("selected-mode");
    }

    @FXML
    public void setDifficultyPeaceful() throws IOException {
        manager.setDifficulty(DifficultyEnums.PEACEFUL);
        returnToInitialScene();
    }

    @FXML
    public void setDifficultyEasy() throws IOException {
        manager.setDifficulty(DifficultyEnums.EASY);
        returnToInitialScene();
    }

    @FXML
    public void setDifficultyNormal() throws IOException {
        manager.setDifficulty(DifficultyEnums.NORMAL);
        returnToInitialScene();
    }

    @FXML
    public void setDifficultyHard() throws IOException {
        manager.setDifficulty(DifficultyEnums.HARD);
        returnToInitialScene();
    }

    @FXML
    public void returnToInitialScene() throws IOException {
        manager.switchTo(SceneEnums.INITIAL_CHOICE);
    }




}
