package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Conversation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConversationService {
    Conversation findByConversationID(long id);
    List<Conversation> findBySellerIdOrBuyerId(long userID);
    Conversation save(Conversation conversation);
}
