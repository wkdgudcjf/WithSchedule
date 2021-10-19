package management;

import java.util.*;

import org.apache.catalina.tribes.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.*;
import list.*;

public class MemberManagement {
	public static final String SYSTEM_ERROR = "system_error";
	public static final String DOUBLE_EMAIL = "double_email";
	public static final String COMPLETION = "completion";
	public static final String NO_SEARCH = "no_search";
	public static final String MISSMATCH_PASSWORD = "missmatch_password";
	
	private static MemberManagement management = new MemberManagement();
	private MemberList memberList;
	private friendManager friendManager;
	private FriendRequestManager friendRequestManager;
	private HashMap<Integer,String> idmap;
	
	public MemberManagement()
	{
		this.memberList = new MemberList();
		idmap=new HashMap<Integer,String>();
	}
	
	public String addId(String email,String id)
	{
		int memberNo = memberList.searchForEmail(email).getId();
		System.out.println(memberNo+"과"+id+"가 들어왓다.");
		if(this.idmap.get(memberNo)==null)
		{
			this.idmap.put(Integer.valueOf(memberNo),id);
		}
		else
		{
			this.idmap.remove(Integer.valueOf(memberNo));
			this.idmap.put(Integer.valueOf(memberNo),id);
		}
		return "good";
	}
	
	public HashMap<Integer, String> getIdmap() {
		return idmap;
	}
	public void setIdmap(HashMap<Integer, String> idmap) {
		this.idmap = idmap;
	}
	
	public void deleteGcmId(Integer memberId){
		this.idmap.remove(memberId);
	}
	
	public friendManager getFriendManager() {
		return friendManager;
	}

	public FriendRequestManager getFriendRequestManager() {
		return friendRequestManager;
	}

	public void setFriendRequestManager(HashMap<Integer, ArrayList<Integer>> map) {
		this.friendRequestManager = new FriendRequestManager(map);
	}
	
	public static MemberManagement getInstance(){
		return management;
	}

	public  MemberList getMemberList() {
		return this.memberList;
	}

	public void setMemberList(ArrayList<MemberInfo> ArrayList){
		this.memberList.setList(ArrayList);
	}

	public void setFriendManager(HashMap<Integer, ArrayList<FriendInfo>> map){
		this.friendManager=new friendManager(map);
	}
	
	public HashMap<Integer,ArrayList<FriendInfo>> getFriendMap(){
		return this.friendManager.getFriendMap();
	}
	
	
	public String deleteMember(String email){
		MemberInfo searchMember = this.memberList.searchForEmail(email);
		
		if(searchMember==null){
			return NO_SEARCH;
		}else{						
			CommentManager cm = ScheduleManagement.getInstance().getCommentManager();
			Set<Integer> key = cm.getCommentMap().keySet();
			Iterator<Integer> it = key.iterator();
			while(it.hasNext()){
				int sno = (Integer)it.next();
				ArrayList<CommentInfo> list = cm.getCommentMap().get(sno);
				if(list!=null){
					ArrayList<CommentInfo> copyList = (ArrayList<CommentInfo>)list.clone();
					for(CommentInfo ci : copyList){
						if(ci.getMemberId()==searchMember.getId()){
							ci.setMemberId(0);
						}
					}
				}				
			}
			
			//스케쥴 삭제
			ScheduleManagement.getInstance().outAllSchedule(email);
			//멤버삭제
			
			ArrayList<FriendInfo> flist = this.friendManager.getFriendMap().get(searchMember.getId());
			
			if(flist!=null){
				ArrayList<FriendInfo> copyflist = (ArrayList<FriendInfo>)flist.clone();				
				for(FriendInfo fi : copyflist){
					ArrayList<FriendInfo> fflist = this.friendManager.getFriendMap().get(fi.getFriendId());
					if(fflist!=null){
					ArrayList<FriendInfo> copyfflist = (ArrayList<FriendInfo>)fflist.clone();
						for(FriendInfo ffi : copyfflist){
							if(ffi.getFriendId()==searchMember.getId()){
								fflist.remove(ffi);
							}
						}
					}
				}		
			}
			this.friendManager.getFriendMap().remove(searchMember.getId());			
						
			this.friendRequestManager.getRequestMap().remove(searchMember.getId());
			//Gcm맵핑삭제
			MemberManagement.getInstance().deleteGcmId(searchMember.getId());
			
			this.memberList.remove(searchMember);
			
			return COMPLETION;
		}	
	}
	
