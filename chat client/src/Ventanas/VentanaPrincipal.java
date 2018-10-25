package Ventanas;

import Client.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;
	private JTextField textField;
	private JButton btnEnviar;
	
	private Client client;

	public VentanaPrincipal() {
		lookAndFeel();
		setTitle("Client");
		initComponents();
		createEvents();
		setVisible(true);
		client = new Client();
		new Thread(new Runnable() {
			public void run() {
				while(true)
					receiveMessage();
			}
		}).start();
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
		textPane.setBounds(10, 11, 414, 208);
		textPane.setEditable(false);
		contentPane.add(textPane);
		
		textField = new JTextField();
		textField.setBounds(10, 230, 315, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(335, 229, 89, 23);
		contentPane.add(btnEnviar);
		
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
	
	public void createEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//client.close();
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) sendMessage();
			}
		});
		
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
	}
	
	private void modificarTexto(String s) {
		if(textPane.getText().equals("")) textPane.setText(s);
		else textPane.setText(textPane.getText() + "\n" + s);
	}
	
	private void receiveMessage() {
		if(client.receiveMessage() != null) modificarTexto(client.receiveMessage());
	}
	
	private void sendMessage() {
		String message = textField.getText().trim();
		modificarTexto(message);
		client.sendMessage(message);
		textField.setText("");
	}
}
