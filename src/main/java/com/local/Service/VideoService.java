package com.local.Service;

import com.local.utils.Camera;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VideoService {

    private static final String CAMERA_IP = "camera-ip"; // 替换为实际摄像头 IP

    // 获取视频数据的实际实现
    public byte[] getVideoData() throws IOException {
        // 从 Camera 类中获取视频帧
        byte[] videoData = Camera.sendFrameToClient(CAMERA_IP);
        if (videoData == null) {
            throw new IOException("从摄像头获取视频帧失败");
        }
        return videoData;
    }
}

