package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO extends DAO {

	int lastNo;
	
	public ScheduleDAO() {
		
	}

	public boolean insert(int no,String title,String startTime,String memo,int type,String isOpen,String isPublic) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "schedule"
					+ " (no,title,starttime,memo,type,isOpen,isPublic) values(?,?,?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.setString(2, title);		
			pstmt.setString(3, startTime);
			pstmt.setString(4, memo);
			pstmt.setInt(5,type);
			pstmt.setString(6,isOpen);
			pstmt.setString(7,isPublic);

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
	
	public boolean insert(Schedule schedule) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "schedule"
					+ " (no,title,starttime,memo,type,isOpen,isPublic) values(?,?,?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, schedule.getNo());
			pstmt.setString(2, schedule.getTitle());		
			pstmt.setString(3, schedule.getStartTime());
			pstmt.setString(4, schedule.getMemo());
			pstmt.setInt(5,schedule.getType());
			pstmt.setString(6,schedule.getIsOpen());
			pstmt.setString(7,schedule.getIsPublic());

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
	
	public boolean delete(int no){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try 
		{
			String query = "delete from "+"schedule"+ " where no=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,no);
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
	
	
	public List<Schedule> searchAll()
	{
		ArrayList<Schedule> list = new ArrayList<Schedule>();
		PreparedStatement pstmt = null;
		int lastNo=0;
		
		try
		{
			String query = "select * from "+"schedule";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String memo = rs.getString("memo");
				String startTime = rs.getString("starttime");
				int type = rs.getInt("type");
				String isOpen = rs.getString("isopen");
				String isPublic = rs.getString("ispublic");
				
				Schedule schedule = new Schedule(no, title, memo, startTime, type, isOpen, isPublic);				
				
				//obj.getAddressManager().setAddressList(addressList);
				list.add(schedule);
				
				if(lastNo < no){
					lastNo=no;
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		this.lastNo=lastNo;
		
		return list;
	}
	
	public void deleteAll(){
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "delete from "+"schedule";
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
	
	public class Schedule {
		final static int GENERAL = 1;
		final static int WEEK_REPEAT = 2;
		final static int MONTH_LAST = 3;
		final static int MONTH_REPEAT = 4;
		final static int YEAR_REPEAT = 5;
		private int no;
		private String title;
		private String memo;
		private String startTime;
		private int type;
		private String isOpen;
		private String isPublic;
		
		
		public Schedule(){
			
		}
		
		public Schedule(int no, String title, String memo, String startTime,
				int type, String isOpen,String isPublic) {
			super();
			this.no = no;
			this.title = title;
			this.memo = memo;
			this.startTime = startTime;
			this.type = type;
			this.isOpen = isOpen;
			this.isPublic = isPublic;
		}
		
		

		public Schedule(String title, String memo, String startTime, int type,
				String isOpen, String isPublic) {
			super();
			this.title = title;
			this.memo = memo;
			this.startTime = startTime;
			this.type = type;
			this.isOpen = isOpen;
			this.isPublic = isPublic;
		}

		public int getNo() {
			return no;
		}

		public String getTitle() {
			return title;
		}

		public String getMemo() {
			return memo;
		}

		public String getStartTime() {
			return startTime;
		}

		public int getType() {
			return type;
		}

		public String getIsOpen() {
			return isOpen;
		}

		public String getIsPublic() {
			return isPublic;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setIsOpen(String isOpen) {
			this.isOpen = isOpen;
		}

		public void setIsPublic(String isPublic) {
			this.isPublic = isPublic;
		}

		@Override
		public String toString() {
			return "Schedule [no=" + no + ", title=" + title + ", memo=" + memo
					+ ", startTime=" + startTime + ", endTime="  
					+ ", alramSet=" +  ", alramTime=" 
					+ ", type=" + type + ", isOpen=" + isOpen + ", isPublic="
					+ isPublic + "]";
		}

		
			
	}

}
