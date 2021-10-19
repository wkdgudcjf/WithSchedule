package list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import management.MemberManagement;
import management.ScheduleManagement;

import dto.EntryInfo;
import dto.FriendInfo;
import dto.MemberInfo;
import dto.RequestInfo;
import dto.ScheduleInfo;

public class FriendRequestManager {
	private HashMap<Integer,ArrayList<Integer>> requestMap;

	public FriendRequestManager(HashMap<Integer, ArrayList<Integer>> rmap) {
		super();
		this.requestMap = rmap;	
	}

	public HashMap<Integer, ArrayList<Integer>> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(HashMap<Integer, ArrayList<Integer>> requestMap) {
		this.requestMap = requestMap;
	}	
	
/*	public boolean deleteRequest(Integer sno, String senderEmail,String receiverEmail,int type){
		MemberInfo sender = MemberManagement.getInstance().getMemberList().searchForEmail(senderEmail);
		MemberInfo receiver = MemberManagement.getInstance().getMemberList().searchForEmail(receiverEmail);
		if(sender==null||receiver==null) return false;
		ArrayList<RequestInfo> rlist = this.requestMap.get(sno);
		for(RequestInfo r : rlist){
			if(r.getReceiverId()==receiver.getId()&&r.getSenderId()==sender.getId()&&r.getRequestType()==type){
				rlist.remove(r);
				return true;
			}
		}
		return false;
	}
	*/
	
	public ArrayList<Integer> searchFriendRequestList(int no){
		
		ArrayList<Integer> rlist = this.requestMap.get((Integer)no);
		if(rlist==null) return null;
		
		return rlist;
	}

	public ArrayList<MemberInfo> searchFriendRequestList(String email){
		MemberInfo sender = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		ArrayList<Integer> rlist = this.requestMap.get(sender.getId());
		if(rlist==null)
		{
			rlist=new ArrayList<Integer>();
		}
		ArrayList<MemberInfo> list = new ArrayList<MemberInfo>();
		for(int i=0;i<rlist.size();i++)
		{
			list.add(MemberManagement.getInstance().getMemberList().searchForId(rlist.get(i)));
		}
		return list;
	}
	
/*	public HashMap<Integer, ArrayList<Integer>> searchRequestMap(String email){
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		if(member==null) return null;
		
		ArrayList<ScheduleInfo> scheduleList = ScheduleManagement.getInstance().getScheduleList();
				
		HashMap<Integer, ArrayList<Integer>> rhashMap = new HashMap<Integer, ArrayList<Integer>>();
		for(ScheduleInfo schedule : scheduleList){
			ArrayList<RequestInfo> rlist = searchRequestList(schedule.getNo());
			if(rlist==null) break;		
			ArrayList<RequestInfo> list = null;
			for(RequestInfo ri : rlist){				
				if(ri.getReceiverId()==member.getId()||ri.getSenderId()==member.getId()){
					if(list==null) list = new ArrayList<RequestInfo>();
					list.add(ri);
				}
			}			
			if(list!=null) rhashMap.put(schedule.getNo(), list);
		}
		
		return rhashMap;
	}*/
	
	@Override
	public String toString() 
	{
		return "RequestManager [requestMap=" + requestMap + "]";
	}

	public String addFriendRequest(String senderEmail, String receiverEmail) {
		MemberInfo sender = MemberManagement.getInstance().getMemberList().searchForEmail(senderEmail);
		MemberInfo receiver = MemberManagement.getInstance().getMemberList().searchForEmail(receiverEmail);
		System.out.println(senderEmail);
		System.out.println(receiverEmail);
		int k=0;
		if(sender==null||receiver==null) return ScheduleManagement.NO_SEARCH;
		ArrayList<FriendInfo> list = MemberManagement.getInstance().getFriendManager().searchFriendList(receiver.getId());
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getFriendId()==sender.getId())
			{
				k=1;
				break;
			}
		}
		if(k==0)
		{
			ArrayList<Integer> rlist = this.requestMap.get(receiver.getId());
			if(rlist==null){
				rlist = new ArrayList<Integer>();
				this.requestMap.put(receiver.getId(), rlist);
			}
			else{
				for(Integer r : rlist){			
					if(r==sender.getId()) 
						return ScheduleManagement.DUPLICATE;
				}
			}
			rlist.add(sender.getId());
		}
		else
		{
		   ArrayList<Integer> rlist = this.requestMap.get(sender.getId());
		   if(rlist==null){
		    rlist = new ArrayList<Integer>();
		    this.requestMap.put(receiver.getId(), rlist);
		   }
		   rlist.remove(new Integer(receiver.getId()));
		}
		MemberManagement.getInstance().getFriendManager().addFriend(senderEmail, receiverEmail);
		return ScheduleManagement.COMPLETION;
	}


}
