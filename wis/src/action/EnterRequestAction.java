package action;


import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import management.MemberManagement;
import management.ScheduleManagement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.android.gcm.server.*;

public class EnterRequestAction implements ServletAction
{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//JSONObject object=(JSONObject)request.getAttribute("keyword");
		//JSONObject obj=(JSONObject)request.getAttribute("newMemberInfo");
		
		JSONParser parse = new JSONParser();
		
		JSONObject obj=(JSONObject)parse.parse(ActionUtil.getInputString(request));
		
		//System.out.println(obj.toString());
		int sno = Integer.valueOf(obj.get("sno").toString());
		String senderEmail = (String)obj.get("sender");
		String receiverEmail = (String)obj.get("receiver");
		int type = Integer.valueOf(obj.get("type").toString());
				
		String result=ScheduleManagement.getInstance().getRequestManager().addRequest(sno, senderEmail, receiverEmail, type);
		String name = MemberManagement.getInstance().getMemberList().searchForEmail(senderEmail).getName();
		int memberNo = MemberManagement.getInstance().getMemberList().searchForEmail(receiverEmail).getId();
		String roomname = ScheduleManagement.getInstance().search(sno).getTitle();
		sendGCM(MemberManagement.getInstance().getIdmap().get(Integer.valueOf(memberNo)),roomname+"방에 입장요청을 하셨습니다.",name+"("+senderEmail+")님이 참가요청을 하였습니다.");
		
		//System.out.println(result);
		request.setAttribute("result", result);
		//tc.savaPersonalMapping(x, y, width, height, email);
	}
	
	private void sendGCM(String registrationId, String title, String message) 
	{
		System.out.println(registrationId+"로보내자");
		Sender sender = new Sender("AIzaSyBj2AHYqcI8DTRmP1_Y7ufbCg1gWoDtwIg");
		Message msg = new Message.Builder().addData("title", title).addData("msg", message).build();
		try {
			/*Result result = */sender.send(msg, registrationId, 3);
			//result 값으로 뭔가를 할 수 있을지....??
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
