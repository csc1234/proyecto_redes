package chat_client;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class client_frame extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;

	private javax.swing.JButton b_send;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea ta_chat;
	private javax.swing.JTextField tf_chat;

	String username;
	ArrayList<String> users = new ArrayList<String>();
	int port = 2222;
	Boolean isConnected = false;

	Socket sock;
	BufferedReader reader;
	PrintWriter writer;

	public client_frame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				b_disconnectActionPerformed(evt);
			}
		});
		lookAndFeel();
		initComponents();
		b_anonymousActionPerformed();
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
		tf_chat = new javax.swing.JTextField();
		b_send = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chat - Cliente");
		setName("client"); // NOI18N
		setResizable(false);

		ta_chat.setColumns(20);
		ta_chat.setRows(5);
		ta_chat.setEditable(false);
		jScrollPane1.setViewportView(ta_chat);

		b_send.setText("Enviar");
		b_send.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_sendActionPerformed(evt);
			}
		});
		getContentPane().setLayout(new MigLayout("",
				"[62px][18px][89px][18px][56px][10px][50px][6px][43px][6px][24px][2px]", "[310px][31px]"));
		getContentPane().add(tf_chat, "cell 0 1 10 1,grow");
		getContentPane().add(b_send, "cell 10 1 2 1,grow");
		getContentPane().add(jScrollPane1, "cell 0 0 12 1,grow");
		pack();
	}
	
	public void ListenThread() {
		Thread IncomingReader = new Thread(new IncomingReader());
		IncomingReader.start();
	}

	public void userAdd(String data) {
		users.add(data);
	}

	public void userRemove(String data) {
		ta_chat.append(data + " is now offline.\n");
	}

	public void sendDisconnect() {
		String bye = (username + ": :Disconnect");
		try {
			writer.println(bye);
			writer.flush();
		} catch (Exception e) {
			ta_chat.append("Could not send Disconnect message.\n");
		}
	}

	public void Disconnect() {
		try {
			ta_chat.append("Desconectado.\n");
			sock.close();
		} catch (Exception ex) {
			ta_chat.append("Error al desconectar. \n");
		}
		isConnected = false;
	}

	public class IncomingReader implements Runnable {
		@Override
		public void run() {
			String[] data;
			String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

			try {
				while ((stream = reader.readLine()) != null) {
					data = stream.split(":");

					if (data[2].equals(chat)) {
						ta_chat.append(data[0] + ": " + data[1] + "\n");
						ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
					} else if (data[2].equals(connect)) {
						ta_chat.removeAll();
						userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						userRemove(data[0]);
					} else if (data[2].equals(done)) {
						users.clear();
					}
				}
			} catch (Exception ex) {
			}
		}
	}

	private void b_disconnectActionPerformed(WindowEvent evt) {// GEN-FIRST:event_b_disconnectActionPerformed
		sendDisconnect();
		Disconnect();
	}

	private void b_anonymousActionPerformed() {
		if (isConnected == false) {
			String anon = "anon";
			Random generator = new Random();
			int i = generator.nextInt(999) + 1;
			String is = String.valueOf(i);
			anon = anon.concat(is);
			username = anon;

			try {
				// sock = new Socket(address, port);
				sock = new Socket("127.0.0.1", 2222);
				InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(streamreader);
				writer = new PrintWriter(sock.getOutputStream());
				writer.println(anon + ":te has conectado.:Connect");
				writer.flush();
				isConnected = true;
			} catch (Exception ex) {
				ta_chat.append("Conexión fallida! Reintente. \n");
			}

			ListenThread();

		} else if (isConnected == true) {
			ta_chat.append("Ya estás conectado. \n");
		}
	}

	private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_b_sendActionPerformed
		String nothing = "";
		if ((tf_chat.getText()).equals(nothing)) {
			tf_chat.setText("");
			tf_chat.requestFocus();
		} else {
			try {
				writer.println(username + ":" + tf_chat.getText() + ":" + "Chat");
				writer.flush(); // flushes the buffer
			} catch (Exception ex) {
				ta_chat.append("Mensaje no enviado. \n");
			}
			tf_chat.setText("");
			tf_chat.requestFocus();
		}

		tf_chat.setText("");
		tf_chat.requestFocus();
	}
	
}
