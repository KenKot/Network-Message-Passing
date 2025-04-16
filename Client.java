
import java.io.*;
import java.net.Socket;

public class Client {
//	private static int count = 0;
	private final String id;

	private final int port;
	private final String hostIP;

	private final Socket socket;

	public Client(int port, String hostIP, String id) throws IOException {
		this.port = port;
		this.hostIP = hostIP;
		this.id = id; // will be from client argument

		this.socket = new Socket(hostIP, port);

		Message m1 = new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.LOGIN, "HELLO THERE");
	}

//	TESTER - TO DELETE
	public void log() {
		System.out.println("LOGGER - TO DELETE");
		System.out.println("Client ID: " + this.id + " PORT: " + this.port + " IP: " + this.hostIP);

	}

//	private void sendMessage() {}

	public static void main(String[] args) {
		try {
			Client c1 = new Client(4321, "127.0.0.1", "A");
			c1.log();
		} catch (IOException e) {
			System.out.println("error connecting: " + e);
		}
	}
}
