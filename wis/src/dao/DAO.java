package dao;

import java.sql.*;


public abstract class DAO {
	
	protected static Connection conn;	
	
	protected DAO(){
		super();
	}
	
	static public Connection getConnection(){
		ConnectionDB db = new ConnectionDB();
		return db.getConn();
	}
	
	public void connect(){
		conn=getConnection();
		System.out.println("연결되었습니다.");
	}	
	
	public void close(){
		try {
			conn.close();
			System.out.println("정상적으로 연결이 해제되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
