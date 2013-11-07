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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles a server-side channel.
 */
@Sharable
public class GameServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = Logger.getLogger(GameServerHandler.class.getName());

    
    
    private boolean login = false;
    
    private static String ClientId; // Sating login and ClientId here will cause errors in the case of concurrent connections. 

    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        
    	ctx.write(
                "Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.write("Please input your id like id:***** .\r\n");
        ctx.flush();
        login = false;
        
    }
  

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
    	
        // Generate and write a response.
        String response;
        boolean close = false;

        if (request.isEmpty()) {
            response = "At your command, sir!.\r\n";
        } else if(!login){
        	Pattern pattern = Pattern.compile("^(id:)[0-9]+");
        	Matcher matcher = pattern.matcher(request.toLowerCase());
        	
        	if(matcher.find()){
        		
        		setClientId(request.substring(3, request.length()));
        		
        		response = "Sir, your id is " + getClientId() + "\r\n";
        		
                System.out.println("Welcome " + getClientId() + "!\r\n");
        		
        		login = true;
        	}else{
        		response = "Sir, you haven't logged in.\r\n";
        	}
        } else{
        	if ("start".equals(request.toLowerCase())) {
                response = "OK! Let's go!\r\n";
            } else if ("get".equals(request.toLowerCase())) {
                response = "Here is your item!\r\n";
            }else if ("sell".equals(request.toLowerCase())) {
                response = "OK! I will do it!\r\n";
            }else if ("quit".equals(request.toLowerCase())) {
                response = "Have a good day!\r\n";
                close = true;
                setClientId(null);
            }else {
                response = "Did you say '" + request + "'?\r\n";
            }
        }
        

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.
        ChannelFuture future = ctx.write(response);
        
        System.out.println(getClientId() +": "+ response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'quit'.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }
    
    public String getClientId() {
		return ClientId;
	}

	public static void setClientId(String clientId) {
		ClientId = clientId;
	}  
    
    
}
