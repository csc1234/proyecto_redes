package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private ServerSocket serverSocket;
	private Socket socket;
	private ClientConnection clientConnection;
	private ArrayList<ClientConnection> clientList;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public Server() {
		try {
			setServerSocket(new ServerSocket(25));
			clientList = new ArrayList<ClientConnection>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connection() {
		try {
			setSocket(getServerSocket().accept());
			setClientConnection(new ClientConnection(socket));
			getClientConnection().start();
			clientList.add(getClientConnection());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		try {
			if(!clientList.isEmpty() && socket != null) {
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF(message);
				for (ClientConnection clientConnection : clientList) {
						clientConnection.receiveMessage();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receiveMessage() {
		try {
			if(socket != null) {
				dataInputStream = new DataInputStream(socket.getInputStream());
				//if(dataInputStream != null && dataInputStream.ready())
				return dataInputStream.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void close() {
		try {
			if(dataInputStream != null) dataInputStream.close();
			if(dataOutputStream != null) dataOutputStream.close();
			if(clientList != null) clientList.clear();
			if(socket != null) socket.close();
			if(serverSocket != null) serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
	}
	
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public ClientConnection getClientConnection() {
		return this.clientConnection;
	}

}
	
