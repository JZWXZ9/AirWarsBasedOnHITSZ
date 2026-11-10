package edu.hitsz.ui;

import edu.hitsz.application.Main;
import edu.hitsz.dao.Record;
import edu.hitsz.dao.RecordDaoImpl;
import edu.hitsz.enums.DifficultyEnums;
import edu.hitsz.enums.SceneEnums;
import edu.hitsz.game.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TableController {

    @FXML
    private Label difficultyLabel;

    @FXML
    private TableView<Record> scoreTable;

    @FXML
    private TableColumn<Record, Integer> rankColumn;

    @FXML
    private TableColumn<Record, String> nameColumn;

    @FXML
    private TableColumn<Record, Integer> scoreColumn;

    @FXML
    private TableColumn<Record, String> timeColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    private RecordDaoImpl recordDao;
    private DifficultyEnums currentDifficulty;
    private ObservableList<Record> recordList;
    private Stage primaryStage;

    @FXML
    private void initialize() {
        // 初始化表格列
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("recordTime"));

        // 设置表格样式
        scoreTable.setPlaceholder(new Label("Talk is cheap, show me your RECORD！"));
    }

    public void setDifficulty(DifficultyEnums difficulty) {
        this.currentDifficulty = difficulty;
        difficultyLabel.setText("Current Difficulty: " + difficulty.getName());

        // 初始化DAO
        recordDao = new RecordDaoImpl(difficulty.getName());

        // 加载数据
        refreshLeaderboard();
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }


    @FXML
    public void refreshLeaderboard() {
        if (recordDao == null) return;

        List<Record> records = recordDao.getAllRecords();
        recordList = FXCollections.observableArrayList(records);
        scoreTable.setItems(recordList);

        // 更新排名显示
        for (int i = 0; i < recordList.size(); i++) {
            recordList.get(i).setRank(i + 1);
        }
        scoreTable.refresh();
    }

    @FXML
    public void deleteRecord() {
        Record selectedRecord = scoreTable.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert("Warning", "Please Choose the target first！", Alert.AlertType.WARNING);
            return;
        }

        // 确认删除对话框
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm");
        confirmAlert.setHeaderText("Delete Record");
        confirmAlert.setContentText("You sure to delete \"" + selectedRecord.getPlayerName() +
                "\" 's " + selectedRecord.getScore() + " score？");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int index = recordList.indexOf(selectedRecord);
                recordDao.deleteRecord(index);
                refreshLeaderboard();
                showAlert("Succeed", "One record has been deleted！", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}