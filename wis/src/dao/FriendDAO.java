package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FriendDAO extends DAO {

	public FriendDAO() {

	}

	public boolean insert(int myId, int friendId,String hasPhoneNum) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "friend"
					+ " (my_id,friend_id,has_phone_num) values(?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, myId);
			pstmt.setInt(2, friendId);
			pstmt.setString(3, hasPhoneNum);

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
	
	public boolean insert(Friend friend) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "friend"
					+ " (my_id,friend_id,has_phone_num) values(?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, friend.getMyId());
			pstmt.setInt(2, friend.getFriendId());
			pstmt.setString(3, friend.getHasPhoneNum());

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
	public boolean delete(int myId,int friendId){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try 
		{
			String query = "delete from "+"friend"+ " where my_id=? and friend_id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,myId);
			pstmt.setInt(2,friendId);
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
	
	
	public List<Friend> searchAll()
	{
		ArrayList<Friend> list = new ArrayList<Friend>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"friend";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int myId = rs.getInt("my_id");
				int friendId = rs.getInt("friend_id");
				String hasPhoneNum = rs.getString("has_phone_num");
				Friend obj = new Friend(myId,friendId,hasPhoneNum);				
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
			String query = "delete from "+"friend";
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
	
	public class Friend {
		private int myId;
		private int friendId;
		private String hasPhoneNum;
		
		public Friend(int myId, int friendId,String hasPhoneNum) {
			super();
			this.myId = myId;
			this.friendId = friendId;
			this.hasPhoneNum = hasPhoneNum;
		}

		public int getMyId() {
			return myId;
		}

		public int getFriendId() {
			return friendId;
		}

		public void setMyId(int myId) {
			this.myId = myId;
		}

		public void setFriendId(int friendId) {
			this.friendId = friendId;
		}

		
		public String getHasPhoneNum() {
			return hasPhoneNum;
		}

		public void setHasPhoneNum(String hasPhoneNum) {
			this.hasPhoneNum = hasPhoneNum;
		}

		@Override
		public String toString() {
			return "Friend [myId=" + myId + ", friendId=" + friendId
					+ ", hasPhoneNum=" + hasPhoneNum + "]";
		}

		
	}
}
