package is.hi.hbv501g.hbv501gteam4.Repositories;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User save(User user);
    void delete(User user);
    List<User> findAll();
}
