package edu.hitsz.musicManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/** Streams classpath audio resources and releases its audio line on stop. */
public class MusicThread extends Thread {
    private final String filename;
    private final Object lineLock = new Object();
    private URL resource;
    private AudioFormat audioFormat;
    private volatile boolean isLooping;
    private volatile boolean isRunning = true;
    private SourceDataLine dataLine;

    public MusicThread(String filename) {
        this.filename = filename;
        setDaemon(true);
        loadMusic();
    }

    public MusicThread(String filename, boolean isLooping) {
        this(filename);
        this.isLooping = isLooping;
    }

    public void startLooping() {
        isLooping = true;
    }

    public void stopLooping() {
        isLooping = false;
    }

    /** Resolve and inspect the audio header without decoding the whole track into heap memory. */
    public void loadMusic() {
        resource = getClass().getResource(filename);
        if (resource == null) {
            isRunning = false;
            System.err.println("Audio resource not found: " + filename);
            return;
        }

        try (AudioInputStream encodedStream = AudioSystem.getAudioInputStream(resource);
             AudioInputStream pcmStream = toPcmStream(encodedStream)) {
            audioFormat = pcmStream.getFormat();
        } catch (UnsupportedAudioFileException | IOException | IllegalArgumentException e) {
            isRunning = false;
            System.err.println("Could not load audio resource " + filename + ": " + e.getMessage());
        }
    }

    private static AudioInputStream toPcmStream(AudioInputStream source) {
        AudioFormat sourceFormat = source.getFormat();
        AudioFormat.Encoding encoding = sourceFormat.getEncoding();
        if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding)
                || AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding)) {
            return source;
        }

        int channels = Math.max(1, sourceFormat.getChannels());
        float sampleRate = sourceFormat.getSampleRate();
        AudioFormat pcmFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                16,
                channels,
                channels * 2,
                sampleRate,
                false);
        return AudioSystem.getAudioInputStream(pcmFormat, source);
    }

    public void play(InputStream source) {
        if (source == null) return;
        try (AudioInputStream encodedStream = AudioSystem.getAudioInputStream(source);
             AudioInputStream pcmStream = toPcmStream(encodedStream)) {
            audioFormat = pcmStream.getFormat();
            if (!playStream(pcmStream)) {
                isRunning = false;
            }
        } catch (UnsupportedAudioFileException | IOException | IllegalArgumentException e) {
            isRunning = false;
            System.err.println("Could not play audio resource " + filename + ": " + e.getMessage());
        }
    }

    public void playLooping(InputStream ignoredSource) {
        if (!isRunning || resource == null || audioFormat == null) {
            return;
        }

        do {
            try (AudioInputStream encodedStream = AudioSystem.getAudioInputStream(resource);
                 AudioInputStream pcmStream = toPcmStream(encodedStream)) {
                if (!playStream(pcmStream)) {
                    isRunning = false;
                    return;
                }
            } catch (UnsupportedAudioFileException | IOException | IllegalArgumentException e) {
                isRunning = false;
                System.err.println("Could not play audio resource " + filename + ": " + e.getMessage());
                return;
            }
        } while (isLooping && isRunning);
    }

    private boolean playStream(AudioInputStream source) {
        if (!isRunning || source == null || audioFormat == null) {
            return false;
        }

        SourceDataLine line = null;
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            int frameSize = Math.max(1, audioFormat.getFrameSize());
            int bufferSize = Math.max(8192, (int) (audioFormat.getSampleRate() * frameSize / 10));
            line.open(audioFormat, bufferSize);

            synchronized (lineLock) {
                if (!isRunning) {
                    return false;
                }
                dataLine = line;
            }

            line.start();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while (isRunning && (bytesRead = source.read(buffer, 0, buffer.length)) != -1) {
                int bytesWritten = 0;
                while (isRunning && bytesWritten < bytesRead) {
                    int written = line.write(buffer, bytesWritten, bytesRead - bytesWritten);
                    if (written <= 0) {
                        break;
                    }
                    bytesWritten += written;
                }
            }

            if (isRunning) {
                line.drain();
            }
            return isRunning;
        } catch (IOException | LineUnavailableException | IllegalArgumentException | IllegalStateException e) {
            return false;
        } finally {
            synchronized (lineLock) {
                if (dataLine == line) {
                    dataLine = null;
                }
            }
            closeLine(line);
        }
    }

    private static void closeLine(SourceDataLine line) {
        if (line == null) {
            return;
        }
        try {
            if (line.isRunning()) {
                line.stop();
            }
        } catch (IllegalStateException ignored) {
            // The line may already have been closed by forceStop().
        }
        try {
            if (line.isOpen()) {
                line.flush();
                line.close();
            }
        } catch (IllegalStateException ignored) {
            // Closing an already-closed line is harmless.
        }
    }

    public void forceStop() {
        isRunning = false;
        SourceDataLine line;
        synchronized (lineLock) {
            line = dataLine;
            dataLine = null;
        }
        closeLine(line);
        interrupt();

        if (Thread.currentThread() != this) {
            try {
                join(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        playLooping(null);
    }
}
