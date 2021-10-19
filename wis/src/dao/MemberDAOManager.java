package dao;

import java.util.ArrayList;
import java.util.List;

import dao.MemberDAO.Member;
import dto.MemberInfo;

public class MemberDAOManager {
	private static MemberDAOManager manager = new MemberDAOManager();
	private MemberDAO dao;
	public MemberDAOManager() {
		super();
		this.dao = new MemberDAO();
	}
	
	public static MemberDAOManager getInstance(){
		return manager;
	}
		
	public ArrayList<MemberInfo> loadFromDB(){
		dao.connect();
		List<Member> mlist = dao.searchAll();
		dao.close();
		
		ArrayList<MemberInfo> milist = new ArrayList<MemberInfo>();
		milist.add(new MemberInfo(0, "00000000000", "noemail@wis.com", "알수없음", "#123#22"));
		for(Member m : mlist){
			MemberInfo mi = new MemberInfo(m.getId(),m.getPhoneNum(),m.getEmail(),m.getName(),m.getPassword());
			milist.add(mi);
		}
		
		return milist;
	}
	
	public void saveToDB(ArrayList<MemberInfo> milist){
		dao.connect();
		for(MemberInfo mi: milist){
			dao.insert(mi.getId(),mi.getPhoneNum(), mi.getEmail(), mi.getName(), mi.getPassword());
			System.out.println("완료");
		}		
		dao.close();
	}

	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
	
	public MemberDAO getDao() {
		return dao;
	}

	public void setDao(MemberDAO dao) {
		this.dao = dao;
	}
	
	
}
