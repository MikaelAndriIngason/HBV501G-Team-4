package is.hi.hbv501g.hbv501gteam4.Repositories;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Favorite;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite save(Favorite favorite);
    void delete(Favorite favorite);
    List<Favorite> findByUser(User user);
    Favorite findByUserAndDisc(User user, Disc disc);
    Favorite findById(long id);
}
