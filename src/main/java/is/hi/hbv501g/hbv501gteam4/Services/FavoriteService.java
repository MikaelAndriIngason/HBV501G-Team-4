package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Favorite;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {
    Favorite save(Favorite favorite);
    void delete(Favorite favorite);
    Favorite findById(long id);
    List<Favorite> findFavoritesByUser(User user);
    Favorite findFavoriteByUserAndDisc(User user, Disc disc);
}
