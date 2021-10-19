package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerCommandHandler implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		System.out.println("서버가 시작 되었습니다.");
		while(true){			
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
		}
	}

}
