package com.architec.crediti;

import com.architec.crediti.models.*;
import com.architec.crediti.repositories.*;
import com.architec.crediti.security.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {

    @Autowired
    AssignmentRepository assignments;

    @Autowired
    ArchiveRepository archive;

    @Autowired
    UserRepository users;

    @Autowired
    StudentRepository students;

    @Autowired
    ExternalUserRepository externalUsers;

    @Autowired
    TagRepo tags;

    @Test
    public void createEditValidateArchiveAssignment(){

        //arrange
        User u = new User("test","test","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        ArchivedAssignment ar = new ArchivedAssignment();
        String input = "success";

        //act
        assignments.save(a); // create assignment
        a.setTitle(input); // edit assignment
        assignments.save(a);
        a.setValidated(true); // validate assignment
        assignments.save(a);
        ar.fillArchivedAssignment(a); // archive assignment
        archive.save(ar);

        //assert
        assertEquals(a.getTitle(),ar.getTitle());

        //undo operations
        archive.delete(ar);
        assignments.delete(a);
        users.delete(u);
    }

    @Test
    public void createUserMakeStudentEditStudent(){

        //arrange
        User u = new User("test","test","test@test.test", Role.STUDENT,false);
        Student s = new Student("0000000000","000000",u);
        String input = "success";

        //act
        users.save(u); // create user
        students.save(s); // create student
        s.setStudentennummer(input); // edit student
        students.save(s);

        //assert
        assertEquals(input, s.getStudentennummer());

        //undo operations
        students.delete(s);
        users.delete(u);
    }

    @Test
    public void createEditFindExternalUser(){

        //arrange
        char[] password = new char[10];
        byte[] salt = new byte[10];
        ExternalUser e = new ExternalUser("test", "test", "test","00000000000","test","test","test", password, salt);
        String input = "success";

        //act
        externalUsers.save(e); // create external user
        e.setFirstname(input);
        externalUsers.save(e); // edit external user
        ExternalUser found = externalUsers.findByUserId(e.getUserId()); // find external user

        //assert
        assertEquals(input, found.getFirstname());

        //undo operations
        externalUsers.delete(e);
    }

    @Test
    public void findUserFourDifferentWays(){

        //arrange
        String firstName= "testFindUserFourDifferentWays";
        String lastName = "testFindUserFourDifferentWays";
        String email = "test@test.test";
        Role input = Role.STUDENT;
        User u = new User(firstName,lastName,email, input,false);
        users.save(u);
        int fourSearches = 4;
        int count = 0;


        //act
        User found = users.findByUserId(u.getUserId()); // first find
        if (found.getUserId() == u.getUserId()){
            count++;
        }

        User found2 = users.findByEmail(u.getEmail()); // second find
        if (found2.getEmail().equalsIgnoreCase(u.getEmail())){
            count++;
        }

        int countName = 0; // third search
        for(User foundUserName : users.findAll()){
            if(foundUserName.getFirstname().equalsIgnoreCase(firstName) || foundUserName.getLastname().equalsIgnoreCase(lastName)){
                countName++;
            }
        }
        List<User> listName = users.findByFirstnameContainingOrLastnameContaining(firstName,lastName);
        int totalCountName = listName.size();
        if (totalCountName == countName){
            count++;
        }

        int countRole = 0; // fourth search
        for(User foundUserRole : users.findAll()){
            if(foundUserRole.getRole() == input){
                countRole++;
            }
        }
        List<User> listRole = users.findAllByRole(input);
        int totalCountRole = listRole.size();

        if (totalCountRole == countRole){
            count++;
        }

        //assert
        assertEquals(fourSearches, count);

        //undo operations
        users.delete(u);
    }

    @Test
    public void createEditFindTag(){

        //arrange
        Tag t = new Tag("test","test");
        String input = "success";

        //act
        tags.save(t); // create tag
        t.setTagName(input); // edit tag
        tags.save(t);
        Tag found = tags.findBytagId(t.getTagId()); // find tag

        //assert
        assertEquals(input, found.getTagName());

        //undo operations
        tags.delete(t);
    }

    @Test
    public void deleteUserExternalUserStudentTagAssignmentArchivedAssignment(){

        //arrange
        User u = new User("test","test","test@test.test", Role.COORDINATOR,false);
        char[] password = new char[10];
        byte[] salt = new byte[10];
        ExternalUser e = new ExternalUser("test", "test", "test","00000000000","test","test","test", password, salt);
        Student s = new Student("0000000000","000000",u);
        Tag t = new Tag("test","test");
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        ArchivedAssignment ar = new ArchivedAssignment("test","ZAP","test",5,5,6,"2019-05-05","2019-05-06","testuser","test_tags");

        users.save(u);
        externalUsers.save(e);
        students.save(s);
        tags.save(t);
        assignments.save(a);
        archive.save(ar);

        int count;
        int countFound = (int) (users.count() + externalUsers.count() + tags.count() + students.count() + assignments.count()+ archive.count());
        int amountColumns = 6;

        //act

        archive.delete(ar); // delete archived assignment
        assignments.delete(a); // delete assignment
        tags.delete(t); // delete tag
        students.delete(s); // delete student
        externalUsers.delete(e); // delete external user
        users.delete(u); // delete user

        count = (int) (users.count() + externalUsers.count() + tags.count() + students.count() + assignments.count() + archive.count());
        countFound = countFound - amountColumns;

        //assert
        assertEquals(countFound, count);
    }

}
