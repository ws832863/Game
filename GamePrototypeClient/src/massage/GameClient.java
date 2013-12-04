package massage;

/**
 * @author DZQ
 * Simplistic Game client.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class GameClient {

    private final String host;
    private final int port;
	private static int ClientId;
    
    public GameClient(String host, int port, int id) {
        this.host = host;
        this.port = port;
        GameClient.ClientId = id;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new GameClientInitializer());
            
            GameClientInitializer.setClientId(getClientId());

            // Start the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // Read commands from the screen.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("quit".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        // Print usage if no argument is specified.
        if (args.length != 3) {
            System.err.println(
                    "Usage: " + GameClient.class.getSimpleName() +
                    " <host> <port> <client id>");
            return;
        }

        // Parse options.
        String host = args[0];
        int port = Integer.parseInt(args[1]);
       
        setClientId(Integer.parseInt(args[2]));

        new GameClient(host, port, getClientId()).run();
    }

	public static int getClientId() {
		return ClientId;
	}

	public static void setClientId(int clientId) {
		ClientId = clientId;
	}
}
