package com.local.Service;

import com.local.utils.AudioConnectionMonitor;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

@Service
public class AudioService {

    private static final String DEVICE_HOST = "device-host-ip"; // 设备的 IP 地址
    private static final int DEVICE_PORT = 12345; // 设备的端口号

    private SourceDataLine sourceDataLine;
    private AudioConnectionMonitor monitor;

    // 获取音频数据的实际实现
    public void startStreamingAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("不支持的音频格式");
        }

        sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        monitor = new AudioConnectionMonitor(sourceDataLine);
        monitor.start();

        try (Socket socket = new Socket(DEVICE_HOST, DEVICE_PORT)) {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                sourceDataLine.write(buffer, 0, bytesRead);
            }
        } catch (UnknownHostException e) {
            throw new IOException("未知主机：" + DEVICE_HOST, e);
        } catch (IOException e) {
            throw new IOException("连接设备错误", e);
        }
    }

    public void stopStreamingAudio() {
        if (monitor != null) {
            monitor.stopMonitor();
        }
        if (sourceDataLine != null) {
            sourceDataLine.stop();
            sourceDataLine.close();
        }
    }
}
