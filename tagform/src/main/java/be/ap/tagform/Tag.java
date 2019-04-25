package be.ap.tagform;
import javax.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private int tag_id;

    @Column(name = "tag_name")
    private String tag_name;
    @Column(name = "tag_description")
    private String tag_description;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_description() {
        return tag_description;
    }

    public void setTag_description(String tag_description) {
        this.tag_description = tag_description;
    }

    public Tag(String tag_name, String tag_description) {
        this.tag_name = tag_name;
        this.tag_description = tag_description;
    }
    public Tag(){
        
    }
}