package is.hi.hbv501g.hbv501gteam4.Persistence.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long conversationID;

    private long buyerID;
    private long sellerID;
    private boolean conversationEnded;
    private String conversationTitle;

    public Conversation(long buyerID, long sellerID, String conversationTitle) {
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.conversationEnded = false;
        this.conversationTitle = conversationTitle;
    }

    public Conversation() {}

    public long getConversationID() {
        return conversationID;
    }

    public void setConversationID(long conversationID) {
        this.conversationID = conversationID;
    }

    public long getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(long buyerID) {
        this.buyerID = buyerID;
    }

    public long getSellerID() {
        return sellerID;
    }

    public void setSellerID(long sellerID) {
        this.sellerID = sellerID;
    }

    public boolean isConversationEnded() {
        return conversationEnded;
    }

    public void setConversationEnded(boolean conversationEnded) {
        this.conversationEnded = conversationEnded;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }
}
