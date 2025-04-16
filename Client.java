public class Client {
	private final int PORT;
	private final String IP;
	
	public Client(int PORT, String IP) {
		this.PORT = PORT;
		this.IP = IP;
		

		Message m1 = new Message(MessageStatusEnum.SUCCESS, MessageTypeEnum.LOGIN, "HELLO THERE");
	}
	
//	TESTER - TO DELETE
	public void log() {
		System.out.println("LOGGER - TO DELETE");
		System.out.println("PORT: " + this.PORT + " IP: " + this.IP);
		
	}
	
//	private void sendMessage() {}
	
	public static void main(String[] args) {
		Client c1 = new Client(4321, "127.0.0.1");
		System.out.println("Client's main ran");
		
		c1.log();
		
	}
}
