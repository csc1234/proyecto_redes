import chat_server.server_frame;

public class MainServer {
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new server_frame().setVisible(true);
			}
		});
	}

}