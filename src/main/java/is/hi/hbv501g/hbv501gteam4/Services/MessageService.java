package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    List<Message> findByConversationID(long id);
    Message save(Message message);
}
