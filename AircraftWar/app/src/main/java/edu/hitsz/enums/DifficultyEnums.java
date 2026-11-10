package edu.hitsz.enums;

public enum DifficultyEnums {
    PEACEFUL(0, "Peaceful"),
    EASY(1, "Easy"),
    NORMAL(2, "Normal"),
    HARD(3, "Hard");



    private final int id;
    private final String name;

    DifficultyEnums(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
