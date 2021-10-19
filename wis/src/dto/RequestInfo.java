package dto;

public class RequestInfo {
		public static final int ENTER = 1;
		public static final int INVITATION = 2;
		
		private int receiverId;
		private int senderId;
		private int requestType;
		public RequestInfo(int receiverId, int senderId,int requestType) {
			super();
			this.receiverId = receiverId;
			this.senderId = senderId;
			this.requestType=requestType;
		}
		public int getReceiverId() {
			return receiverId;
		}
		public int getSenderId() {
			return senderId;
		}
		public void setReceiverId(int receiverId) {
			this.receiverId = receiverId;
		}
		public void setSenderId(int senderId) {
			this.senderId = senderId;
		}
		public int getRequestType() {
			return requestType;
		}
		public void setRequestType(int requestType) {
			this.requestType = requestType;
		}
		@Override
		public String toString() {
			return "Request [receiverId=" + receiverId + ", senderId=" + senderId
					+ ", requestType="
					+ requestType + "]";
		}		
		
	}