package is.hi.hbv501g.hbv501gteam4.Services.Implmentation;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Conversation;
import is.hi.hbv501g.hbv501gteam4.Repositories.ConversationRepository;
import is.hi.hbv501g.hbv501gteam4.Services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImplementation implements ConversationService {
    private ConversationRepository conversationRepository;

    @Autowired
    public ConversationServiceImplementation(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Conversation findByConversationID(long id) {
        return conversationRepository.findByConversationID(id);
    }

    @Override
    public List<Conversation> findBySellerIdOrBuyerId(long userID) {
        return conversationRepository.findBySellerIdOrBuyerId(userID);
    }

    @Override
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
