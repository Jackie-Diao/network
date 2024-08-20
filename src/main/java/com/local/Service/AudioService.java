package com.local.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

@Service
public class AudioService {

    private static final String DEVICE_HOST = "device-host-ip"; // 设备的 IP 地址
    private static final int DEVICE_PORT = 12345; // 设备的端口号

    // 获取音频数据的实际实现
    public byte[] getAudioData() throws IOException {
        try (Socket socket = new Socket(DEVICE_HOST, DEVICE_PORT)) {
            InputStream inputStream = socket.getInputStream();

            // 假设字节数组长度已知，或者需要先读取长度信息
            // 示例中假设字节数组长度为 4096 字节
            byte[] buffer = new byte[4096];
            int bytesRead = inputStream.read(buffer);

            if (bytesRead == -1) {
                throw new IOException("从设备读取数据失败");
            }

            // 返回读取的字节数组（实际情况可能需要根据实际数据长度调整）
            return buffer;
        } catch (UnknownHostException e) {
            throw new IOException("未知代理：" + DEVICE_HOST, e);
        } catch (IOException e) {
            throw new IOException("连接设备错误", e);
        }
    }
}
