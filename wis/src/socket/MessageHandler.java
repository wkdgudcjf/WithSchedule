package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageHandler {
	private Socket socket;
	public MessageHandler(String ip,int port) throws UnknownHostException,IOException{
		this.socket=new Socket(ip,port);
	}
	public MessageHandler(Socket socket){
		this.socket=socket;
	}
	
	public String readMessage(){
		BufferedReader reader = null;
		String str = null;
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			str=reader.readLine();
		}catch(IOException e){
			e.printStackTrace();
		}
		return str;
	}
	public void sendMessage(String message){
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(socket.getOutputStream());
			writer.println(message);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public void close(){
		try{
			this.socket.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void sendThread(){
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		String str=null;
		try{
			while(true){
				if(!socket.isConnected()){
					close();
				}
				str=reader.readLine();
				sendMessage(str);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void readThread(){
		String str=null;
		while(true){
			if(!socket.isConnected()){
				close();
			}
			str=readMessage();
		}
	}
}
