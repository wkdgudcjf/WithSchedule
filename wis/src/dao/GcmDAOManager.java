package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import dto.GcmInfo;

public class GcmDAOManager {
	private static GcmDAOManager manager = new GcmDAOManager();
	private GcmDAO dao;
	public GcmDAOManager() {
		super();
		this.dao = new GcmDAO();
	}
	
	public static GcmDAOManager getInstance(){
		return manager;
	}
		
	public HashMap<Integer,String> loadFromDB(){
		dao.connect();
		ArrayList<GcmInfo> mlist = dao.searchAll();
		dao.close();

		HashMap<Integer,String> cmap = new HashMap<Integer,String>();
		
		for (GcmInfo r : mlist) {			
			cmap.put(r.getMemberId(), r.getGcmId());
		}
		return cmap;
	}
	
	@SuppressWarnings("rawtypes")
	public void saveToDB(HashMap<Integer,String> cmap){
		dao.connect();
		Set<Integer> keyList = cmap.keySet();
		Iterator it = keyList.iterator();
			
		while(it.hasNext()){
			int id = (Integer)it.next();
			String gcmid = cmap.get(id);
			dao.insert(id, gcmid);
		}	
		dao.close();
	}

	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
	
	public GcmDAO getDao() {
		return dao;
	}

	public void setDao(GcmDAO dao) {
		this.dao = dao;
	}
	
	
}
