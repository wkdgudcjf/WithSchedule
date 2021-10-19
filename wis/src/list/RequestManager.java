package list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import management.MemberManagement;
import management.ScheduleManagement;

import dto.EntryInfo;
import dto.MemberInfo;
import dto.RequestInfo;
import dto.ScheduleInfo;

public class RequestManager {
	private HashMap<Integer,ArrayList<RequestInfo>> requestMap;
	
	public RequestManager(HashMap<Integer, ArrayList<RequestInfo>> rmap) {
		super();
		this.requestMap = rmap;	
	}

	public HashMap<Integer, ArrayList<RequestInfo>> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(HashMap<Integer, ArrayList<RequestInfo>> requestMap) {
		this.requestMap = requestMap;
	}
	
	public String addRequest(Integer sno, String senderEmail,String receiverEmail,int type){
		MemberInfo sender = MemberManagement.getInstance().getMemberList().searchForEmail(senderEmail);
		MemberInfo receiver = MemberManagement.getInstance().getMemberList().searchForEmail(receiverEmail);
		if(sender==null||receiver==null) return ScheduleManagement.NO_SEARCH;
		RequestInfo ri = new RequestInfo(receiver.getId(), sender.getId(), type);
		ArrayList<RequestInfo> rlist = this.requestMap.get(sno);
		if(rlist==null){
			rlist = new ArrayList<RequestInfo>();
			this.requestMap.put(sno, rlist);
		}
		else{
			for(RequestInfo r : rlist){
				if(r.getReceiverId()==receiver.getId()&&r.getSenderId()==sender.getId()) 
					return ScheduleManagement.DUPLICATE;
			}
		}
		rlist.add(ri);
		return ScheduleManagement.COMPLETION;
	}
	
	public boolean deleteRequest(Integer sno, String senderEmail,String receiverEmail,int type){
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
	
	
	public ArrayList<RequestInfo> searchRequestList(int no){
		
		ArrayList<RequestInfo> rlist = this.requestMap.get((Integer)no);
		if(rlist==null) return null;
		
		return rlist;
	}
	
	public HashMap<Integer, ArrayList<RequestInfo>> searchRequestMap(String email){
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		if(member==null) return null;
		
		ArrayList<ScheduleInfo> scheduleList = ScheduleManagement.getInstance().getScheduleList();
				
		HashMap<Integer, ArrayList<RequestInfo>> rhashMap = new HashMap<Integer, ArrayList<RequestInfo>>();
		for(ScheduleInfo schedule : scheduleList){
			ArrayList<RequestInfo> rlist = searchRequestList(schedule.getNo());
			if(rlist==null) continue;		
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
	}
	
	public String acceptEnterRequest(Integer sno, String senderEmail) {
		// TODO Auto-generated method stub
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(senderEmail);
		if(member==null) return ScheduleManagement.NO_SEARCH;
		ArrayList<RequestInfo> rlist = this.requestMap.get((Object)sno);
		if(rlist==null) return ScheduleManagement.NO_SEARCH;
		
		ArrayList<RequestInfo> selectList = new ArrayList<RequestInfo>();
		
		for(RequestInfo ri : rlist){
			if(ri.getSenderId()==member.getId()){
				selectList.add(ri);
			}
		}
		
		for(RequestInfo sri : selectList){
			rlist.remove(sri);
		}
		
		ScheduleManagement.getInstance().getEntryManager().addEntry(sno,member.getId());
				
		return ScheduleManagement.COMPLETION;
	}	
	
	public String cancelEnterRequest(Integer sno, String senderEmail,
			String receiverEmail) {
		// TODO Auto-generated method stub
		MemberInfo sendMember = MemberManagement.getInstance().getMemberList().searchForEmail(senderEmail);
		MemberInfo receiveMember = MemberManagement.getInstance().getMemberList().searchForEmail(receiverEmail);
		if(sendMember==null && receiveMember==null) return ScheduleManagement.NO_SEARCH;
		ArrayList<RequestInfo> rlist = this.requestMap.get((Object)sno);
		if(rlist==null) return ScheduleManagement.NO_SEARCH;
		
		RequestInfo sri = null;
		
		for(RequestInfo ri : rlist){
			if(ri.getSenderId()==sendMember.getId()&&ri.getReceiverId()==receiveMember.getId()){
				sri=ri;
				break;
			}
		}
		
		rlist.remove(sri);
		
		return ScheduleManagement.COMPLETION;
	}
	
	@Override
	public String toString() {
		return "RequestManager [requestMap=" + requestMap + "]";
	}

	

	
}
