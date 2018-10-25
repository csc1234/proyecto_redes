package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	
	private static final String ip = "127.0.0.1";
	private static final int port = 25;
	
	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public Client() {
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Hubo problemas al conectarse con el servidor", "UnknownHostException", JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public String receiveMessage() {
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			return dataInputStream.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendMessage(String message) {
		try {
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			dataInputStream.close();
			dataOutputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}