package management;

import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.CommentInfo;
import dto.EntryInfo;
import dto.MemberInfo;
import dto.RequestInfo;
import dto.ScheduleInfo;
import list.CommentManager;
import list.EntryManager;
import list.RequestManager;
import list.ScheduleList;

public class ScheduleManagement {
	public static final String SYSTEM_ERROR = "system_error";
	public static final String COMPLETION = "completion";
	public static final String NO_SEARCH = "no_search";
	public static final String DUPLICATE = "duplicate";
	
	private static ScheduleManagement scheduleManager = new ScheduleManagement();
	private ScheduleList scheduleList;
	
	private EntryManager entryManager;	
	
	private RequestManager requestManager;
	
	private CommentManager commentManager;
	
	public ScheduleManagement()
	{
		scheduleList = new ScheduleList();
	}
	
	public static ScheduleManagement getInstance(){
		return scheduleManager;
	}
	
	public ArrayList<ScheduleInfo> getScheduleList() {
		return scheduleList.getList();
	}
	public void setScheduleList(ArrayList<ScheduleInfo> scheduleList) {
		this.scheduleList.setList(scheduleList);
	}
		
	public void setEntryManager(HashMap<Integer, ArrayList<EntryInfo>> hashMap) {
		this.entryManager = new EntryManager(hashMap);
	}
	
	public void setRequestManager(HashMap<Integer, ArrayList<RequestInfo>> hashMap) {
		this.requestManager = new RequestManager(hashMap);
	}

	public void setCommentManager(HashMap<Integer, ArrayList<CommentInfo>> hashMap) {
		this.commentManager = new CommentManager(hashMap);
	}
	
	public void cancel(int no)
	{
		for(int i=0;i<this.scheduleList.getList().size();i++)
		{
			if(this.scheduleList.getList().get(i).getNo()==no)
			{
				this.scheduleList.getList().remove(i);
			}
		}
	}

	public String enroll(String email,ScheduleInfo newScheduleInfo){
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		
		if(member==null){
			return NO_SEARCH;
		}else{
			Integer newNo = MakeNewNo();
			newScheduleInfo.setNo(newNo);
			this.scheduleList.add(newScheduleInfo);	
			
			System.out.println(this.entryManager.searchScheduleForMemberId(member.getId()));
			this.entryManager.addEntry(newNo,member.getId());
			System.out.println(this.entryManager.searchScheduleForMemberId(member.getId()));
			
			return String.valueOf(newNo);
		}
	}
	
	public String modify(ScheduleInfo modifySchedule) {
		// TODO Auto-generated method stub		
		//System.out.println(modifySchedule.getNo());
		ScheduleInfo schedule = this.scheduleList.search(modifySchedule.getNo());
		
		if(schedule==null){
			return NO_SEARCH;
		}else{
			schedule.setIsOpen(modifySchedule.getIsOpen());
			schedule.setIsPublic(modifySchedule.getIsPublic());
			schedule.setMemo(modifySchedule.getMemo());
			schedule.setStartTime(modifySchedule.getStartTime());
			schedule.setTitle(modifySchedule.getTitle());
			schedule.setType(modifySchedule.getType());
			return COMPLETION;
		}
	}
	
