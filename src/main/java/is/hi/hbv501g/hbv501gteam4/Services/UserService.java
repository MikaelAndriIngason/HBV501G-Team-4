package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User save(User user);
    void delete(User user);
    List<User> findAll();
    User findByEmail(String email);
    User findById(long id);
    User login(User user);
}
