package h2;

/**
 * @author DZQ
 * H2 accessor.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

public class H2Accessor {
	
	public static int rowNum = 100; //row >= 1
	public static int columnNum = 100; // column >= 2
	public static String tableName = new String("TEST");
	
	private static JdbcConnectionPool cp = null;
	
	public H2Accessor(JdbcConnectionPool cp){
		this.setCp(cp);
	}
	
	// create a table
	public boolean createTable() throws Exception{
		if(columnNum < 2){
			System.err.println("Please do not set columnNum less than two!");
			return false;
		}else{
			Connection conn = getCp().getConnection();
			Statement stmt = conn.createStatement();
			try{
				String order = "DROP TABLE IF EXISTS " + tableName + "; CREATE TABLE " + tableName + "(ID INT PRIMARY KEY, NAME VARCHAR(255)";
				for(int i = 1; i< columnNum-1; i++){
					order = order + ", COLUMN" + i + " VARCHAR(255)";
				}
				order = order + ");";
					
//				System.out.println(order);
					
				stmt.execute(order);
				return true;
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}finally{// no matter whether there is an Exception or not, statement in finally will be execute once before return
				stmt.close();
				conn.close();
			}
		}
		
	}
	
	
	// insert simulation values into the table
	public boolean insertValues() throws Exception{

		Connection conn = cp.getConnection();
		Statement stmt = conn.createStatement();
		
		try{
			for (int i = 1; i <= rowNum; i++) {

				String order = "";
				
				order = order + "INSERT INTO " + tableName + " VALUES (";
				
				for(int j = 1; j<= columnNum; j++){
					if (j == 1){
						order = order + i + "";
					}else{
						order = order + ",'" + (100000 + i) + "'";
					}
					
				}
				order = order + ");";
				
//				System.out.println(order);
				
				stmt.execute(order);
			}
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			stmt.close();
			conn.close();
		}
	}
	
	
	// insert one row into H2 with a specified row id
	public boolean addSpecifiedRow(String rowId) throws Exception{
		Connection conn = cp.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		
		try{		
			String queryOrder = "";
			
			queryOrder = "SELECT COUNT(ID) FROM " + tableName + " WHERE ID = " + rowId + ";";
			rs= stmt.executeQuery(queryOrder);

//			System.out.println(queryOrder);
			
			if(rs.next() && !rs.getNString(1).equals("0")){
				System.out.println("This row is already there!");
				return true;
			}else{
				String order = "";
				
				order = order + "INSERT INTO " + tableName + " VALUES (";
				
				/* if rowId is not number, here will be a problem!!!!!!*/
				for(int i = 1; i<= columnNum; i++){
					if (i == 1){
						order = order + rowId + ""; 
					}else{
						order = order + ",'" + rowId + "'";
					}
					
				}
				order = order + ");";
				stmt.execute(order);
				
//				System.out.println(order);
				return true;								
			}
							
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			rs.close();
			stmt.close();
			conn.close();
		}
	}
	
	
	// update one column in a row
	public boolean updateColumn(String rowId, String columnName, String newValue) throws Exception{
		Connection conn = cp.getConnection();
		Statement stmt = conn.createStatement();
		
		try{		
			String queryOrder = "";
			queryOrder = "UPDATE " + tableName + " SET " + columnName + " = '" + newValue + "' WHERE ID = " + rowId + ";";

//			System.out.println(queryOrder);
			
			if(stmt.executeUpdate(queryOrder) == 0){
				System.out.println("There is no such a row.");
				return false;
			}else{
				return true;								
			}
							
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			stmt.close();
			conn.close();
		}
	}
	
	
	//select one row
	public boolean selectOneRow(String rowId) throws Exception{
			
		return select("SELECT * FROM " + tableName + " WHERE ID = " + rowId + ";");
						
	}
	
	
	//select all from table
	public boolean selectAll() throws Exception{
		
		return select("SELECT * FROM " + tableName + ";");
		
	}
	
	//select one column of a row 
	public boolean selectOneColumn(String rowId, String rowName) throws Exception{
		
		return select("SELECT " + rowName + " FROM " + tableName + " WHERE ID = " + rowId + ";");
		
	}
	
	
	//select several columns of all row
	public boolean selectMultiColumns( String rowNames) throws Exception{
		
		return select("SELECT " + rowNames + " FROM " + tableName + " ;");
		
	}
		
	
	
	//select value from table
	public boolean select(String queryOrder) throws Exception{
		
		Connection conn = cp.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		try{
			rs= stmt.executeQuery(queryOrder);
			
			// print the name of each column
			ResultSetMetaData md = rs.getMetaData();
			for (int j = 1; j <= md.getColumnCount(); j++){
				System.out.print("'" + md.getColumnLabel(j) + "' ");
			}
			
			System.out.println();
			
			// print the result
	        while(rs.next()){
	        	for(int i = 1; i<= md.getColumnCount(); i++ ){
	        		System.out.print(rs.getNString(i) + " ");
	        	}
	            System.out.println();
	        }
	        return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
	        rs.close();  
	        stmt.close();  
	        conn.close();
		}
	}
	

	public static JdbcConnectionPool getCp() {
		return cp;
	}

	public void setCp(JdbcConnectionPool cp) {
		H2Accessor.cp = cp;
	}
}
