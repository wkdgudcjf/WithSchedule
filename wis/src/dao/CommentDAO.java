package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO extends DAO {

	public CommentDAO() {

	}

	public boolean insert(int no,int scheduleNo, int memberId, String content, String date) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "comment_tb"
					+ " (comment_no,schedule_no,member_id,content,time) values(?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.setInt(2, scheduleNo);
			pstmt.setInt(3, memberId);
			pstmt.setString(4, content);
			pstmt.setString(5, date);

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
	
	public boolean insert(Comment comment){
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "comment_tb"
					+ " (comment_no,schedule_no,member_id,content,time) values(?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, comment.getNo());
			pstmt.setInt(2, comment.getScheduleNo());
			pstmt.setInt(3, comment.getMemberId());
			pstmt.setString(4, comment.getContent());
			pstmt.setString(5, comment.getDate());

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
	
	public boolean delete(int no,int scheduleNo,int memberId){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try 
		{
			String query = "delete from "+"comment_tb"+ " where comment_no=? and schedule_no=? and member_id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,no);
			pstmt.setInt(2,scheduleNo);
			pstmt.setInt(3,memberId);
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
	
	
	
	public List<Comment> searchAll()
	{
		ArrayList<Comment> list = new ArrayList<Comment>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"comment_tb";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int no = rs.getInt("comment_no");
				int scheduleNo = rs.getInt("schedule_no");
				int memberId = rs.getInt("member_id");
				String content = rs.getString("content");
				String date = rs.getString("time");
				
				
				Comment obj = new Comment(no,scheduleNo, memberId, content,date);
				
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
			String query = "delete from "+"comment_tb";
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
	
	public class Comment {
		private int no;
		private int scheduleNo;
		private int memberId;
		private String content;
		private String date;
		public Comment(int no, int scheduleNo, int memberId, String content,
				String date) {
			super();
			this.no = no;
			this.scheduleNo = scheduleNo;
			this.memberId = memberId;
			this.content = content;
			this.date = date;
		}
		public int getNo() {
			return no;
		}
		public int getScheduleNo() {
			return scheduleNo;
		}
		public int getMemberId() {
			return memberId;
		}
		public String getContent() {
			return content;
		}
		public String getDate() {
			return date;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public void setScheduleNo(int scheduleNo) {
			this.scheduleNo = scheduleNo;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public void setDate(String date) {
			this.date = date;
		}
		@Override
		public String toString() {
			return "Comment [no=" + no + ", scheduleNo=" + scheduleNo
					+ ", memberId=" + memberId + ", content=" + content
					+ ", date=" + date + "]";
		}
		
	}
}
