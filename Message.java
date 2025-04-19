import java.io.Serializable;

public class Message implements Serializable {
	private final MessageStatusEnum status;
	private final MessageTypeEnum type;
	private final String content;
	
	public Message(MessageStatusEnum status, MessageTypeEnum type, String content) {
		this.status = status;
		this.type = type;
		this.content = content;
	}
	
	
	public MessageStatusEnum getStatus() {
		return this.status;
	}

	public MessageTypeEnum getType() {
		return this.type;
	}
	
	public String getContent() {
		return this.content;
	}
}
