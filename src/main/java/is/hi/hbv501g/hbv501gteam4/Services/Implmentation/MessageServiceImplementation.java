package is.hi.hbv501g.hbv501gteam4.Services.Implmentation;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Message;
import is.hi.hbv501g.hbv501gteam4.Repositories.MessageRepository;
import is.hi.hbv501g.hbv501gteam4.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImplementation implements MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImplementation(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public List<Message> findByConversationID(long id) {
        return messageRepository.findByConversationID(id);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
