package dto;

public class EntryInfo {
	private int memberId;
	private boolean isOwner;
	public EntryInfo(int memberId,boolean isOwner) {
		super();
		this.memberId = memberId;
		this.isOwner = isOwner;
	}
	public int getMemberId() {
		return memberId;
	}
	public boolean getIsOwner() {
		return isOwner;
	}
	public void setMember(int memberId) {
		this.memberId = memberId;
	}
	public void setIsOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}	
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	@Override
	public String toString() {
		return "EntryInfo [memberId=" + memberId + ", isOwner=" + isOwner + "]";
	}	
}
