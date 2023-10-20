package is.hi.hbv501g.hbv501gteam4.Services.Implmentation;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Repositories.DiscRepository;
import is.hi.hbv501g.hbv501gteam4.Services.DiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscServiceImplementation implements DiscService {

    private DiscRepository discRepository;

    @Autowired
    public DiscServiceImplementation(DiscRepository discRepository) {
        this.discRepository = discRepository;
    }

    @Override
    public List<Disc> findByName(String name) {
        return discRepository.findByName(name);
    }

    @Override
    public List<Disc> findAll() {
        return discRepository.findAll();
    }

    @Override
    public Disc findBydiscID(long id) {
        return discRepository.findBydiscID(id);
    }

    @Override
    public Disc save(Disc disc) {
        return discRepository.save(disc);
    }

    @Override
    public void delete(Disc disc) {
        discRepository.delete(disc);
    }
}
