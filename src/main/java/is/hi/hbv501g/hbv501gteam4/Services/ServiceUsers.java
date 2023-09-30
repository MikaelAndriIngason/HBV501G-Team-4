package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Repositories.RepositoryUsers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceUsers {

    private RepositoryUsers repository;

    public ServiceUsers(RepositoryUsers repository) {
        this.repository = repository;
    }

    public List<User> getRepository() {
        return this.repository.findAll();
    }

    public User createUser(User user) {
        return this.repository.save(user);
    }

    public boolean checkIfUserExists(String email) {
        Optional<User> user = this.repository.findByEmail(email);
        return user.isPresent();
    }
}
