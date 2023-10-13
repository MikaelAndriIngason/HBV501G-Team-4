package is.hi.hbv501g.hbv501gteam4.Services;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Image;
import org.springframework.stereotype.Service;

@Service
public interface ImageService {
    Image findByimageID(long id);
    Image save(Image image);
    void delete(Image image);
}
