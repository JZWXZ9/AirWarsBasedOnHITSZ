package edu.hitsz.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record implements Comparable<Record> {
    private int rank;           // 名次
    private String playerName;  // 玩家名
    private int score;          // 得分
    private String recordTime;  // 记录时间

    // 用来创建数据时候创建
    public Record(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.rank = 0;
    }

    // 用来读取数据的时候创建
    public Record(String playerName, int score, String recordTime) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = recordTime;
        this.rank = 0;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public int compareTo(Record other) {
        // 按得分降序排列
        // compare相当于比较的是a > b的结果
        // 正数说明应该在前面，负数反之，0相等
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("%-4d %-12s %-8d %s", rank, playerName, score, recordTime);
    }
}