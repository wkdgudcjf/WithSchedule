package list;

import java.util.HashMap;
import java.util.ArrayList;

import management.MemberManagement;

import dto.FriendInfo;
import dto.MemberInfo;

public class friendManager {
	private HashMap<Integer,ArrayList<FriendInfo>> friendMap;

	public friendManager(HashMap<Integer, ArrayList<FriendInfo>> friendMap) {
		super();
		this.friendMap = friendMap;
	}

	public friendManager() {
		super();
	}

	public ArrayList<FriendInfo> searchFriendList(Integer memberId){
		ArrayList<FriendInfo> ArrayList = this.friendMap.get(memberId);
		return ArrayList==null?null:ArrayList;
	}	
	
	public boolean addFriend(String myEmail,String friendEmail){
		  MemberInfo my = MemberManagement.getInstance().getMemberList().searchForEmail(myEmail);
		  MemberInfo friend = MemberManagement.getInstance().getMemberList().searchForEmail(friendEmail);
		  if(my==null||friend==null) return false;
		  FriendInfo fi = new FriendInfo(friend.getId(), false);
		  ArrayList<FriendInfo> flist = this.friendMap.get(my.getId());
		  if(flist==null)
		  {
		   return false;
		  }
		  flist.add(fi);
		  return true;
	}
	
	public HashMap<Integer, ArrayList<FriendInfo>> getFriendMap() {
		return friendMap;
	}

	public void setFriendMap(HashMap<Integer, ArrayList<FriendInfo>> friendMap) {
		this.friendMap = friendMap;
	}

	@Override
	public String toString() {
		return "friendManager [friendMap=" + friendMap + "]";
	}
	
	
}
