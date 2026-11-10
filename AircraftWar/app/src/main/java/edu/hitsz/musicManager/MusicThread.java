package edu.hitsz.musicManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicThread extends Thread {

    //音频文件名
    private String filename;
    private AudioFormat audioFormat;
    private byte[] samples;
    private volatile boolean isLooping = false;
    private volatile boolean isRunning = true;  // 新增：控制线程运行状态
    private SourceDataLine dataLine = null;     // 新增：保存数据行引用

    public MusicThread(String filename) {
        //初始化filename
        this.filename = filename;
        loadMusic();
    }

    public MusicThread(String filename, boolean isLooping) {
        this(filename);
        this.isLooping = isLooping;
    }

    public void startLooping() {
        this.isLooping = true;
    }

    public void stopLooping() {
        this.isLooping = false;
    }

    public void loadMusic() {
        try {
            //定义一个AudioInputStream用于接收输入的音频数据，使用AudioSystem来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource(filename));
            //用AudioFormat来获取AudioInputStream的格式
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return samples;
    }

    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SoureDataLine是可以写入数据的数据行
        // 不再每次都创建新的dataLine，使用成员变量
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        dataLine.start();
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1 && isRunning) {
                //从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                numBytesRead = source.read(buffer, 0, buffer.length);
                //通过此源数据行将数据写入混频器
                if (numBytesRead != -1 && isRunning) {
                    dataLine.write(buffer, 0, numBytesRead);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // 确保资源被释放
            if (dataLine != null) {
                dataLine.drain();
                dataLine.close();
                dataLine = null;
            }
        }
    }

    public void playLooping(InputStream source) {
        do {
            InputStream stream = new ByteArrayInputStream(samples);
            this.play(stream);
        } while(isLooping && isRunning);
    }

    public void forceStop() {
        // 停止线程运行
        isRunning = false;
        // 立即停止音频播放
        if (dataLine != null && dataLine.isRunning()) {
            dataLine.stop();
            dataLine.flush();
            dataLine.close();
            dataLine = null;
        }
        // 中断线程
        this.interrupt();

        try {
            this.join(100);  // 等待最多100毫秒
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        InputStream stream = new ByteArrayInputStream(samples);
        playLooping(stream);
    }
}