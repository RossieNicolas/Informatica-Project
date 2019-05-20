package com.architec.crediti;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTest {
    @Autowired
    StudentRepository students;
    @Autowired
    UserRepository users;

    @Test
    //TODO write in testplan
    public void addStudentShouldIncreaseInDB(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.STUDENT,false);
        users.save(u);
        int count = (int) students.count();
        Student s = new Student("0000000000","000000",u);

        //act
        students.save(s);
        int count2 = (int) students.count();
        count++;

        //assert
        assertEquals(count, count2);

        //undo operations
        students.delete(s);
        users.delete(u);
    }

    @Test
    //TODO write in testplan
    public void deleteStudentShouldDecreaseInDB(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        int count = (int) students.count();
        Student s = new Student("0000000000","000000",u);
        students.save(s);

        //act
        students.delete(s);
        int count2 = (int) students.count();

        //assert
        assertEquals(count, count2);

        //undo operations
        users.delete(u);
    }

    @Test
    //TODO write in testplan
    public void editStudentShouldChangeInDB(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Student s = new Student("0000000000","000000",u);
        students.save(s);
        String input = "success";

        //act
        s.setStudentennummer(input);
        students.save(s);

        //assert
        assertEquals(input, s.getStudentennummer());

        //undo operations
        students.delete(s);
        users.delete(u);
    }

    @Test
    //TODO write in testplan
    public void findByUserIdShouldReturnCorrectStudent(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Student s = new Student("0000000000","000000",u);
        students.save(s);

        //act
        Student found = students.findByUserId(u);

        //arrange
        assertEquals(s.getUserId(),found.getUserId());

        //undo operations
        students.delete(s);
        users.delete(u);
    }

    @Test
    //TODO write in testplan
    public void findByStudentenNummerShouldReturnCorrectStudents(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        String studentenNummer = "000000";
        Student s = new Student("0000000000",studentenNummer,u);
        students.save(s);

        //act
        Student found = students.findByStudentennummer(studentenNummer);

        //assert
        assertEquals(found.getStudentennummer(), s.getStudentennummer());

        //undo operations
        students.delete(s);
        users.delete(u);
    }

    @Test
    //TODO write in testplan
    public void existByStudentNummerShouldReturnExistingStudent(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        String studentenNummer = "000000";
        Student s = new Student("0000000000",studentenNummer,u);
        students.save(s);

        //act
        boolean found = students.existsByStudentennummer(studentenNummer);

        //assert
        assertTrue(found);

        //undo operations
        students.delete(s);
        users.delete(u);
    }

    @Test
    //id= 37
    public void enrollStudentShouldReturnSuccesfullEnroll(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Student s = new Student("0000000000","000000",u);
        students.save(s);
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        Set<Assignment> assignments = new HashSet<>();
        assignments.add(a);
        int count = 0;
        int amount = 1;

        //act
        s.setAssignments(assignments);
        for (Assignment assignment : s.getAssignments()){
            count++;
        }

        //assert
        assertEquals(amount, count);

        //undo operations
        students.delete(s);
        users.delete(u);
    }
}
