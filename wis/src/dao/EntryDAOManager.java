package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dao.EntryDAO.Entry;
import dto.EntryInfo;

public class EntryDAOManager {
	private static EntryDAOManager manager = new EntryDAOManager();
	private EntryDAO dao;
	public EntryDAOManager() {
		super();
		this.dao = new EntryDAO();
	}
	
	public static EntryDAOManager getInstance(){
		return manager;
	}
		
	public HashMap<Integer,ArrayList<EntryInfo>> loadFromDB(){
		dao.connect();
		List<Entry> elist = dao.searchAll();
		dao.close();

		HashMap<Integer,ArrayList<EntryInfo>> emap = new HashMap<Integer, ArrayList<EntryInfo>>();
		
		for (Entry e : elist) {
			int no = e.getNo();
			EntryInfo ei = new EntryInfo(e.getMemberId(),e.getIsOwner().equals("true")?true:false);
			
			if(emap.get(no)==null){
				emap.put(no, new ArrayList<EntryInfo>());
			}
			emap.get(no).add(ei);
		}
		return emap;
	}
	
	@SuppressWarnings("rawtypes")
	public void saveToDB(HashMap<Integer,ArrayList<EntryInfo>> emap){
		dao.connect();
		Set<Integer> keyList = emap.keySet();
		Iterator it = keyList.iterator();
			
		while(it.hasNext()){
			int no = (Integer)it.next();
			ArrayList<EntryInfo> EntryList = emap.get(no);
			for(EntryInfo ei : EntryList){
				dao.insert(no, ei.getMemberId(), ei.getIsOwner() ?"true":"false");
			}
		}
		dao.close();
	}

	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
	
	public EntryDAO getDao() {
		return dao;
	}

	public void setDao(EntryDAO dao) {
		this.dao = dao;
	}
	
	
}
