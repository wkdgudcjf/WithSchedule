package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.EntryDAOManager;
import dao.FriendDAOManager;
import dao.MemberDAOManager;
import dao.RequestDAOManager;
import dao.ScheduleDAOManager;
import dto.MemberInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class ServerMain {
	private List<Runnable> list;
	
	public ServerMain(){
		list = new ArrayList<Runnable>();
	}
	
	public void start(){
		for(Runnable handler:this.list){
			new Thread(handler).start();
		}
	}

	public void addServerSocketHandler(
			Runnable handler) {
		this.list.add(handler);
		// TODO Auto-generated method stub		
	}
	
	
	public static void main(String args[]){	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		System.out.println("서버가 시작 되었습니다.");
		while(true){			
			System.out.print("명령어 입력 : ");
			try {
				command = reader.readLine();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			if(command.equals("end")){
				System.out.println("서버가 종료 되었습니다.");
				break;
			}
			if(command.equals("load")){
				System.out.println("로드 작업을 실행합니다...");				
				MemberManagement.getInstance().setMemberList(MemberDAOManager.getInstance().loadFromDB());
				ScheduleManagement.getInstance().setScheduleList(ScheduleDAOManager.getInstance().loadFromDB());
				MemberManagement.getInstance().setFriendManager(FriendDAOManager.getInstance().loadFromDB());
				ScheduleManagement.getInstance().setEntryManager(EntryDAOManager.getInstance().loadFromDB());
				ScheduleManagement.getInstance().setRequestManager(RequestDAOManager.getInstance().loadFromDB());
				
				//System.out.println("////");
				//System.out.println(MemberDAOManager.getInstance().loadFromDB());
				//System.out.println("////");
				//System.out.println(FriendDAOManager.getInstance().loadFromDB());
				/*
				JSONArray a = new JSONArray();
				
				JSONObject o = new JSONObject();
				o.put("email", "hysick71@nate.com");
				a.add(o);
				
				
				JSONArray ary = new JSONArray();
				JSONObject obj1 = new JSONObject();
				obj1.put("phoneNum", "01063722062");
				JSONObject obj2 = new JSONObject();
				obj2.put("phoneNum", "01063722063");
				JSONObject obj3 = new JSONObject();
				obj3.put("phoneNum", "01063722067");
				JSONObject obj4 = new JSONObject();
				obj4.put("phoneNum", "01063722068");
				
				ary.add(obj1);
				ary.add(obj2);
				ary.add(obj3);
				ary.add(obj4);
				
				a.add(ary);
				
				System.out.println(MemberManagement.getInstance().resetFriendList("hysick71@nate.com", ary.toString()));
				*/
				System.out.println("로드 작업 완료!");
			}
			
			if(command.equals("start")){
				new Thread(new JoinServerSocketHandler()).start();
				new Thread(new AddressServerSocketHandler()).start();
				new Thread(new LoginServerSocketHandler()).start();
				new Thread(new AddScheduleServerSocketHandler()).start();
				new Thread(new ScheduleModifyServerSocketHandler()).start();
				new Thread(new ScheduleOutServerSocketHandler()).start();
				new Thread(new OwnerCheckServerSocketHandler()).start();
				new Thread(new ModifyServerSocketHandler()).start();
				new Thread(new FriendCalendarServerSocketHandler()).start();
				new Thread(new MemberDeleteServerSocketHandler()).start();	
				new Thread(new EntryServerSocketHandler()).start();	
				new Thread(new RequestServerSocketHandler()).start();
				new Thread(new FriendsScheduleServerSocketHandler()).start();
				
				/*JSONArray ary = new JSONArray();
				JSONObject o = new JSONObject();
				o.put("year", "2013");
				o.put("month", "4");
				o.put("day", "13");
				ary.add(o);
				JSONArray a=new JSONArray();
				JSONObject n1 = new JSONObject();
				n1.put("email", "hysick71@nate.com");
				JSONObject n2 = new JSONObject();
				n2.put("email", "w@n.com");
				a.add(n1);
				a.add(n2);
				ary.add(a);*/
				
				//System.out.println(new FriendsScheduleServerSocketHandler().friendsScheduleListByParsingData(ary.toString()));
			}
			
			if(command.equals("save")){
				//FriendDAOManager.getInstance().getDao().connect();
				RequestDAOManager.getInstance().deleteAll(); 
				EntryDAOManager.getInstance().deleteAll();
				ScheduleDAOManager.getInstance().deleteAll();
				FriendDAOManager.getInstance().deleteAll();
				MemberDAOManager.getInstance().deleteAll();
								
				MemberDAOManager.getInstance().saveToDB(MemberManagement.getInstance().getMemberList().getList());
				FriendDAOManager.getInstance().saveToDB(MemberManagement.getInstance().getFriendMap());	
				ScheduleDAOManager.getInstance().saveToDB(ScheduleManagement.getInstance().getScheduleList());
				EntryDAOManager.getInstance().saveToDB(ScheduleManagement.getInstance().getEntryManager().getMemberEntryByScheduleNo());
				RequestDAOManager.getInstance().saveToDB(ScheduleManagement.getInstance().getRequestManager().getRequestMap());
				//FriendDAOManager.getInstance().getDao().close();
				System.out.println("저장 작업을 실행합니다...");
			}
		}
		
		/*
		MemberManagement.getInstance().setMemberList(MemberDAOManager.getInstance().loadFromDB());
		ScheduleManagement.getInstance().setScheduleList(ScheduleDAOManager.getInstance().loadFromDB());
		ServerMain main = new ServerMain();		
		main.addServerSocketHandler(new ServerCommandHandler());
		//main.addServerSocketHandler(new JoinServerSocketHandler());		
		//main.addServerSocketHandler(new LoginServerSocketHandler());		
		main.start();
				
		//MemberManagement m = MemberManagement.getInstance();
		//m.join(new MemberInfo("hysick71@nate.com", "01063722065", "함영식", "dudtlr8899"));*/
	}
}
