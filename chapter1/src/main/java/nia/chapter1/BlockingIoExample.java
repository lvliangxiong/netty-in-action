package nia.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kerr.
 * <p>
 * Listing 1.1 Blocking I/O example
 */
public class BlockingIoExample {

    /**
     * Listing 1.1 Blocking I/O example
     */
    public void serve(int portNumber) throws IOException {
        // 创建一个绑定指定端口的 ServerSocket 对象
        ServerSocket serverSocket = new ServerSocket(portNumber);
        // ServerSocket 对象阻塞式监听请求，直到有连接请求输入
        // 此时 ServerSocket 将为发出连接请求的 Socket 创建一个专门的 Socket 对象用于消息通信，并作为 accept 方法的返回值
        Socket clientSocket = serverSocket.accept();

        // 通过 Socket 对象获取输入输出流进行消息通信
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);

        String request, response;
        while ((request = in.readLine()) != null/*阻塞式获取客户端发送的消息*/) {
            if ("Done".equals(request)) {
                break;
            }
            response = processRequest(request);
            out.println(response);
        }
    }

    private String processRequest(String request) {
        return "Processed";
    }
}
