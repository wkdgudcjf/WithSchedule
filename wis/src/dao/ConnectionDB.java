package dao;
import java.sql.*;

public class ConnectionDB {
	private boolean DEBUG_MODE=false;
	static private Connection conn = null;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	
	private String url = "jdbc:oracle:thin:@oracle.hysick2065.cafe24.com:1521:orcl";
	private String id = "hysick2065";
	private String pwd = "dudtlr2065";
	
	public ConnectionDB(){
		try {
			Class.forName(driver);
			if(DEBUG_MODE){
				url = "jdbc:oracle:thin:@192.168.0.143:1521:XE";
				id = "scott";
				pwd = "tiger";
			}
			conn=DriverManager.getConnection(url,id,pwd);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public ConnectionDB(String driver,String url, String id, String pwd){
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,id,pwd);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e){
			e.printStackTrace();
		}		
	}

	
	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getId() {
		return id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * DB占쏙옙 占쏙옙占쏙옙占�Connection 占쏙옙체占쏙옙 占쏙옙환占싼댐옙. 
	 * @return
	 */
	public Connection getConn(){
		return conn;
	}
}
