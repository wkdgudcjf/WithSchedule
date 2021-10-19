package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FriendRequestDAO extends DAO {

	public FriendRequestDAO() {

	}

	public boolean insert(int senderId, int receiverId) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "friendrequest"
					+ " (sender_id,receiver_id) values(?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, senderId);
			pstmt.setInt(2, receiverId);

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
	
	public boolean insert(FriendRequest request) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "friendrequest"
					+ " (sender_id,receiver_id) values(?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, request.getSenderId());
			pstmt.setInt(2, request.getReceiverId());
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
	public boolean delete(int senderId,int receiverId){
		PreparedStatement pstmt = null;
		int rowNum=0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		try 
		{
			String query = "delete from "+"friendrequest"+ " where receiver_id=? and sender_id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,receiverId);
			pstmt.setInt(2,senderId);
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
	
	
	public List<FriendRequest> searchAll()
	{
		ArrayList<FriendRequest> list = new ArrayList<FriendRequest>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"friendrequest";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int senderId = rs.getInt("sender_id");
				int receiverId = rs.getInt("receiver_id");
				FriendRequest obj = new FriendRequest(senderId, receiverId);
				
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
			String query = "delete from "+"friendrequest";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.executeUpdate();		
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public class FriendRequest {
		
		private int senderId;
		private int receiverId;
		public FriendRequest(int senderId, int receiverId) {
			super();
			this.senderId = senderId;
			this.receiverId = receiverId;
		}
		public int getSenderId() {
			return senderId;
		}
		public int getReceiverId() {
			return receiverId;
		}
		public void setSenderId(int senderId) {
			this.senderId = senderId;
		}
		public void setReceiverId(int receiverId) {
			this.receiverId = receiverId;
		}		
		@Override
		public String toString() {
			return "FriendRequest [senderId=" + senderId
					+ ", receiverId=" + receiverId + "]";
		}		
		
	}
}
