package list;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.ArrayList;

import management.MemberManagement;

import dto.CommentInfo;
import dto.FriendInfo;
import dto.MemberInfo;

public class CommentManager {
	private HashMap<Integer,ArrayList<CommentInfo>> commentMap;

	public CommentManager(HashMap<Integer, ArrayList<CommentInfo>> commentMap) {
		super();
		this.commentMap = commentMap;
	}

	public CommentManager() {
		super();
	}
	
	public HashMap<Integer, ArrayList<CommentInfo>> getCommentMap() {
		return commentMap;
	}

	public void setCommentMap(HashMap<Integer, ArrayList<CommentInfo>> commentMap) {
		this.commentMap = commentMap;
	}
	
	public ArrayList<CommentInfo> searchCommentList(Integer sno){
		return this.commentMap.get(sno); 
	}
	
	public boolean addComment(Integer scheduleNo,String email,String content, GregorianCalendar date){
		ArrayList<CommentInfo> clist = this.commentMap.get((Object)scheduleNo);
		if(clist==null){
			clist=new ArrayList<CommentInfo>();
			this.commentMap.put(scheduleNo, clist);
		}
		int size = clist.size();
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		if(member!=null){				
			clist.add(new CommentInfo(size, member.getId(), content, date));
			return true;		
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "CommentManager [commentMap=" + commentMap + "]";
	}
		
}
