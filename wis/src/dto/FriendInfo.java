package dto;

public class FriendInfo {
	private int friendId;
	private boolean hasPhoneNum;
	public FriendInfo(int friendId,boolean hasPhoneNum) {
		super();
		this.friendId = friendId;
		this.hasPhoneNum=hasPhoneNum;
	}
	public int getFriendId() {
		return friendId;
	}
	
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	
	
	public boolean getHasPhoneNum() {
		return hasPhoneNum;
	}

	public void setHasPhoneNum(boolean hasPhoneNum) {
		this.hasPhoneNum = hasPhoneNum;
	}

	@Override
	public String toString() {
		return "FriendInfo [friendId=" + friendId
				+ ", hasPhoneNum=" + hasPhoneNum + "]";
	}	
}


/*
public class FriendInfo {
	private int myId;
	private int friendId;
	private boolean hasPhoneNum;
	public FriendInfo(int myId, int friendId,boolean hasPhoneNum) {
		super();
		this.myId = myId;
		this.friendId = friendId;
		this.hasPhoneNum=hasPhoneNum;
	}

	public int getMyId() {
		return myId;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	
	
	public boolean getHasPhoneNum() {
		return hasPhoneNum;
	}

	public void setHasPhoneNum(boolean hasPhoneNum) {
		this.hasPhoneNum = hasPhoneNum;
	}

	@Override
	public String toString() {
		return "FriendInfo [myId=" + myId + ", friendId=" + friendId
				+ ", hasPhoneNum=" + hasPhoneNum + "]";
	}	
}*/