	public String out(String email,int sno) {
		// TODO Auto-generated method stub
		
		int memberId = MemberManagement.getInstance().getMemberList()
				.searchForEmail(email).getId();

		int result = entryManager.removeEntry(memberId, sno);
		
		if (result == -1)
			return NO_SEARCH;

		else if(result == 0) {
			ScheduleInfo schedule = scheduleList.search(sno);
			this.scheduleList.remove(schedule);	
			this.commentManager.getCommentMap().remove(sno);
			return COMPLETION;
		}
		return COMPLETION;
	}
	
	
	public String outAllSchedule(String email) {
		// TODO Auto-generated method stub
		int memberId = MemberManagement.getInstance().getMemberList().searchForEmail(email).getId();	
		
		ArrayList<Integer> ilist = entryManager.searchScheduleForMemberId(memberId);
		
		if(ilist==null){
			return COMPLETION;
		}
		
		ArrayList<Integer> icopyList = (ArrayList<Integer>)ilist.clone();
		for(int sno : icopyList){
			/*ArrayList<RequestInfo> rlist = this.requestManager.getRequestMap().get(sno);
			ArrayList<RequestInfo> copyrlist = (ArrayList<RequestInfo>)rlist.clone();
			System.out.println("copyrlist : "+copyrlist);
			if(rlist!=null){
				for(RequestInfo ri : copyrlist){
					System.out.println("receiverId : "+ri.getReceiverId());
					System.out.println("senderId : "+ri.getSenderId());
					if(ri.getReceiverId()==memberId||ri.getSenderId()==memberId){
						rlist.remove(ri);
					}
				}
			}*/			
			int result = entryManager.removeEntry(memberId, sno);
			if (result == -1)
				return NO_SEARCH;

			else if(result == 0) {
				ScheduleInfo schedule = scheduleList.search(sno);
				this.scheduleList.remove(schedule);	
				this.commentManager.getCommentMap().remove(sno);
				continue;
			}
		}
		
		Set<Integer> key = this.requestManager.getRequestMap().keySet();
		Iterator<Integer> it = key.iterator();
		while(it.hasNext()){
			int sno = (Integer)it.next();
			ArrayList<RequestInfo> list = this.requestManager.getRequestMap().get(sno);
			ArrayList<RequestInfo> copyList = (ArrayList<RequestInfo>)list.clone();
			for(RequestInfo ri : copyList){
				if(ri.getReceiverId()==memberId||ri.getSenderId()==memberId){
					list.remove(ri);
				}
			}
		}
		
		return COMPLETION;
	}
	
	
	public ScheduleInfo search(int no)
	{
		for(int i=0;i<this.scheduleList.getList().size();i++)
		{
			if(this.scheduleList.getList().get(i).getNo()==no)
			{
				return this.scheduleList.getList().get(i);
			}
		}
		return null;
	}

