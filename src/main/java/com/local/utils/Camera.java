package com.local.utils;

import javax.imageio.ImageIO;
import javax.media.Manager;
import javax.media.Player;
import javax.media.protocol.DataSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class Camera {
    public static byte[] sendFrameToClient(String ip) {
        try {
            // 1. 连接摄像头 IP 视频流
            String rtspUrl = "rtsp://"+ip+"/stream";
            URL url = new URL(rtspUrl);
            DataSource dataSource = Manager.createDataSource(url);
            Player player = Manager.createPlayer(dataSource);
            player.start();
            // 2. 获取视频帧数据
            Component visualComponent = null;
            while (true) {
                if (visualComponent == null) {
                    visualComponent = player.getVisualComponent();
                }
                BufferedImage videoFrame = getVideoFrame(visualComponent);
                if (videoFrame != null) {
                    byte[] frameData = convertImageToBytes(videoFrame);
                    return frameData;
                }
                Thread.sleep(33); // 每 33 毫秒发送一帧
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] convertImageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage getVideoFrame(Component visualComponent) {
        if (visualComponent != null) {
            Dimension preferredSize = visualComponent.getPreferredSize();
            BufferedImage frame = new BufferedImage(
                    (int) preferredSize.getWidth(),
                    (int) preferredSize.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR
            );
            Graphics2D graphics = frame.createGraphics();
            visualComponent.paint(graphics);
            graphics.dispose();
            return frame;
        }
        return null;
    }

    /*private static void sendFrameToClient(byte[] frameData) {
        // 将 frameData 发送给前端的代码
        System.out.println("Sending frame to client...");
    }*/
}
