import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
import java.net.*;

public class Server {
	private final int port;

	private final ServerSocket serverSocket;

	public Server(int port) throws IOException {
		this.port = port;

		// move into 1 function
		this.serverSocket = new ServerSocket(port);
		this.serverSocket.setReuseAddress(true);
		while (true) {
			Socket client = this.serverSocket.accept();

			System.out.println("New client connected" + client.getInetAddress().getHostAddress());

			// create a new thread object
			ClientHandler clientSock = new ClientHandler(client);

			// This thread will handle the client
			// separately
			new Thread(clientSock).start();
		}

//		Message m1 = new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.LOGIN, "HELLO THERE");
	}

//	TESTER - TO DELETE
	public void log() {
		System.out.println("Server fired up!");
	}

	public static void main(String[] args) {
		try {
			Server s1 = new Server(4321);
			s1.log();
		} catch (IOException e) {
			System.out.println("error connecting: " + e);
		}
	}
}