	public ArrayList<ScheduleInfo> getScheduleInfoOfDay(int year,int month,int day){
		ArrayList<ScheduleInfo> alist = new ArrayList<ScheduleInfo>();
		GregorianCalendar calendar = new GregorianCalendar(year,month,day,23,59,59);
		for(ScheduleInfo ScheduleInfo:this.scheduleList.getList()){
			switch(ScheduleInfo.getType()){
			case 1:
				if(ScheduleInfo.getStartTime().get(Calendar.YEAR)==year 
				&&ScheduleInfo.getStartTime().get(Calendar.MONTH)==month 
				&&ScheduleInfo.getStartTime().get(Calendar.DAY_OF_MONTH)==day)
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			case 2: 				
				if(ScheduleInfo.getStartTime().get(Calendar.DAY_OF_WEEK)==calendar.get(Calendar.DAY_OF_WEEK)
						&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{
					alist.add(ScheduleInfo);
				}
				break;
			case 3: 
				if(ScheduleInfo.getStartTime().getMaximum(Calendar.DAY_OF_MONTH)==day
						&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			case 4: 
				if(ScheduleInfo.getStartTime().get(Calendar.DAY_OF_MONTH)==day
						&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			case 5:
				if(ScheduleInfo.getStartTime().get(Calendar.MONTH)==month 
				&&ScheduleInfo.getStartTime().get(Calendar.DAY_OF_MONTH)==day
				&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			}
		}
		return alist;
	}

	public EntryManager getEntryManager(){
		return this.entryManager;
	}
	public RequestManager getRequestManager(){
		return this.requestManager;
	}
	public CommentManager getCommentManager(){
		return this.commentManager;
	}
	private Integer MakeNewNo() {
		// TODO Auto-generated method stub		
		Random r = new Random();
		while(true){
			int newNo = r.nextInt();
			if(this.scheduleList.search(newNo)==null){
				return newNo;
			}
		}
	}
	
	private ScheduleInfo parseToScheduleInfo(String data){
		JSONParser parser = new JSONParser();
		
		JSONObject obj=null;
		try {
			obj =  (JSONObject)parser.parse(data);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String title  = (String)obj.get("title");
		String memo  = (String)obj.get("memo");
		boolean isPublic  = (Boolean)obj.get("ispublic");
		boolean isOpen  = (Boolean)obj.get("isopen");
		int startYear = Integer.valueOf(obj.get("startyear").toString());
		int startMonth = Integer.valueOf(obj.get("startmonth").toString());
		int startDay = Integer.valueOf(obj.get("startday").toString());
		int startHour = Integer.valueOf(obj.get("starthour").toString());
		int startMin = Integer.valueOf(obj.get("startmin").toString());
		int type = Integer.valueOf(obj.get("type").toString());
			
		if(obj.get("sno")==null){
			return new ScheduleInfo(title, memo, new GregorianCalendar(startYear,startMonth,startDay,startHour,startMin), type,
					isOpen, isPublic);
		}
		Integer sno = Integer.valueOf(obj.get("sno").toString());
		return new ScheduleInfo(sno,title, memo, new GregorianCalendar(startYear,startMonth,startDay,startHour,startMin), type,
				isOpen, isPublic);
	}
	/*
	public void setInvitationMap(ArrayList<InvitationInfo> ArrayList){
		for(InvitationInfo ii:ArrayList){
			ArrayList<Integer> invitationArrayList=this.invitationMap.get(ii.getMyId());
			if(invitationArrayList==null){
				invitationArrayList=new ArrayArrayList<Integer>();
			}
			invitationArrayList.add(ii.getSenderId());
		}
	}
	
	public void setRequestMap(ArrayList<FriendInfo> ArrayList){
		for(FriendInfo fi:ArrayList){
			ArrayList<Integer> friendArrayList=this.friendMap.get(fi.getMyId());
			if(friendArrayList==null){
				friendArrayList=new ArrayArrayList<Integer>();
			}
			friendArrayList.add(fi.getFriendId());
		}
	}*/

	public ArrayList<ScheduleInfo> getMemberSchedule(String email){
		
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		if(member==null) return null;
		
		int memberId = member.getId();
		ArrayList<Integer> scheduleNoList = this.entryManager.getScheduleEntryByMemberId().get(memberId);
		
		if(scheduleNoList==null) return null;
			
		ArrayList<ScheduleInfo> scheduleList = new ArrayList<ScheduleInfo>();
		
		for(int ei: scheduleNoList ){
			ScheduleInfo schedule = this.scheduleList.search(ei);
			if(schedule==null) continue;
			scheduleList.add(schedule);
		}
		return scheduleList;		
	}
	
	public String isOwnerCheck(String email,int sno) {
		// TODO Auto-generated method stub	
		
		int memberNo = MemberManagement.getInstance().getMemberList().searchForEmail(email).getId();
		
		ArrayList<EntryInfo> elist = entryManager.searchMemberForScheduleNo(sno);
		if(elist==null) return NO_SEARCH;
		
		for(EntryInfo ei : elist){
			if(ei.getMemberId()==memberNo){
				return ei.getIsOwner()? "true":"false";
			}
		}
		
		return "false";
	}

	public ArrayList<ScheduleInfo> searchFriendsScheduleList(GregorianCalendar calendar,String email){
		ArrayList<ScheduleInfo> list = getMemberSchedule(email);		
		ScheduleList scheduleList = new ScheduleList(list);
		ArrayList<ScheduleInfo> dayList = scheduleList.getScheduleInfoOfDay(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
		//if(dayList==null) continue;	
		return dayList;
	}

	
}
