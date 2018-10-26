package Client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	private static DataOutputStream ou;
	private static BufferedReader in;
	private static Socket s;
	private static String message,name;
	private static boolean end;
	private static ClientThread cl;
	
	public Client() {
		initialize();
		try{			
			System.out.print("Input you name: ");
			name=in.readLine();
			cl=new ClientThread(s,name);
			cl.start();
			ou.writeUTF(name);//send 1 ok
		}catch(IOException io1){
			System.out.println("Error input text");
		}
		while(!end){
			try{
				setMessage(in.readLine());				
				ou.writeUTF(getMessage());
				if(getMessage().equalsIgnoreCase("exit")){
					end=true;
				}
			}catch(IOException io2){
				System.out.println("Error in loop");
			}
		}
	}
	
	private static void initialize(){
		try{
			s=new Socket("localhost",25);
			ou=new DataOutputStream(s.getOutputStream());
			in=new BufferedReader(new InputStreamReader(System.in));
			end=false;			
		}catch(IOException io){
			io.printStackTrace();
		}
	}
	
	public String getMessage() {
		return Client.message;
	}
	
	public void setMessage(String message) {
		Client.message = message;
	}
}
