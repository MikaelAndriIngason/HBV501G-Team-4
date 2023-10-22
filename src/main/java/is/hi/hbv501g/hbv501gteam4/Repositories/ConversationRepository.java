package is.hi.hbv501g.hbv501gteam4.Repositories;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    /*List<Disc> findByName(String name);
    List<Disc> findAll();
    Disc findBydiscID(long id);
    Disc save(Disc disc);
    void delete(Disc disc);*/

    Conversation findByConversationID(long id);
    @Query("SELECT c FROM Conversation c WHERE c.sellerID = :userID OR c.buyerID = :userID")
    List<Conversation> findBySellerIdOrBuyerId(long userID);
    Conversation save(Conversation conversation);
    //void endConversation(Conversation conversation);
}