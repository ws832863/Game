package massage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles a client-side channel.
 */
@Sharable
public class GameClientHandler extends SimpleChannelInboundHandler<String> {

	private static int ClientId;

	private static final Logger logger = Logger
			.getLogger(GameClientHandler.class.getName());

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.

		final ChannelFuture fst = ctx.writeAndFlush("id:" + getClientId()
				+ "\r\n");// The id must start with id:
		fst.addListener(new ChannelFutureListener() {

			private String cmsg;

			@Override
			public void operationComplete(ChannelFuture future) {
				assert fst == future;

				// send commands. there must be \r\n!
				// start, get, sell, quit
				String input = "start/get/sell/hello/get/go/me/wll/zoo/"; // input "quit/" to stop.
				@SuppressWarnings("resource")
				final Scanner s = new Scanner(input).useDelimiter("/");
				cmsg = new String();
				
				// send massages without schedule
//    	        while(s.hasNext()){
//    	        	
//    	        	cmsg = s.next();
//    	        	
//    	        	ctx.writeAndFlush(getClientId() + ":" + cmsg + "\r\n");
//    	        	
//    	        	if("quit".equals(cmsg))
//    					break;
//    	        }
//    			s.close();
    			
				
				
				// send massages with schedule
				final Timer timer = new Timer();

				TimerTask task = new TimerTask() {
					public void run() {

						if (s.hasNext()) {
							cmsg = s.next();

							ctx.writeAndFlush(getClientId() + ":" + cmsg
									+ "\r\n");
							
							if ("quit".equals(cmsg)) {
								timer.cancel();
								s.close();
								ctx.close();
							}

						}
					}
				};


				timer.schedule(task, 0, 2000); // send massage after 2 seconds

			}
		});

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.err.println(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				cause);
		ctx.close();
	}

	public int getClientId() {
		return ClientId;
	}

	public static void setClientId(int clientId) {
		ClientId = clientId;
	}
}
