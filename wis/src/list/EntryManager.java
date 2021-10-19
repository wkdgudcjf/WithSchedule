package list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import management.*;

import dto.*;

public class EntryManager {
	private HashMap<Integer,ArrayList<EntryInfo>> memberEntryByScheduleNo;
	private HashMap<Integer,ArrayList<Integer>> scheduleEntryByMemberId;
	
	
	public EntryManager() {
		super();
	}
	
	public EntryManager(
			HashMap<Integer, ArrayList<EntryInfo>> memberEntryByScheduleNo,
			HashMap<Integer, ArrayList<Integer>> scheduleEntryByMemberId) {
		super();
		this.memberEntryByScheduleNo = memberEntryByScheduleNo;
		this.scheduleEntryByMemberId = scheduleEntryByMemberId;
	}
	
	
	
	public EntryManager(
			HashMap<Integer, ArrayList<EntryInfo>> memberEntryByScheduleNo) {
		super();
		this.memberEntryByScheduleNo = memberEntryByScheduleNo;
		this.scheduleEntryByMemberId = new HashMap<Integer, ArrayList<Integer>>();
		
		Set<Integer> keyset =  this.memberEntryByScheduleNo.keySet();
		Iterator<Integer> it = keyset.iterator();
		
		while(it.hasNext()){
			Integer scheduleNo = it.next();
			for(EntryInfo ei : this.memberEntryByScheduleNo.get(scheduleNo)){
				ArrayList<Integer> scheduleEntry = this.scheduleEntryByMemberId.get(ei.getMemberId());
				if(scheduleEntry==null){
					scheduleEntry = new ArrayList<Integer>();
					this.scheduleEntryByMemberId.put(ei.getMemberId(), scheduleEntry);
				}
				scheduleEntry.add(scheduleNo);
			}
		}		
	}

	public HashMap<Integer, ArrayList<EntryInfo>> getMemberEntryByScheduleNo() {
		return memberEntryByScheduleNo;
	}
	public HashMap<Integer, ArrayList<Integer>> getScheduleEntryByMemberId() {
		return scheduleEntryByMemberId;
	}
	public void setMemberEntryByScheduleNo(
			HashMap<Integer, ArrayList<EntryInfo>> memberEntryByScheduleNo) {
		this.memberEntryByScheduleNo = memberEntryByScheduleNo;
	}
	public void setScheduleEntryByMemberId(
			HashMap<Integer, ArrayList<Integer>> scheduleEntryByMemberId) {
		this.scheduleEntryByMemberId = scheduleEntryByMemberId;
	}

	@Override
	public String toString() {
		return "EntryManager [memberEntryByScheduleNo="
				+ memberEntryByScheduleNo + ", scheduleEntryByMemberId="
				+ scheduleEntryByMemberId + "]";
	}

	public void addEntry(Integer scheduleNo , Integer memberId) {
		// TODO Auto-generated method stub
		boolean isOwner = false;
		ArrayList<EntryInfo> elist = this.memberEntryByScheduleNo.get(scheduleNo);
		if(elist==null){
			elist=new ArrayList<EntryInfo>();
			this.memberEntryByScheduleNo.put(scheduleNo, elist);
			isOwner=true;
		}
		elist.add(new EntryInfo(memberId,isOwner));

		ArrayList<Integer> ilist = this.scheduleEntryByMemberId.get(memberId);
		if(ilist==null){
			ilist=new ArrayList<Integer>();
			this.scheduleEntryByMemberId.put(memberId, ilist);
		}
		ilist.add(scheduleNo);
	}	

	public ArrayList<EntryInfo> searchMemberForScheduleNo(int sno) {
		// TODO Auto-generated method stub
		ArrayList<EntryInfo> entrylist = this.memberEntryByScheduleNo.get(sno);
		return entrylist==null?null:entrylist;
	}	
	
	public ArrayList<Integer> searchScheduleForMemberId(int mId) {
		// TODO Auto-generated method stub
		ArrayList<Integer> entrylist = this.scheduleEntryByMemberId.get(mId);
		return entrylist==null? null:entrylist;
	}

	public int removeEntry(int memberId,int sno) {
		// TODO Auto-generated method stub
		ArrayList<EntryInfo> mlist = this.memberEntryByScheduleNo.get(sno);
		if(mlist==null) return -1;
		
		EntryInfo key = null;
		for(EntryInfo ei : mlist){
			if(ei.getMemberId() == memberId) key=ei;
		}
		
		if(key!=null)
		mlist.remove((Object)key);
		if(mlist.size()!=0){
			mlist.get(0).setIsOwner(true);
		}
		
		ArrayList<Integer> ilist = this.scheduleEntryByMemberId.get(memberId);
		if(ilist==null) return -1;
				
		Integer k = null;
		for(Integer i : ilist){
			if(i == sno) k=i;
		}
		if(k!=null)
		ilist.remove((Object)k);
		
		return mlist.size();
	}	
	
	
}
