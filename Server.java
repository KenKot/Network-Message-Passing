import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
	private final int port;
	private final ServerSocket serverSocket;

	private static final AtomicInteger clientCount = new AtomicInteger(0);
	private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

	public Server(int port) throws IOException {
		this.port = port;
		this.serverSocket = new ServerSocket(port);
		this.serverSocket.setReuseAddress(true);

		log();

		while (true) {
			try {
				Socket client = this.serverSocket.accept();
				String clientId = String.valueOf(clientCount.getAndIncrement());

				System.out.println("[" + clientId + "] Connected from " + client.getInetAddress().getHostAddress());

				ClientHandler clientHandler = new ClientHandler(client, clientId);

				Thread thread = new Thread(clientHandler);
				thread.start();

				clients.put(clientId, clientHandler);
			} catch (IOException e) {
				System.out.println("ERROR!");
				System.out.println(".getMessage() = " + e.getMessage() + "\n");
				System.out.println("e.printStackTrace() = ");
				e.printStackTrace();
			}
		}
	}

	public void log() {
		String ipAddress;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Server fired up from port: " + this.port + ", & this IP : " + ipAddress);
			System.out.println("Use ifconfig in terminal to get real ip");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final String clientId;
		private ObjectOutputStream outgoingStream;
		private ObjectInputStream incomingStream;

		public ClientHandler(Socket socket, String clientId) {
			this.clientSocket = socket;
			this.clientId = clientId;
		}

		public String getId() {
			return this.clientId;
		}

		public void run() {
			try {
				this.outgoingStream = new ObjectOutputStream(clientSocket.getOutputStream());
				this.incomingStream = new ObjectInputStream(clientSocket.getInputStream());

				Message login = (Message) incomingStream.readObject();
				System.out.println("[" + clientId + "] Login attempt: " + login.getContent());

				if (login.getType() != MessageTypeEnum.LOGIN) {
					System.out.println("[" + clientId + "] Expected LOGIN. Closing connection.");
					clientSocket.close();
					return;
				}

				sendMessage(new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.LOGIN, clientId, "SERVER"));

				while (true) {
					Message receivedMsg = (Message) incomingStream.readObject();
					System.out.println(
							"[" + clientId + "] Received " + receivedMsg.getType() + ": " + receivedMsg.getContentCapitalized());

					if (receivedMsg.getType() == MessageTypeEnum.LOGOUT) {
						sendMessage(new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.LOGOUT, "logging out",
								"SERVER"));
						break;
					}

					if (receivedMsg.getType() == MessageTypeEnum.TEXT) {
						String capitalizedContent = receivedMsg.getContentCapitalized();
						Message outgoingMessage = new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.TEXT,
								capitalizedContent, clientId);

						for (ClientHandler ch : clients.values()) {
							if (!ch.getId().equals(receivedMsg.getSenderId())) {
								ch.sendMessage(outgoingMessage);
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("ERROR!");
				System.out.println(".getMessage() = " + e.getMessage() + "\n");
				System.out.println("e.printStackTrace() = ");
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					System.out.println("ERROR!");
					System.out.println(".getMessage() = " + e.getMessage() + "\n");
					System.out.println("e.printStackTrace() = ");
					e.printStackTrace();
				}

				clients.remove(clientId);
				System.out.println("[" + clientId + "] Disconnected.");
			}
		}

		public void sendMessage(Message msg) {
			try {
				outgoingStream.writeObject(msg);
				outgoingStream.flush();
			} catch (IOException e) {
				System.out.println("ERROR!");
				System.out.println(".getMessage() = " + e.getMessage() + "\n");
				System.out.println("e.printStackTrace() = ");
				e.printStackTrace();
			}
		}
	} // end of ClientHandler definition

	public static void main(String[] args) {
		try {
			Server s1 = new Server(4321);
		} catch (Exception e) {
			System.out.println("ERROR!");
			System.out.println(".getMessage() = " + e.getMessage() + "\n");
			System.out.println("e.printStackTrace() = ");
			e.printStackTrace();
		}
	}
}
