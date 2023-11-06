package is.hi.hbv501g.hbv501gteam4.Repositories;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findByConversationID(long id);
    @Query("SELECT c FROM Conversation c WHERE c.sellerID = :userID OR c.buyerID = :userID")
    List<Conversation> findBySellerIdOrBuyerId(long userID);
    Conversation save(Conversation conversation);
}