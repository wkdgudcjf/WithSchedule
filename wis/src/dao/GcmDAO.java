package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dto.GcmInfo;


public class GcmDAO extends DAO {

	public GcmDAO() {

	}

	public boolean insert(int id,String gcmid) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "gcm"
					+ " (id,gcmid) values(?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.setString(2, gcmid);
			rowNum = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rowNum == 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean insert(GcmInfo gcm) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "gcm"
			+ " (id,gcmid) values(?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, gcm.getMemberId());
			pstmt.setString(2, gcm.getGcmId());	
			rowNum = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rowNum == 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean delete(int id){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try 
		{
			String query = "delete from "+"gcm"+ " where id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,id);
			rowNum=pstmt.executeUpdate();	
			conn.commit();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		if(rowNum==0)
		{
			return false;
		}
		return true;
	}
	
	public boolean modify(int id,String gcmid){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try {
			String query = "update "+"gcm"+" set gcmid=? where id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1,gcmid);
			pstmt.setInt(2,id);
			rowNum=pstmt.executeUpdate();	 
			conn.commit();
		} catch (SQLException e){
			e.printStackTrace();
		}
		if(rowNum==0) return false;
		return true;
	}
	
	public ArrayList<GcmInfo> searchAll()
	{
		ArrayList<GcmInfo> list = new ArrayList<GcmInfo>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"gcm";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int id = rs.getInt("id");
				String gcmid = rs.getString("gcmid");
				GcmInfo obj = new GcmInfo(id,gcmid);
				
				//obj.getAddressManager().setAddressList(addressList);
				list.add(obj);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public void deleteAll(){
		PreparedStatement pstmt = null;		
		try
		{
			String query = "delete from "+"gcm";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.executeUpdate();		
			conn.commit();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
