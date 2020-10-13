package nia.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by kerr.
 * <p>
 * Listing 1.3 Asynchronous connect
 * <p>
 * Listing 1.4 Callback in action
 */
public class ConnectExample {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * Listing 1.3 Asynchronous connect
     * <p>
     * Listing 1.4 Callback in action
     */
    public static void connect() {
        Channel channel = CHANNEL_FROM_SOMEWHERE; //reference form somewhere
        // Does not block
        ChannelFuture future = channel.connect(
                new InetSocketAddress("192.168.0.1", 25));

        // ChannelFuture 对象可以看做一个异步操作的结果的占位符
        future.addListener(new ChannelFutureListener() {
            /**
             * 此方法将在 future 对应的异步操作已经完成时，被回调
             * @param future
             */
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    ByteBuf buffer = Unpooled.copiedBuffer(
                            "Hello", Charset.defaultCharset());
                    ChannelFuture wf = future.channel()
                            .writeAndFlush(buffer);
                    // ...
                } else {
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });

    }
}