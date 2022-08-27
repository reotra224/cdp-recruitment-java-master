package adeo.leroymerlin.cdp;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String title;

    private String imgUrl;

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Band> bands;

    private Integer nbStars;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
    }

    public Integer getNbStars() {
        return nbStars;
    }

    public void setNbStars(Integer nbStars) {
        this.nbStars = nbStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id) && title.equals(event.title) && imgUrl.equals(event.imgUrl) && Objects.equals(nbStars, event.nbStars) && Objects.equals(comment, event.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, imgUrl, nbStars, comment);
    }
}
