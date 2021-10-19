package dto;

public class MemberInfo implements Comparable<Object>{
	private int id;
	private String email;
	private String phoneNum;
	private String name;
	private String password;
	
	
	public MemberInfo(String email) {
		super();
		this.email = email;
	}
	
	public MemberInfo(String phoneNum, String email, String name,String password) {
		super();
		this.email = email;
		this.phoneNum = phoneNum;
		this.name = name;
		this.password=password;
	}
	public MemberInfo(int id,String phoneNum, String email, String name,String password) {
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
		return "MemberInfo [id=" + id + ", email=" + email + ", phoneNum="
				+ phoneNum + ", name=" + name + ", password=" + password
				+ "]";
	}
	@Override
	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		MemberInfo mi = (MemberInfo)obj;
		return this.email.compareTo(mi.email);
	}		
			
}