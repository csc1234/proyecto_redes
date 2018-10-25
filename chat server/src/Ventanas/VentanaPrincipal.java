package Ventanas;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import Server.Server;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;

	private Server server;

	public VentanaPrincipal() {
		lookAndFeel();
		setTitle("Server");
		initComponents();
		createEvents();
		setVisible(true);
		
		server = new Server();
		
		new Thread(new Runnable() {
			public void run() {
				while(true && server != null)
					server.connection();
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				while(true)
					receiveMessage();
			}
		}).start();
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

	public void initComponents() {
		contentPane = new JPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textPane = new JTextPane();
		textPane.setForeground(Color.CYAN);
		textPane.setBackground(Color.BLACK);
		textPane.setBounds(10, 11, 414, 239);
		textPane.setEditable(false);
		contentPane.add(textPane);
		
	}

	public void createEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//server.close();
			}
		});
	}
	
	private void modificarTexto(String s) {
		if(textPane.getText().equals("")) textPane.setText(s);
		else textPane.setText(textPane.getText() + "\n" + s);
	}

	private void receiveMessage() {
		String receive = server.receiveMessage();
		if(server != null && receive != null) {
			modificarTexto(receive);
			server.sendMessage(receive);
		}
	}

}
