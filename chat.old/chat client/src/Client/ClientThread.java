package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread{
	private Socket s;
	private DataInputStream in;
	private boolean end;
	private String message,name;
	
	public ClientThread(Socket s,String name){
		this.s=s;
		this.name=name;
	}
	
	public ClientThread(){
		
	}
	
	public void run(){
		try{
			end=false;
			in=new DataInputStream(s.getInputStream());
			while(!end){
				message=in.readUTF();
				System.out.println(message);
				if(message.equalsIgnoreCase("- "+name+" disconected")){
					end=true;					
				}
			}
			s.close();
		}catch(IOException io){
			System.out.println("Error client thread");
			io.printStackTrace();
			end=true;
		}
	}
}
