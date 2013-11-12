package massage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a server-side channel.
 */
@Sharable
public class GameServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = Logger
			.getLogger(GameServerHandler.class.getName());

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.

		ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName()
				+ "!\r\n");
		ctx.write("It is " + new Date() + " now.\r\n");
		ctx.write("Please input your id like id:20001 , and then input your command like 20001:start . \r\n");
		ctx.flush();

	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request)
			throws Exception {

		// System.out.println(ctx.channel().config() + "!\r\n");

		// Generate and write a response.

		String req = new String(RequestSorter.checkCommand(request));

		String response = new String(RequestSorter.setResponse(req));

		// We do not need to write a ChannelBuffer here.
		// We know the encoder inserted at TelnetPipelineFactory will do the
		// conversion.
		ChannelFuture future = ctx.write(response);

//		System.out.println(response);

		// Close the connection after sending 'Have a good day!'
		// if the client has sent 'quit'.
		if ("quit".equals(req)) {
			future.addListener(ChannelFutureListener.CLOSE);
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				cause);
		ctx.close();
	}

}
