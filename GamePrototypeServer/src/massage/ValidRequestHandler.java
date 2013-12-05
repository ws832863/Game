package massage;

import org.h2.jdbcx.JdbcConnectionPool;

import h2.H2Accessor;

/**
 * @author DZQ Handle a valid request from client.
 */

public class ValidRequestHandler {

	private static String ClientId; // Sating login and ClientId here will cause
	// errors in the case of concurrent
	// connections.

	public static JdbcConnectionPool cp = null;

	public static H2Accessor ha = null;

	public static String getClientId() {
		return ClientId;
	}

	public void setClientId(String clientId) {
		ClientId = clientId;
	}

	public boolean run(String req) throws Exception {

		cp = new h2.InitH2().memoryMode();
		ha = new h2.H2Accessor(cp);
		switch (req) {
		case "start":
			return startGame();
		case "get":
			return getItem();
		case "sell":
			return sellItem();
		case "quit":
			return quitGame();
		default:
			return false;
		}
	}

	private static boolean startGame() throws Exception {
//		System.out.println(getClientId() + ": OK! Let's go!\r\n");
		ha.addSpecifiedRow(getClientId());
		return true;
	}

	private static boolean getItem() throws Exception {
		System.out.println(getClientId() + ": Here is your item: \r\n");
		ha.selectOneColumn(getClientId(), "NAME", true);
		return true;
	}

	private static boolean sellItem() throws Exception {
//		System.out.println(getClientId() + ": OK! I will do it!\r\n");
		ha.updateColumn(getClientId(), "NAME", "AK");
		return true;
	}

	private static boolean quitGame() throws Exception {
//		ha.selectOneRow(getClientId());
		System.out.println(getClientId() + ": Have a good day!\r\n");
		return true;
	}

}