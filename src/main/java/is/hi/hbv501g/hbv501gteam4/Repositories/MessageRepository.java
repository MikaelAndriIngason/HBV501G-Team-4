package is.hi.hbv501g.hbv501gteam4.Repositories;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    /*List<Disc> findByName(String name);
    List<Disc> findAll();
    Disc findBydiscID(long id);
    Disc save(Disc disc);
    void delete(Disc disc);*/

    List<Message> findByConversationID(long id);
    Message save(Message message);
}