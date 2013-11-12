package massage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.Timer;
//import java.util.TimerTask;

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
    		
    		final ChannelFuture fst = ctx.writeAndFlush("id:" + getClientId() + "\r\n");// The id must start with id:
        	fst.addListener(new ChannelFutureListener(){
        		@Override
                public void operationComplete(ChannelFuture future) {
                    assert fst == future;
                    
        	        //send commands. there must be \r\n!
        	        String input = "start/get/sell/hello/get/go/me/wll/zoo/quit/"; // start, get, sell, quit
        	        @SuppressWarnings("resource")
        			final Scanner s = new Scanner(input).useDelimiter("/");
        	        String Cmsg = new String();	
        			
        			
        	        while(s.hasNext()){
        	        	
        	        	Cmsg = s.next();
        	        	
        	        	ctx.writeAndFlush(getClientId() + ":" + Cmsg + "\r\n");
        	        	
        	        	if("quit".equals(Cmsg))
        					break;
        	        }
        			s.close();

                    
                    
//                    TimerTask task = new TimerTask() {
//            			public void run() {
//            				
//            				
//            				
//            				if(s.hasNext()){
//            					Cmsg = s.next();
//                            	
//                            	ctx.writeAndFlush(Cmsg+"\r\n");
                            	
//                            	if("quit".equals(Cmsg))
//                    				break;
//            				}
//            			}
//            		};
                    
            		
//            		Timer timer = new Timer();
//            		timer.schedule(task, 0, 2000); // send this massage after 2 seconds
//                    
//            		if(!s.hasNext()){
//            			timer.cancel();
//            			
//            		}       		
                    


                    
                    
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
