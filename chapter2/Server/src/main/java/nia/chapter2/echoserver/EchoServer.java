package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args)
            throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() +
                    " <port>"
            );
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        // 1. 创建 EchoServerHandler 的实例（继承了 ChannelInboundHandlerAdapter）
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 2. 创建 EventLoopGroup，用于接收和处理新的连接
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 3. 创建 ServerBootstrap 实例，用于快速引导 ServerChannel（这只是一个标记接口）
            ServerBootstrap b = new ServerBootstrap();
            // 指定 parent 和 child 使用的 EventLoopGroup
            b.group(group)
                    // 指定用于创建 Channel 的类
                    .channel(NioServerSocketChannel.class)
                    // 指定绑定的地址（含端口），服务器将绑定本地的指定端口
                    .localAddress(new InetSocketAddress(port))
                    // 指定处理请求的 ChannelHandler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * Channel 注册完成事件
                         * @param ch
                         * @throws Exception
                         */
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 将之前创建的 EchoServerHandler 实例添加到 SocketChannel 实例绑定的 ChannelPipeline 末尾
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            // 异步地绑定服务器，调用 sync() 方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() +
                    " started and listening for connections on " + f.channel().localAddress());
            // 获取该 ChannelFuture 的 CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}
