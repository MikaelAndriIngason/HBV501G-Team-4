package is.hi.hbv501g.hbv501gteam4.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


//ekki viss hérna fékk bæði á chatgpt og frá vini að e'ð álíka hjálpaði að
//lesa af database en veit ekki þurfum að finna útúr því
@Repository
@Transactional(readOnly = true)
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(string email);
}
