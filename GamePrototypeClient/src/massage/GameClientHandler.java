package massage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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

	private static int cmsgNum = 50; // the number of sent requests will be
										// cmsgNum + 2
	private static double ProbOfSell = 0.6; // set the probability of "sell" in
											// the massage
	private static int count = 0;

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		
		// to make sure that the id will be sent there at first.
		final ChannelFuture fst = ctx.writeAndFlush("id:" + getClientId()
				+ "\r\n");// The id must start with id:
		fst.addListener(new ChannelFutureListener() {

			private String cmsg = "";

			@Override
			public void operationComplete(ChannelFuture future) {
				assert fst == future;

				// send commands. there must be \r\n!
				// start, get, sell, quit
								
				// do not worry about the massage missing of "start"
				// when there is a get/sell and the row does not exist
				// the server will insert the row firstly
				
            	// send massages with schedule
        		final Timer timer = new Timer();
        		TimerTask task = new TimerTask() {
        			public void run() {
        				if(count == 0){
        					cmsg = "start";
        				}else if (count == cmsgNum + 1) {
        					cmsg = "quit";
        					timer.cancel();
        				} else
        					cmsg = generateInputMassage();

        				count++;
        				// send massages to server
        				ctx.writeAndFlush(getClientId() + ":" + cmsg + "\r\n");
        				System.out.println(getClientId() + ":" + cmsg + "\r\n");
        				// if ("quit".equals(cmsg)) {
        				// timer.cancel();
        				// }
        			}
        		};

        		timer.schedule(task, 2000, 2000); // after 0s send massages for
        										// each 2s
				
			}
		});

	}


	// generate one random massage
	public String generateInputMassage() {
		double rd = Math.random();
//		System.out.println(rd);
		if (rd < ProbOfSell) // [0,10000)
			return "sell";
		else
			return "get";
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.err.println(msg);

		if ("quit".equals(msg)) {
			ctx.close();
			System.exit(0);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				cause);
		ctx.close();
		System.exit(0);
	}

	public int getClientId() {
		return ClientId;
	}

	public static void setClientId(int clientId) {
		ClientId = clientId;
	}
}
