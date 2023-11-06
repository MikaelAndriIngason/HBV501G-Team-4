package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DiscService {

    List<Disc> findByName(String name);
    List<Disc> findAll();
    List<Disc> findByPrice(int fromPrice, int toPrice);
    List<Disc> findByColour(String colour);
    List<Disc> findByCondition(String condition);
    Disc findBydiscID(long id);
    Disc save(Disc disc);
    void delete(Disc disc);

    List<Disc> findByNameContainingIgnoreCase(String partialName);

}
