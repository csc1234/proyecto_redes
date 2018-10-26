package chat_server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

public class server_frame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;

	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea ta_chat;
	ArrayList<PrintWriter> clientOutputStreams;
	ArrayList<String> users;

	public server_frame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				b_endActionPerformed(evt);
			}
		});
		lookAndFeel();
		initComponents();
		b_startActionPerformed();
	}
	
	public void lookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		ta_chat = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Pantalla Server");
		setName("server");
		setResizable(false);

		ta_chat.setColumns(20);
		ta_chat.setRows(5);
		ta_chat.setEditable(false);
		jScrollPane1.setViewportView(ta_chat);

		getContentPane().setLayout(new MigLayout("", "[89.00px][65px][57.00px][74.00px]", "[280.00px]"));
		getContentPane().add(jScrollPane1, "cell 0 0 4 1,grow");

		pack();
	}
	
	private void b_startActionPerformed() {
		Thread starter = new Thread(new ServerStart());
		starter.start();
		ta_chat.append("Server conectado...\n");
	}
	
	private void b_endActionPerformed(WindowEvent evt) {
		tellEveryone("Server: El server se va a cerrar y todos los clientes serán desconectados.\n:Chat");
		ta_chat.append("Cerrando el server... \n");
		ta_chat.setText("");
	}

	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		PrintWriter client;

		public ClientHandler(Socket clientSocket, PrintWriter user) {
			client = user;
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {
				ta_chat.append("Error Inesperado... \n");
			}

		}

		@Override
		public void run() {
			String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat";
			String[] data;

			try {
				while ((message = reader.readLine()) != null) {
					ta_chat.append("Recibido: " + message + "\n");
					data = message.split(":");

					for (String token : data) {
						ta_chat.append(token + "\n");
					}

					if (data[2].equals(connect)) {
						tellEveryone((data[0] + ":" + data[1] + ":" + chat));
						userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						tellEveryone((data[0] + ":has disconnected." + ":" + chat));
						userRemove(data[0]);
					} else if (data[2].equals(chat)) {
						tellEveryone(message);
					} else {
						ta_chat.append("No Conditions were met. \n");
					}
				}
			} catch (Exception ex) {
				ta_chat.append("Conexión perdida. \n");
				// ex.printStackTrace();
				clientOutputStreams.remove(client);
			}
		}
	}

	public void userAdd(String data) {
		String message, add = ": :Connect", done = "Server: :Done", name = data;
		ta_chat.append("Before " + name + " added. \n");
		users.add(name);
		ta_chat.append("After " + name + " added. \n");
		String[] tempList = new String[(users.size())];
		users.toArray(tempList);

		for (String token : tempList) {
			message = (token + add);
			tellEveryone(message);
		}
		tellEveryone(done);
	}

	public void userRemove(String data) {
		String message, add = ": :Connect", done = "Server: :Done", name = data;
		users.remove(name);
		String[] tempList = new String[(users.size())];
		users.toArray(tempList);

		for (String token : tempList) {
			message = (token + add);
			tellEveryone(message);
		}
		tellEveryone(done);
	}

	public void tellEveryone(String message) {
		Iterator<PrintWriter> it = clientOutputStreams.iterator();

		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				ta_chat.append("Enviando: " + message + "\n");
				writer.flush();
				ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

			} catch (Exception ex) {
				ta_chat.append("Error al responder a todos. \n");
			}
		}
	}

	public class ServerStart implements Runnable {
		private ServerSocket serverSock;

		@Override
		public void run() {
			clientOutputStreams = new ArrayList<PrintWriter>();
			users = new ArrayList<String>();

			try {
				serverSock = new ServerSocket(2222);

				while (true) {
					Socket clientSock = serverSock.accept();
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					clientOutputStreams.add(writer);

					Thread listener = new Thread(new ClientHandler(clientSock, writer));
					listener.start();
					ta_chat.append("Se ha realizado una conexión. \n");
				}
			} catch (Exception ex) {
				ta_chat.append("Error creando la conexión. \n");
			}
		}
	}
}