	public String modifyMember(String email, MemberInfo modifyMemberInfo) {
		MemberInfo member = memberList.searchForEmail(email);
		MemberInfo modifyMember = memberList.searchForEmail(modifyMemberInfo.getEmail());
		
		if(member==null) return NO_SEARCH;
		
		else if(modifyMember!=null&&!modifyMember.getEmail().equals(email)){
			return DOUBLE_EMAIL;
		}
		
		else{
			member.setEmail(modifyMemberInfo.getEmail());
			member.setName(modifyMemberInfo.getName());
			member.setPassword(modifyMemberInfo.getPassword());
			return COMPLETION;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String resetFriendList(String email, ArrayList<String> phoneNumList) {
		// TODO Auto-generated method stub
		int memberNo = memberList.searchForEmail(email).getId();	
		
		ArrayList<MemberInfo> memberArrayList = new ArrayList<MemberInfo>();
		
		for(MemberInfo mInfo: this.memberList.getList()){
			for(String phoneNum : phoneNumList){
				if(mInfo.getPhoneNum().equals(phoneNum)){
					memberArrayList.add(mInfo);
				}
			}
		}
		
		ArrayList<FriendInfo> friendList = friendManager.searchFriendList(memberNo);
		System.out.println("1 : "+friendList);
		//System.out.println("//4//");
		//System.out.println(friendList);
		//System.out.println("//5//");
		//System.out.println(memberArrayList);
		//사라진 전화번호부 회원 없애기
		if(friendList==null){
			friendList = new ArrayList<FriendInfo>();
			friendManager.getFriendMap().put(memberNo, friendList);			
		}
		
		ArrayList<FriendInfo> flist = new ArrayList<FriendInfo>();
		
		for(FriendInfo fInfo : friendList){
			boolean check=false;
			for(MemberInfo mInfo: memberArrayList){
				if(fInfo.getFriendId()==mInfo.getId()){
					System.out.println("2 : "+fInfo);
					check=true;
					break;
				}
			}
			if(check==false){
				flist.add(fInfo);
			}
		}
		
		for(FriendInfo fInfo : flist){
			if(fInfo.getHasPhoneNum()){
				friendList.remove(fInfo);
			}
		}
		
		//새로생긴 전화번호부 회원 만들기
		for(MemberInfo mInfo: memberArrayList){
			boolean check=false;
			for(FriendInfo fInfo : friendList){
				if(fInfo.getFriendId()==mInfo.getId()){
					fInfo.setHasPhoneNum(true);
					check=true;
					break;
				}
			}
			if(check==false){				
				friendList.add(new FriendInfo(mInfo.getId(),true));
				
				//친구추천에 떠있으면 삭제하기
				ArrayList<Integer> rlist = MemberManagement.getInstance().getFriendRequestManager().searchFriendRequestList(memberNo);
			    if(rlist!=null){
			    	rlist.remove(new Integer(mInfo.getId()));
			    }		    
			}
		}
		
		//최종 결과물 리턴
		JSONArray ary = new JSONArray();
		for(FriendInfo friendInfo : friendList){
			MemberInfo info = this.memberList.searchForId(friendInfo.getFriendId());//여기서 과부화 가능성??
			JSONObject obj = new JSONObject();
			obj.put("email", info.getEmail());
			obj.put("name", info.getName());
			if(friendInfo.getHasPhoneNum()){
				obj.put("phoneNum", info.getPhoneNum());
			}else{
				obj.put("phoneNum", null);
			}
			ary.add(obj);
		}		
		return ary.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	public String searchFriendList(Integer myId){
		ArrayList<FriendInfo> friendList = this.friendManager.searchFriendList(myId);
		JSONArray ary = new JSONArray();
		for(FriendInfo friendInfo : friendList){
			MemberInfo info = this.memberList.searchForId(friendInfo.getFriendId());//여기서 과부화 가능성??
			JSONObject obj = new JSONObject();
			obj.put("email", info.getEmail());
			obj.put("name", info.getName());
			if(friendInfo.getHasPhoneNum()){
				obj.put("phoneNum", info.getPhoneNum());
			}
			ary.add(obj);
		}
		return ary.toString();
	}
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	public JSONObject login(MemberInfo memberInfo){
		MemberInfo searchMember = this.memberList.searchForEmail(memberInfo.getEmail());		//로직
		
		JSONObject obj = new JSONObject();		
		
		if(searchMember==null){
			obj.put("result", NO_SEARCH);
			return obj;
		}else if(memberInfo.getPassword().equals(searchMember.getPassword())){
			searchMember.setPhoneNum(memberInfo.getPhoneNum());
			obj.put("name",searchMember.getName());
			obj.put("result", COMPLETION);
			return obj;
		}else{
			obj.put("result", MISSMATCH_PASSWORD);
			return obj;
		}	
	}
	
	public String join(MemberInfo memberInfo) {		
		MemberInfo searchMember = this.memberList.searchForEmail(memberInfo.getEmail());		
		
		if(searchMember!=null){
			return DOUBLE_EMAIL;
		}else{
			MemberInfo samePhoneNumMember = this.memberList.searchForPhoneNum(memberInfo.getPhoneNum());
			if(samePhoneNumMember!=null){
				samePhoneNumMember.setPhoneNum("-");//혹시 null로 해도 아무이상 없음???
			}
			int newId = MakeNewId();
			memberInfo.setId(newId);
			System.out.println(memberInfo);
			this.memberList.add(memberInfo);	
			
			return COMPLETION;
		}
	}
	
	private Integer MakeNewId() {
		// TODO Auto-generated method stub		
		Random r = new Random();
		while(true){
			int newId = r.nextInt();
			if(this.memberList.searchForId(newId)==null){
				return newId;
			}
		}
	}

	public String addFriendRequest(String senderEmail, String receiverEmail) {
		return this.friendRequestManager.addFriendRequest(senderEmail,receiverEmail);
	}
	
	public String searchFriendRequest(String email)
	{
		ArrayList<MemberInfo> list = this.friendRequestManager.searchFriendRequestList(email);
		JSONArray ary = new JSONArray();
		for(MemberInfo memberInfo : list){
			JSONObject obj = new JSONObject();
			obj.put("email", memberInfo.getEmail());
			obj.put("name", memberInfo.getName());
			ary.add(obj);
		}
		return ary.toString();
	}

	
	
}
