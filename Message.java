import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Message implements Serializable {
	private static final AtomicInteger count = new AtomicInteger(0);

	private final MessageStatusEnum status;
	private final MessageTypeEnum type;
	private final String content;
	private final String senderId;
	private final String countId;
	private final LocalDateTime timeCreated;

	public Message(MessageStatusEnum status, MessageTypeEnum type, String content, String senderId) {
		this.countId = String.valueOf(count.incrementAndGet());
		this.senderId = senderId;
		this.status = status;
		this.type = type;
		this.content = content;
		this.timeCreated = LocalDateTime.now();

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

	public String getContentCapitalized() {
		return this.content.toUpperCase();
	}

	public String getSenderId() {
		return this.senderId;
	}

	public String getCountId() {
		return this.countId;
	}

	public LocalDateTime getTimeCreated() {
		return this.timeCreated;
	}
}
