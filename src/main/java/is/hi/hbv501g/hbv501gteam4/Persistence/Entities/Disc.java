package is.hi.hbv501g.hbv501gteam4.Persistence.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discs")
public class Disc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long discID;

    private String name;
    private String description;
    private String type;
    private String condition;

    private String colour;

    private int price;


    @OneToMany(mappedBy = "disc", cascade = CascadeType.ALL)
    List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "disc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();



    public long getDiscID() {
        return discID;
    }

    public void setDiscID(long discID) {
        this.discID = discID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getCondition() {
        return condition;
    }

    public int getPrice() {
        return price;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        if (user != null)
            return user.getId();
        else
            return (long) -1;
    }

    public String getColour(){
        return colour;
    }

    public void setColour(String colour) { this.colour = colour;}


}
