package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dao.FriendDAO.Friend;
import dto.FriendInfo;

public class FriendDAOManager {
	private static FriendDAOManager manager = new FriendDAOManager();
	private FriendDAO dao;

	public FriendDAOManager() {
		super();
		this.dao = new FriendDAO();
	}

	public static FriendDAOManager getInstance() {
		return manager;
	}

	public HashMap<Integer,ArrayList<FriendInfo>> loadFromDB() {
		dao.connect();
		List<Friend> flist = dao.searchAll();
		dao.close();

		HashMap<Integer,ArrayList<FriendInfo>> fmap = new HashMap<Integer, ArrayList<FriendInfo>>();
		
		for (Friend f : flist) {
			int myId = f.getMyId();
			FriendInfo fi = new FriendInfo(f.getFriendId(),f.getHasPhoneNum().equals("true")?true:false);
			
			if(fmap.get(myId)==null){
				fmap.put(myId, new ArrayList<FriendInfo>());
			}
			fmap.get(myId).add(fi);
		}
		return fmap;
	}

	@SuppressWarnings("rawtypes")
	public void saveToDB(HashMap<Integer,ArrayList<FriendInfo>> fmap) {
		dao.connect();
		Set<Integer> keyList = fmap.keySet();
		Iterator it = keyList.iterator();
			
		while(it.hasNext()){
			int myId = (Integer)it.next();
			ArrayList<FriendInfo> friendList = fmap.get(myId);
			for(FriendInfo fi : friendList){
				dao.insert(myId, fi.getFriendId(), fi.getHasPhoneNum()?"true":"false");
			}
		}
		dao.close();
	}

	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
	
	public FriendDAO getDao() {
		return dao;
	}

	public void setDao(FriendDAO dao) {
		this.dao = dao;
	}
	
	
}

/*public List<FriendInfo> loadFromDB(){
dao.connect();
List<Friend> flist = dao.searchAll();
dao.close();

List<FriendInfo> filist = new ArrayList<FriendInfo>();

for(Friend f : flist){
	FriendInfo fi = new FriendInfo(f.getMyId(),f.getFriendId(),f.getHasPhoneNum().equals("true")?true:false);
	filist.add(fi);
}		
return filist;
}

public void saveToDB(List<FriendInfo> filist){
for(FriendInfo fi: filist){
	dao.insert(fi.getMyId(),fi.getFriendId(),fi.getHasPhoneNum()?"true":"false");
}		
}*/