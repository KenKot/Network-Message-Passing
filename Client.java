public class Client {
//	private static int count = 0;
	private final String id;

	private final int PORT;
	private final String IP;
	
//	public Client(int PORT, String IP) { // w/o id
	public Client(int PORT, String IP, String id) {
		this.PORT = PORT;
		this.IP = IP;
		
		this.id = id;
		

		Message m1 = new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.LOGIN, "HELLO THERE");
	}
	
//	TESTER - TO DELETE
	public void log() {
		System.out.println("LOGGER - TO DELETE");
		System.out.println("Client ID: " + this.id + " PORT: " + this.PORT + " IP: " + this.IP);
		
	}
	
//	private void sendMessage() {}
	
	public static void main(String[] args) {
		Client c1 = new Client(4321, "127.0.0.1", args[0]);
		System.out.println("Client's main ran");
		
		c1.log();
//		while(true) {}
		
	}
}
