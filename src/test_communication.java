import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class test_communication {
    public static void main(String[] args) throws IOException {
// 创建 ServerSocket 并监听 8080 端口
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server is listening on port 8080");

// 接受客户端连接
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected");

// 获取客户端输入流并读取消息
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        System.out.println("Message1 received: " + message);
        ServerSocket serverSocket1 = new ServerSocket(8090);
        System.out.println("Server is listening on port 8090");

// 处理客户端连接

// 创建新的 Socket 连接,将消息发送给本地 8090 端口
        Socket anotherSocket = new Socket("localhost", 8090);
        PrintWriter out = new PrintWriter(anotherSocket.getOutputStream(), true);
        out.println(message);
        Socket clientSocket1 = serverSocket1.accept();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
        String message1 = in1.readLine();
        System.out.println("Message2 received: " + message1);
// 关闭资源
        in.close();
        out.close();
        clientSocket.close();
        anotherSocket.close();
        serverSocket.close();
        in.close();
        out.close();

        //clientsocket.close();
        //anotherSocket.close();

    }
}
