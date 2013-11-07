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
		try {

			for (int i = 1; i <= ClientNum; i++) {
			ProcessBuilder pb = new ProcessBuilder("java","massage.GameClient","localhost","8080", String.valueOf(ClientId++));
			pb.directory(new File("C:\\Users\\diao\\Desktop\\Game\\GamePrototypeClient\\bin"));
			pb.start();
			
//			Process p = pb.start();
            
            //get echo from GameClient (not work...)
//            BufferedInputStream in = new BufferedInputStream(p..getInputStream());
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String s;
//            while ((s = br.readLine()) != null)
//            	System.out.println(s);            
            
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}