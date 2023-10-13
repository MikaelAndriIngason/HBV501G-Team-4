package is.hi.hbv501g.hbv501gteam4.Repositories;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DiscRepository extends JpaRepository<Disc, Long> {
    List<Disc> findByName(String name);
    List<Disc> findAll();
    Disc findBydiscID(long id);
    Disc save(Disc disc);
    void delete(Disc disc);
}
