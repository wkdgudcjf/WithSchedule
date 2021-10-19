package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dao.FriendRequestDAO.FriendRequest;

public class FriendRequestDAOManager {
	private static FriendRequestDAOManager manager = new FriendRequestDAOManager();
	private FriendRequestDAO dao;
	public FriendRequestDAOManager() {
		super();
		this.dao = new FriendRequestDAO();
	}
	
	public static FriendRequestDAOManager getInstance(){
		return manager;
	}
		
	public HashMap<Integer,ArrayList<Integer>> loadFromDB(){
		dao.connect();
		List<FriendRequest> rlist = dao.searchAll();
		dao.close();
		
		HashMap<Integer,ArrayList<Integer>> rmap = new HashMap<Integer, ArrayList<Integer>>();
		
		for (FriendRequest r : rlist) {			
			if(rmap.get(r.getReceiverId())==null){
				rmap.put(r.getReceiverId(), new ArrayList<Integer>());
			}
			rmap.get(r.getReceiverId()).add(r.getSenderId());
		}
		return rmap;
	}
	
	@SuppressWarnings("rawtypes")
	public void saveToDB(HashMap<Integer,ArrayList<Integer>> rmap){
		dao.connect();
		Set<Integer> keyList = rmap.keySet();
		Iterator it = keyList.iterator();
			
		while(it.hasNext()){
			int recieverId = (Integer)it.next();
			ArrayList<Integer> requestList = rmap.get(recieverId);
			for(Integer senderId : requestList){
				dao.insert(senderId,recieverId);
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
