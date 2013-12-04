package h2;

/**
 * @author DZQ
 * Initialize H2
 */

import org.h2.jdbcx.JdbcConnectionPool;


public class InitH2 {
	
	public JdbcConnectionPool cp = null;
	
	public JdbcConnectionPool embeddedMode() throws ClassNotFoundException{	
		return getConecction("jdbc:h2:~/test");
	}
	
	public JdbcConnectionPool serverMode() throws ClassNotFoundException{
		return getConecction("jdbc:h2:tcp://localhost/~/test");
	}
	
	public JdbcConnectionPool memoryMode() throws ClassNotFoundException{
		return getConecction("jdbc:h2:mem:test");
	}
	
	public JdbcConnectionPool getConecction(String JDBC_URL)throws ClassNotFoundException{
		Class.forName("org.h2.Driver");
		cp = JdbcConnectionPool.create(JDBC_URL,"sa", "");
		return cp;
	}
	
}

