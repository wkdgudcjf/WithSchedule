package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryDAO extends DAO {

	public EntryDAO() {

	}

	public boolean insert(int no, int memberId, String isOwner) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "entry"
					+ " (no,member_id,isOwner) values(?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.setInt(2, memberId);
			pstmt.setString(3, isOwner);

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
	
	public boolean insert(Entry entry) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "entry"
					+ " (no,member_id,isOwner) values(?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, entry.getNo());
			pstmt.setInt(2, entry.getMemberId());
			pstmt.setString(3, entry.getIsOwner());

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
	
	public boolean delete(int no,int memberId){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try 
		{
			String query = "delete from "+"entry"+ " where no=? and member_id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,no);
			pstmt.setInt(2,memberId);
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
	
	public boolean modify(int no,int memberId,String isOwner){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try {
			String query = "update "+"entry"+" set isOwner=? where no=? and member_id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1,isOwner);
			pstmt.setInt(2,no);			
			pstmt.setInt(3,memberId);
			
			rowNum=pstmt.executeUpdate();	 
			conn.commit();
		} catch (SQLException e){
			e.printStackTrace();
		}
		if(rowNum==0) return false;
		return true;
	}
	
	public List<Entry> searchAll()
	{
		ArrayList<Entry> list = new ArrayList<Entry>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"entry";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int no = rs.getInt("no");
				int memberId = rs.getInt("member_id");
				String isOwner = rs.getString("isowner");
				
				Entry obj = new Entry(no, memberId, isOwner);
				
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
			String query = "delete from "+"entry";
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
	
	public class Entry {
		private int no;
		private int memberId;
		private String isOwner;
		public Entry(int no, int memberId, String isOwner) {
			super();
			this.no = no;
			this.memberId = memberId;
			this.isOwner = isOwner;
		}
		public int getNo() {
			return no;
		}
		public int getMemberId() {
			return memberId;
		}
		public String getIsOwner() {
			return isOwner;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		public void setIsOwner(String isOwner) {
			this.isOwner = isOwner;
		}
		@Override
		public String toString() {
			return "Entry [no=" + no + ", member=" + memberId + ", isOwner="
					+ isOwner + "]";
		}
		
		
	}
}
