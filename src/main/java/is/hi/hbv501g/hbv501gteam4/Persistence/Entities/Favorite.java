package is.hi.hbv501g.hbv501gteam4.Persistence.Entities;


import jakarta.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Disc disc;

    public Favorite() {
    }

    public Favorite(User user, Disc disc) {
        this.user = user;
        this.disc = disc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }

    public Long getId() {
        return id;
    }
}