import java.io.*;
import java.net.Socket;
import java.util.Scanner;

// MessageTypeEnum = LOGIN, TEXT, LOGOUT
// MessageStatusEnum = NONE, SUCCESS, FAILURE

public class Client {
	private String id = null; // Assigned after login (server returns value)
	private final int port;
	private final String hostIP;
	private Socket socket;

	private ObjectInputStream incomingStream;
	private ObjectOutputStream outgoingStream;

	private volatile boolean isConnected = false;

	public Client(int port, String hostIP) {
		this.port = port;
		this.hostIP = hostIP;

		try {
			this.socket = new Socket(hostIP, port);
			this.incomingStream = new ObjectInputStream(this.socket.getInputStream());
			this.outgoingStream = new ObjectOutputStream(this.socket.getOutputStream());

//			log();
			login();
			start();
//			socket.close();
		} catch (Exception e) {
			System.out.println("ERROR!");
			System.out.println(".getMessage() = " + e.getMessage() + "\n");
			System.out.println("e.printStackTrace() = ");
			e.printStackTrace();
		}
	}

// start new thread before, add 1 second sleep to retry logins?	
	private void login() {
		try {
//			args:      (MessageStatusEnum status, MessageTypeEnum type, String content, String senderId) 
			sendMessage(MessageStatusEnum.NONE, MessageTypeEnum.LOGIN, "n/a", "pending client");

			Message res = (Message) incomingStream.readObject();

			if (res.getType() == MessageTypeEnum.LOGIN && res.getStatus() == MessageStatusEnum.SUCCESS) {
				this.id = res.getContent(); // assign ID to client!
				this.isConnected = true;
//				System.out.println("Assigned client ID: " + this.id);
				log();
			} else {
				System.out.println("Login failed.");
				socket.close();
			}
		} catch (Exception e) {
			System.out.println("ERROR!");
			System.out.println(".getMessage() = " + e.getMessage() + "\n");
			System.out.println("e.printStackTrace() = ");
			e.printStackTrace();
		}
	}

	private void start() {
		Thread listener = new Thread(() -> {
			try {
				while (isConnected) {
					Message messageFromServer = (Message) incomingStream.readObject();
					System.out.println("[" + messageFromServer.getSenderId() + "]: " + messageFromServer.getContent());
				}
			} catch (Exception e) {
				if (isConnected) {
					System.out.println("ERROR!");
					System.out.println(".getMessage() = " + e.getMessage() + "\n");
					System.out.println("e.printStackTrace() = ");
					e.printStackTrace();
				}
			}
		});
		listener.start();

		// in Main thread
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("Enter message (or type \"q!\" to logout): ");
				String userInput = scanner.nextLine();

				if (userInput.equalsIgnoreCase("q!")) {
					sendMessage(MessageStatusEnum.NONE, MessageTypeEnum.LOGOUT, "N/A", this.id);
					isConnected = false;
					System.out.println("Logged out!");
//					break;
					return;
					
				} else {
					
					sendMessage(MessageStatusEnum.NONE, MessageTypeEnum.TEXT, userInput, this.id);
				}

			}
		} catch (Exception e) {
			System.out.println("ERROR!");
			System.out.println(".getMessage() = " + e.getMessage() + "\n");
			System.out.println("e.printStackTrace() = ");
			e.printStackTrace();
		}
	}

	public void sendMessage(MessageStatusEnum status, MessageTypeEnum type, String content, String senderId) {
		try {
			Message m = new Message(status, type, content, senderId);
			outgoingStream.writeObject(m);
			outgoingStream.flush();
		} catch (Exception e) {
			System.out.println("ERROR!");
			System.out.println(".getMessage() = " + e.getMessage() + "\n");
			System.out.println("e.printStackTrace() = ");
			e.printStackTrace();
		}
	}

	public void log() {
		System.out.println("Client ID: " + this.id + " PORT: " + this.port + " IP: " + this.hostIP);
	}

	public static void main(String[] args) {
		try {
			String ipAddress = "127.0.0.1";
			if (args.length == 1) {
				ipAddress = args[0];
			}

			Client c1 = new Client(4321, ipAddress);
		} catch (Exception e) {
			System.out.println("ERROR!");
			System.out.println(".getMessage() = " + e.getMessage() + "\n");
			System.out.println("e.printStackTrace() = ");
			e.printStackTrace();
		}
	}
}