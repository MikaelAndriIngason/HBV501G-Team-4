package is.hi.hbv501g.hbv501gteam4.Services.Implmentation;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Favorite;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Repositories.FavoriteRepository;
import is.hi.hbv501g.hbv501gteam4.Services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImplementation implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteServiceImplementation(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }


    @Override
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    @Override
    public Favorite findById(long id) {
        return favoriteRepository.findById(id);
    }

    @Override
    public List<Favorite> findFavoritesByUser(User user) {
        return favoriteRepository.findByUser(user);
    }

    @Override
    public Favorite findFavoriteByUserAndDisc(User user, Disc disc) {
        return favoriteRepository.findByUserAndDisc(user, disc);
    }


}
