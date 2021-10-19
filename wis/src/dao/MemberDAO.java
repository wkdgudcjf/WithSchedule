package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MemberDAO extends DAO {

	public MemberDAO() {

	}

	public boolean insert(int id,String phone, String email, String name,String password) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "member"
					+ " (id,phone,email,name,password) values(?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);
			pstmt.setString(4, name);
			pstmt.setString(5, password);
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
	
	public boolean insert(Member member) {
		int rowNum = 0;
		PreparedStatement pstmt = null;
		try {
			String query = "insert into " + "member"
					+ " (id,phone,email,name,password) values(?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, member.getId());
			pstmt.setString(2, member.getPhoneNum());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getName());
			pstmt.setString(5, member.getPassword());		
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
			String query = "delete from "+"member"+ " where id=?";
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
	
	public boolean modify(int id,String phoneNum,String email,String name,String password){
		PreparedStatement pstmt = null;
		int rowNum=0;
		try {
			String query = "update "+"member"+" set email=? phone=? ,name=?,password=? where id=?";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1,email);
			pstmt.setString(2,phoneNum);
			pstmt.setString(3,name);			
			pstmt.setString(4,password);
			pstmt.setInt(5,id);
			rowNum=pstmt.executeUpdate();	 
			conn.commit();
		} catch (SQLException e){
			e.printStackTrace();
		}
		if(rowNum==0) return false;
		return true;
	}
	
	public List<Member> searchAll()
	{
		ArrayList<Member> list = new ArrayList<Member>();
		PreparedStatement pstmt = null;
		
		try
		{
			String query = "select * from "+"member";
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();		
				
			while(rs.next())
			{
				int id = rs.getInt("id");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				String name = rs.getString("name");
				String password = rs.getString("password");
				Member obj = new Member(id,phone, email, name, password);
				
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
			String query = "delete from "+"member";
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
	
	public class Member {
		private int id;
		private String email;
		private String phoneNum;
		private String name;
		private String password;
		
		public Member(int id,String phoneNum, String email, String name,String password) {
			super();
			this.id=id;
			this.email = email;
			this.phoneNum = phoneNum;
			this.name = name;
			this.password=password;
		}

		public int getId() {
			return id;
		}

		public String getEmail() {
			return email;
		}

		public String getPhoneNum() {
			return phoneNum;
		}

		public String getName() {
			return name;
		}

		public String getPassword() {
			return password;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String toString() {
			return "Member [id=" + id + ", email=" + email + ", phoneNum="
					+ phoneNum + ", name=" + name + ", password=" + password
					+ "]";
		}		
				
	}

}
