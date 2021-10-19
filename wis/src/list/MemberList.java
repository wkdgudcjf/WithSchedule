package list;

import java.util.*;

import dto.*;

public class MemberList {
	
	private ArrayList<MemberInfo> list;

	public MemberList(ArrayList<MemberInfo> list) {
		this.list = list;
	}
	
	public MemberList(){
		this(new ArrayList<MemberInfo>());
	}

	public ArrayList<MemberInfo> getList() {
		return list;
	}

	public void setList(ArrayList<MemberInfo> list) {
		this.list = list;
	}

	public void add(MemberInfo memberInfo) {
		// TODO Auto-generated method stub
		this.list.add(memberInfo);
		Collections.sort(this.list);
	}
	
	public void remove(MemberInfo memberInfo)
	{
		this.list.remove(memberInfo);
	}
	
	public ArrayList<MemberInfo> searchAll(){
		return this.list;
	}
	
	public MemberInfo searchForEmail(String email){ 
		int index = Collections.binarySearch(this.list, new MemberInfo(email));
		if(index<0) return null;
		return this.list.get(index);
	}
	public MemberInfo searchForPhoneNum(String phoneNum){
		for(MemberInfo MemberInfo:this.list){
			if(MemberInfo.getPhoneNum().equals(phoneNum)){
				return MemberInfo;
			}
		}
		return null;
	}
	
	public MemberInfo searchForId(int id){
		for(MemberInfo MemberInfo:this.list){
			if(MemberInfo.getId()==id){
				return MemberInfo;
			}
		}
		return null;
	}
	
	public MemberInfo search(int index){
		return this.list.get(index);
	}
	
	
	@Override
	public String toString() {
		return "Schedulelist [list=" + list + "]";
	}	
	
}
