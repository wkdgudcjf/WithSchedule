package dto;

public class GcmInfo {
	private int memberId;
	private String gcmId;
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getGcmId() {
		return gcmId;
	}
	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}
	public GcmInfo(int memberId, String gcmId) {
		super();
		this.memberId = memberId;
		this.gcmId = gcmId;
	}
}
