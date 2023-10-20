package is.hi.hbv501g.hbv501gteam4.Services.Implmentation;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Image;
import is.hi.hbv501g.hbv501gteam4.Repositories.ImageRepository;
import is.hi.hbv501g.hbv501gteam4.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImplementation implements ImageService {

    ImageRepository imageRepository;

    @Autowired
    public ImageServiceImplementation(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image findByimageID(long id) {
        return imageRepository.findByimageID(id);
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void delete(Image image) {
        imageRepository.delete(image);
    }
}
