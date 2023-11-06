package is.hi.hbv501g.hbv501gteam4.Persistence.Entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageID;

    private long conversationID;
    private long senderID;
    private String message;
    private ZonedDateTime sentAt;
    private boolean read;

    public Message(long conversationID, long senderID, String message) {
        this.conversationID = conversationID;
        this.senderID = senderID;
        this.message = message;
        this.sentAt = ZonedDateTime.now();
        this.read = false;
    }

    public Message() {}

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public long getConversationID() {
        return conversationID;
    }

    public void setConversationID(long conversationID) {
        this.conversationID = conversationID;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
