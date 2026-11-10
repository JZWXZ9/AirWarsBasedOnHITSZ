package edu.hitsz.enums;

public enum SceneEnums {
    INITIAL_CHOICE(0, "/fxmls/initial.fxml"),
    DIFFICULTY_CHOICE(1,"/fxmls/difficultyChoice.fxml");


    private final int id;
    private final String path;
    SceneEnums(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }
}
