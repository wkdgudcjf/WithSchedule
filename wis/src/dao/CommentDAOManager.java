package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import dao.CommentDAO.Comment;
import dto.CommentInfo;

public class CommentDAOManager {
	private static CommentDAOManager manager = new CommentDAOManager();
	private CommentDAO dao;
	public CommentDAOManager() {
		super();
		this.dao = new CommentDAO();
	}
	
	public static CommentDAOManager getInstance(){
		return manager;
	}
		
	public HashMap<Integer,ArrayList<CommentInfo>> loadFromDB(){
		dao.connect();
		List<Comment> clist = dao.searchAll();
		dao.close();

		HashMap<Integer,ArrayList<CommentInfo>> cmap = new HashMap<Integer, ArrayList<CommentInfo>>();
		
		for (Comment c : clist) {
			int no = c.getNo();
			int scheduleNo = c.getScheduleNo();
			int memberId = c.getMemberId();
			String content = c.getContent();
			
			StringTokenizer st = new StringTokenizer(c.getDate(),"/");
			int year = Integer.valueOf(st.nextToken());
			int month = Integer.valueOf(st.nextToken())-1;
			int day = Integer.valueOf(st.nextToken());
			int hour = Integer.valueOf(st.nextToken());
			int min = Integer.valueOf(st.nextToken());
			GregorianCalendar gc = new GregorianCalendar(year,month,day,hour,min);
			
			CommentInfo ei = new CommentInfo(no,memberId,content,gc);
			if(cmap.get(scheduleNo)==null){
				cmap.put(scheduleNo, new ArrayList<CommentInfo>());
			}
			cmap.get(scheduleNo).add(ei);
		}
		return cmap;
	}
	
	@SuppressWarnings("rawtypes")
	public void saveToDB(HashMap<Integer,ArrayList<CommentInfo>> cmap){
		dao.connect();
		Set<Integer> keyList = cmap.keySet();
		Iterator it = keyList.iterator();
			
		while(it.hasNext()){
			Integer scheduleNo = (Integer)it.next();
			ArrayList<CommentInfo> commentList = cmap.get(scheduleNo);
			System.out.println("no : "+scheduleNo);
			System.out.println("info : "+commentList);
			for(CommentInfo ci : commentList){
				StringBuilder sb = new StringBuilder();
				GregorianCalendar gc = ci.getDate();
				sb.append(gc.get(Calendar.YEAR));
				sb.append("/");
				sb.append(gc.get(Calendar.MONTH)+1);
				sb.append("/");
				sb.append(gc.get(Calendar.DAY_OF_MONTH));
				sb.append("/");
				sb.append(gc.get(Calendar.HOUR_OF_DAY));
				sb.append("/");
				sb.append(gc.get(Calendar.MINUTE));
				dao.insert(ci.getNo(),scheduleNo,ci.getMemberId(),ci.getContent(),sb.toString());
			}
		}
		dao.close();
	}

	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
	
	public CommentDAO getDao() {
		return dao;
	}

	public void setDao(CommentDAO dao) {
		this.dao = dao;
	}
	
	
}
