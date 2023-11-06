package is.hi.hbv501g.hbv501gteam4.Persistence.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageID;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    private Disc disc;

    public Image(){}

    public Image(String image, Disc disc) {
        this.image = image;
        this.disc = disc;
    }

    public long getImageID() {
        return imageID;
    }

    public void setImageID(long imageID) {
        this.imageID = imageID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
