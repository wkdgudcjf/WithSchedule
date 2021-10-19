package com.example.withschedule.friend;


public class FriendManagement {
	private FriendList friendList;
	public FriendManagement()
	{
		friendList = new FriendList();
	}
	
	public FriendList getFriendList() {
		return friendList;
	}

	public void setFriendList(FriendList friendList) {
		this.friendList = friendList;
	}

	public void cancel(String email)
	{
		for(int i=0;i<this.friendList.getList().size();i++)
		{
			if(this.friendList.getList().get(i).getEmail().equals(email))
			{
				this.friendList.getList().remove(i);
			}
		}
	}
	
	public boolean modify(String oemail,String nemail,String nnickname,String nphoneNo)
	{
		for(int i=0;i<this.friendList.getList().size();i++)
		{
			if(this.friendList.getList().get(i).getEmail().equals(oemail))
			{
				this.friendList.getList().get(i).setEmail(nemail);
				this.friendList.getList().get(i).setNickname(nnickname);
				this.friendList.getList().get(i).setPhoneNo(nphoneNo);
				return true;
			}
		}
		return false;
	}

	public void enroll(Friend friend) {
		this.friendList.add(friend);
	}
	public Friend search(String email)
	{
		for(int i=0;i<this.friendList.getList().size();i++)
		{
			if(this.friendList.getList().get(i).getEmail().equals(email))
			{
				return this.friendList.getList().get(i);
			}
		}
		return null;
	}
	
	public void allRemove()
	{
		int size = this.friendList.getList().size();
		for(int i=0;i<size;i++)
		{
			this.friendList.getList().remove(0);
		}
	}

}
