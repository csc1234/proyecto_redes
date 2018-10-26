import chat_client.client_frame;

public class MainClient {
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new client_frame().setVisible(true);
			}
		});
	}
}