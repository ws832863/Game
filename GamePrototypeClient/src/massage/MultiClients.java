package massage;

//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
//import java.io.InputStreamReader;

public class MultiClients {

	private final static int ClientNum = 50;
	private static int ClientId = 10000; // It could be 10000, 20000, 30000... and 90000.
	public static void main(String[] args) {
		String path=System.getProperty("user.dir");
		//if running from command line ,remove the following line
		//path=path+File.separator+"bin";
		System.out.println(path);
		try {

			for (int i = 1; i <= ClientNum; i++) {
			ProcessBuilder pb = new ProcessBuilder("java","massage.GameClient","localhost","8080", String.valueOf(ClientId++));
			pb.directory(new File(path));
			pb.start();          
            
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}