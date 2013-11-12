package massage;

public class ValidRequestHandler {
	
	private static String ClientId; // Sating login and ClientId here will cause
	// errors in the case of concurrent
	// connections.
	
	public static String getClientId() {
		return ClientId;
	}

	public void setClientId(String clientId) {
		ClientId = clientId;
	}
	
	public boolean run(String req){
		switch (req){
		case "start": return startGame();
		case "get": return getItem();
		case "sell": return sellItem();
		case "quit": return quitGame();
		default: return false;
		}
	}
	
	private static boolean startGame(){
		System.out.println(getClientId() + ": OK! Let's go!\r\n");
		return true;
	}
	
	
	private static boolean getItem(){
		System.out.println(getClientId() + ": Here is your item!\r\n");
		return true;
	}
	
	private static boolean sellItem(){
		System.out.println(getClientId() + ": OK! I will do it!\r\n");
		return true;
	}
	
	
	private static boolean quitGame(){
		System.out.println(getClientId() + ": Have a good day!\r\n");
		return true;
	}
	
}
