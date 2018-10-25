package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {

	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;

	public ClientConnection(Socket socket) {
		try {
			setSocket(socket);
			setDataInputStream(new DataInputStream(getSocket().getInputStream()));
			setDataOutputStream(new DataOutputStream(getSocket().getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void setDataInputStream(DataInputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
	}
	
	public void setDataOutputStream(DataOutputStream dataOutputStream) {
		this.dataOutputStream = dataOutputStream;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public DataInputStream getBufferedReader() {
		return this.dataInputStream;
	}
	
	public DataOutputStream getDataOutputStream() {
		return this.dataOutputStream;
	}
	
	@Override
	public void run() {
		while (true) {
			receiveMessage();
		}
	}

	public String receiveMessage() {
		try {
			//String message;
			//bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return dataInputStream.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
