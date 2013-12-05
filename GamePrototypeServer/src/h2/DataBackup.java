package h2;

import java.util.Timer;
import java.util.TimerTask;

import org.h2.jdbcx.JdbcConnectionPool;

public class DataBackup extends Thread {
	
	public static JdbcConnectionPool cp = null;
	public H2Accessor hm = null;
	
	
	public DataBackup(H2Accessor h2m) {
		hm = h2m;
	}

	public void run() {
		
		final Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {
			public void run() {
				try {
//					hm.selectAll();
					hm.selectMultiColumns("ID,NAME");
					hm.selectMultiColumns("COUNT(*) COUNT");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};


		timer.schedule(task, 5000, 15000); //backup data for each 15s
//		timer.cancel();
	}
}
