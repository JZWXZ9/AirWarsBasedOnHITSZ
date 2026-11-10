package edu.hitsz.dao;

import java.util.List;

public interface RecordDao {

    List<Record> getAllRecords();

    // 按照玩家名查找
    List<Record> findByPlayerName(String playerName);

    // 添加游戏记录
    void addRecord(Record record);

    // 删除游戏记录
    void deleteRecord(int index);

    // 获取前Limit条数据
    List<Record> getLeaderboard(int limit);
}