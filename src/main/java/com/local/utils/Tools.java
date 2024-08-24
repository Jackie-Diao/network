package com.local.utils;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class Tools {
    public static void main(String[] args) throws IOException {
        //System.out.println(turn_on_the_bulb("127.0.0.1"));
    }
    public static String turn_on_the_bulb(String ip) throws IOException {//接收灯泡IP并连接，然后发送http命令打开灯泡，并将结果（成功与否）返回
        String targetIP = "http://"+ip+"/light"; // 目标 IP
        String postData = "control=1"; // 包含 control=1 参数
        // 创建 URL 对象
        URL url = new URL(targetIP);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为 GET
        connection.setRequestMethod("POST");
        // 设置请求头
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 启用输出
        connection.setDoOutput(true);
        // 写入 POST 数据
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            outputStream.write(postDataBytes);
        }
        // 获取响应码
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return null;
        } else {
            return "Failed to turn on the lamp. Response code: " + responseCode;
        }
    }

    public static boolean reaction(int from, int to) throws IOException {
        ServerSocket serverSocket = new ServerSocket(from);
        Socket clientsocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
        String message = in.readLine();
        StringTokenizer st = new StringTokenizer(message,"_");
        String judge = st.nextToken();
        String reaction = st.nextToken();
        if(judge.equals("0")){
            Socket anotherSocket = new Socket("localhost", 8080);
            PrintWriter out = new PrintWriter(anotherSocket.getOutputStream(), true);
            out.println(message);
            sendAudio(8080,8090);
            out.close();
            anotherSocket.close();
            return true;
        }
        else if(judge.equals("1")){
            Socket anotherSocket = new Socket("localhost", 8080);
            PrintWriter out = new PrintWriter(anotherSocket.getOutputStream(), true);
            out.println(message);
            sendAudio(8080,8090);
            turn_on_the_bulb("127.0.0.1");
            out.close();
            anotherSocket.close();
            return false;
        }
        else if(judge.equals("-1")){
            Socket anotherSocket = new Socket("localhost", 8080);
            PrintWriter out = new PrintWriter(anotherSocket.getOutputStream(), true);
            out.println(message);
            sendAudio(8080,8090);
            out.close();
            anotherSocket.close();
            return false;
        }
        else{
            Camera camera = new Camera();

            return false;
        }
    }

    public static void receivesendmessage(int from, int to) throws IOException {//将语音转成的文字转发出去
        // 创建 ServerSocket 监听来自客户端的音频数据
        ServerSocket serverSocket = new ServerSocket(from);
        // 等待客户端连接
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        Socket anotherSocket = new Socket("localhost", to);
        PrintWriter out = new PrintWriter(anotherSocket.getOutputStream(), true);
        out.println(message);
        anotherSocket.close();
        //System.out.println("Message sent to another port");
    }


    public static void sendAudio(int from,int to) {
        try {
            // 创建 ServerSocket 监听来自客户端的音频数据
            ServerSocket serverSocket = new ServerSocket(from);
            //System.out.println("音频服务器已启动,等待客户端连接...");
            // 等待客户端连接
            Socket clientSocket = serverSocket.accept();
            //System.out.println("客户端已连接!");
            // 创建 ServerSocket 用于向音频接收器发送音频数据
            Socket audioReceiverConnection = new Socket("localhost", to);
            //System.out.println("已连接至音频接收器!");
            // 设置音频格式
            AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 2, true, false);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            // 从客户端读取音频数据,并写入到音频接收器
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = audioReceiverConnection.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                sourceDataLine.write(buffer, 0, bytesRead);
            }
            // 关闭资源
            sourceDataLine.drain();
            sourceDataLine.close();
            audioReceiverConnection.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static String getBulbID() {//监听8000端口，并接收灯泡的ip
        int portNumber = 8000; // 替换为你想监听的端口号
        try {
            // 创建 ServerSocket 并监听指定端口
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                // 在新的线程中处理客户端连接
                String id = handleClientConnection(clientSocket);
                if (id != null) {
                    return id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCameraID() {//监听8001端口，并接收摄像头的ip
        int portNumber = 8001; // 替换为你想监听的端口号
        try {
            // 创建 ServerSocket 并监听指定端口
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                // 在新的线程中处理客户端连接
                String id = handleClientConnection(clientSocket);
                if (id != null) {
                    return id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getListennerID() {//监听8002端口，并接收音频接收器的ip
        int portNumber = 8002; // 替换为你想监听的端口号
        try {
            // 创建 ServerSocket 并监听指定端口
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                // 在新的线程中处理客户端连接
                String id = handleClientConnection(clientSocket);
                if (id != null) {
                    return id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String handleClientConnection(Socket clientSocket) {//接收设备发送的数据
        try {
            // 读取客户端发送的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            System.out.println("Received message: " + message);
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                // 关闭客户端连接
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}