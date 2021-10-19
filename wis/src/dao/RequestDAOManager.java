package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dao.RequestDAO.Request;
import dto.RequestInfo;

public class RequestDAOManager {
	private static RequestDAOManager manager = new RequestDAOManager();
	private RequestDAO dao;
	public RequestDAOManager() {
		super();
		this.dao = new RequestDAO();
	}
	
	public static RequestDAOManager getInstance(){
		return manager;
	}
		
	public HashMap<Integer,ArrayList<RequestInfo>> loadFromDB(){
		dao.connect();
		List<Request> rlist = dao.searchAll();
		dao.close();
		
		HashMap<Integer,ArrayList<RequestInfo>> rmap = new HashMap<Integer, ArrayList<RequestInfo>>();
		
		for (Request r : rlist) {
			int no = r.getNo();
			RequestInfo ri = new RequestInfo(r.getReceiverId(),r.getSenderId(),r.getRequestType());
			
			if(rmap.get(no)==null){
				rmap.put(no, new ArrayList<RequestInfo>());
			}
			rmap.get(no).add(ri);
		}
		return rmap;
	}
	
	@SuppressWarnings("rawtypes")
	public void saveToDB(HashMap<Integer,ArrayList<RequestInfo>> rmap){
		dao.connect();
		Set<Integer> keyList = rmap.keySet();
		Iterator it = keyList.iterator();
			
		while(it.hasNext()){
			int no = (Integer)it.next();
			List<RequestInfo> requestList = rmap.get(no);
			for(RequestInfo ri : requestList){
				dao.insert(no,ri.getSenderId(),ri.getReceiverId(),ri.getRequestType());
			}
		}
		dao.close();
	}
	
	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
}
