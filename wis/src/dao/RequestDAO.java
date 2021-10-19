package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RequestDAO extends DAO {

	public RequestDAO() {

	}

	public boolean insert(int no, int senderId, int receiverId, int requestType) {
		if(requestType!=Request.ENTER&&requestType!=Request.INVITATION) return false;
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "request"
					+ " (no,sender_id,receiver_id,type) values(?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.setInt(2, senderId);
			pstmt.setInt(3, receiverId);
			pstmt.setInt(4, requestType);

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
	
	public boolean insert(Request request) {
		if(request.getRequestType()!=Request.ENTER&&request.getRequestType()!=Request.INVITATION) return false;
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "request"
					+ " (no,sender_id,receiver_id,type) values(?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, request.getNo());
			pstmt.setInt(2, request.getSenderId());
			pstmt.setInt(3, request.getReceiverId());
			pstmt.setInt(4, request.getRequestType());
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
	public boolean delete(String no,int senderId){
		PreparedStatement pstmt = null;
		int rowNum=0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		try 
		{
			String query = "delete from "+"request"+ " where no=? and sender_id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1,no);
			pstmt.setInt(1,senderId);
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
	
	
	public List<Request> searchAll()
	{
		ArrayList<Request> list = new ArrayList<Request>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"request";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int no = rs.getInt("no");
				int senderId = rs.getInt("sender_id");
				int receiverId = rs.getInt("receiver_id");
				int requestType = rs.getInt("type");
				Request obj = new Request(no, senderId, receiverId,requestType);
				
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
			String query = "delete from "+"request";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.executeUpdate();		
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public class Request {
		public static final int ENTER = 1;
		public static final int INVITATION = 2;
		
		private int no;
		private int senderId;
		private int receiverId;
		private int requestType;
		public Request(int no, int senderId, int receiverId,int requestType) {
			super();
			this.no = no;
			this.senderId = senderId;
			this.receiverId = receiverId;
			this.requestType=requestType;
		}
		public int getNo() {
			return no;
		}
		public int getSenderId() {
			return senderId;
		}
		public int getReceiverId() {
			return receiverId;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public void setSenderId(int senderId) {
			this.senderId = senderId;
		}
		public void setReceiverId(int receiverId) {
			this.receiverId = receiverId;
		}		
		public int getRequestType() {
			return requestType;
		}
		public void setRequestType(int requestType) {
			this.requestType = requestType;
		}
		@Override
		public String toString() {
			return "Request [no=" + no + ", senderId=" + senderId
					+ ", receiverId=" + receiverId + ", requestType="
					+ requestType + "]";
		}		
		
	}
}
