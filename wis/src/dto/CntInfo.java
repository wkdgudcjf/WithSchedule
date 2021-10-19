package dto;

public class CntInfo
{
	private int sno;
	private int ccnt;
	private int ecnt;
	public CntInfo(int sno, int ccnt, int ecnt) {
		super();
		this.sno = sno;
		this.ccnt = ccnt;
		this.ecnt = ecnt;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public int getCcnt() {
		return ccnt;
	}
	public void setCcnt(int ccnt) {
		this.ccnt = ccnt;
	}
	public int getEcnt() {
		return ecnt;
	}
	public void setEcnt(int ecnt) {
		this.ecnt = ecnt;
	}
	
}
