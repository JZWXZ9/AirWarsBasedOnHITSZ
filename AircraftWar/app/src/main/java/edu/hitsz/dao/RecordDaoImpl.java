package edu.hitsz.dao;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据访问对象实现类
 * 实现文件存储的排行榜数据操作
 * @author hitsz
 */
public class RecordDaoImpl implements RecordDao {

    private static int globalGameCount = 0;

    // 数据文件路径
    private static final String DATA_DIR = "resources/records";
    // 采用csv文件记录数据
    private static final String FILE_EXTENSION = ".csv";

    private String difficulty;

    // 记录
    private List<Record> records;

    public RecordDaoImpl(String difficulty) {
        this.difficulty = difficulty;
        this.records = new ArrayList<>();
        // 确保数据目录存在
        ensureDataDirExists();
        // 加载数据
        loadRecordsFromFile();
    }

    // 确保数据目录存在
    private void ensureDataDirExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // 获取当前难度的数据文件路径
    private String getDataFilePath() {
        return DATA_DIR + File.separator + difficulty + FILE_EXTENSION;
    }

    public void loadRecordsFromFile() {
        String filePath = getDataFilePath();
        File file = new File(filePath);

        if (!file.exists()) {
            records = new ArrayList<>();
            return;
        }

        records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 跳过表头
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String playerName = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    String recordTime = parts[2];
                    records.add(new Record(playerName, score, recordTime));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load file: " + e.getMessage());
            records = new ArrayList<>();
        }

        records.sort(null);
        // 确保加载的数据是排好名的
        updateRanks();
        globalGameCount = records.size();
    }

    private void saveRecordsToFile() {
        String filePath = getDataFilePath();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 写入表头
            writer.write("playerName,score,recordTime");
            writer.newLine();

            for (Record record : records) {
                writer.write(String.format("%s,%d,%s",
                        record.getPlayerName(),
                        record.getScore(),
                        record.getRecordTime()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
        }
    }

    private void updateRanks() {
        records.sort(null);
        for (int i = 0; i < records.size(); i++) {
            // 排序逻辑：把Rank设置为当前编号+1
            records.get(i).setRank(i + 1);
        }
    }

    @Override
    public List<Record> getAllRecords() {
        return new ArrayList<>(records);
    }

    @Override
    public List<Record> findByPlayerName(String playerName) {
        return records.stream()
                .filter(r -> r.getPlayerName().equals(playerName))
                .collect(Collectors.toList());
    }

    @Override
    public void addRecord(Record record) {
        records.add(record);
        updateRanks();
        saveRecordsToFile();
        incrementGameCount();
    }

    @Override
    public void deleteRecord(int index) {
        if (index >= 0 && index < records.size()) {
            records.remove(index);
            updateRanks();
            saveRecordsToFile();
        }
    }

    // 取出前limit名，以防数据超出表格
    @Override
    public List<Record> getLeaderboard(int limit) {
        return records.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static int getGlobalGameCount() {
        return globalGameCount;
    }

    public static void incrementGameCount() {
        globalGameCount++;
    }
}