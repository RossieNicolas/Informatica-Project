package be.ap.tagform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.validation.Valid;

@Controller
public class TagController {
    @Autowired
    TagRepo tagRepo;

    /*private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Tag tag) {
        String sql = "INSERT INTO TAGS "
                + "(tag_id, tag_name, tag_description) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tag.getTag_id());
            ps.setString(2, tag.getTag_name());
            ps.setString(3, tag.getTag_description());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }*/
    @RequestMapping("/tag")
    public String assignment() {
        getConnectionToDb();
        return "tag";
    }

    @PostMapping("/tag")
    public Tag createTag(@Valid Tag tag){
        return tagRepo.save(tag);
    }

    public Connection getConnectionToDb() {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlserver://192.168.84.92;databaseName=crediti;username=sa;password=Zxcvb0123");
            System.out.println("connected");

            return c;

        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

}
