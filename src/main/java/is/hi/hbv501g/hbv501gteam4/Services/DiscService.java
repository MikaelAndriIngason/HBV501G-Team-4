package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DiscService {

    List<Disc> findByName(String name);
    List<Disc> findAll();
    Disc findBydiscID(long id);
    Disc save(Disc disc);
    void delete(Disc disc);

}
