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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;
	private JTextField textField;
	private JButton btnEnviar;
	
	private static DataOutputStream ou;
	private static BufferedReader in;
	private static Socket s;
	private static String message,name;
	private static boolean end;
	private static ClientThread cl;

	public VentanaCliente() {
		lookAndFeel();
		setTitle("Client");
		initComponents();
		createEvents();
		setVisible(true);
		
		try{
			s=new Socket("localhost",25);
			ou=new DataOutputStream(s.getOutputStream());
			in=new BufferedReader(new InputStreamReader(System.in));
			end=false;			
		}catch(IOException io){
			io.printStackTrace();
		}
		try{			
			System.out.print("Input you name: ");
			name=in.readLine();
			cl=new ClientThread(s,name);
			cl.start();
			ou.writeUTF(name);//send 1 ok
		}catch(IOException io1){
			System.out.println("Error input text");
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
		//código del cliente
		
	}
	
	public void createEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//client.close();
				
			}
			@Override
			public void windowOpened(WindowEvent e) {
				while(!end){
					try{
						setMessage(in.readLine());				
						ou.writeUTF(getMessage());
						modificarTexto(getMessage());
						if(getMessage().equalsIgnoreCase("exit")){
							end=true;
						}
					}catch(IOException io2){
						System.out.println("Error in loop");
					}
				}
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//if(e.getKeyCode() == KeyEvent.VK_ENTER) sendMessage();
			}
		});
		
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//sendMessage();
			}
		});
	}
	
	private void modificarTexto(String s) {
		if(textPane.getText().equals("")) textPane.setText(s);
		else textPane.setText(textPane.getText() + "\n" + s);
	}
	
	public String getMessage() {
		return VentanaCliente.message;
	}
	
	public void setMessage(String message) {
		VentanaCliente.message = message;
	}
}
