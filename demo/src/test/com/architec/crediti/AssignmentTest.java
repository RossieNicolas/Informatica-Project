package com.architec.crediti;
import static org.junit.Assert.assertEquals;

import java.util.List;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;

import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class AssignmentTest {
    @Autowired
    private AssignmentRepository assignments;
    @Autowired
    private UserRepository users;


    @Test
    //id= 2
    public void addNewAssignmentShouldIncreaseInDB() {

        //arrange
        User u = new User("test","test","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        List<Assignment> list = (List<Assignment>) assignments.findAll();
        int length = list.size();
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        assignments.save(a);


        //act
        List<Assignment> list2 = (List<Assignment>) assignments.findAll();
        int length2 = list2.size();
        length++;

        //assert
        assertEquals(length, length2);

        //undo operation
        assignments.delete(a);
        users.delete(u);
    }
    @Test
    //id= 16
    public void findByAssignmentIdShouldReturnCorrectAssignment() {

        //arrange
        User u = new User("test","test","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        assignments.save(a);
        int assignmentId = (int) a.getAssignmentId();

        //act
        Assignment copyA = assignments.findByAssignmentId(assignmentId);

        //assert
        assertEquals(assignmentId, copyA.getAssignmentId());

        //undo operations
        assignments.delete(a);
        users.delete(u);
    }
    @Test
    //id= 15
    public void findByNameAssignmentShouldReturnCorrectAssignment() {

        //arrange
        int count = 0;
        String input = "findByNameAssignmentShouldReturnCorrectAssignment";

        //act
        // no mock assignment needs to be made because multiple assignments can have the same name
        for(Assignment a : assignments.findAll()){
            if(a.getTitle().contains(input)){
                count++;
            }
        }
        List<Assignment> list = assignments.findByTitleContainingAndArchived(input,true);
        int totalCount = list.size();

        //assert
        assertEquals(count, totalCount);
    }
    @Test
    //id= 8
    public void deleteAssignmentShouldDeleteFromDB(){

        //arrange
        User u = new User("test","test","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        assignments.save(a);
        int length = (int) assignments.count();

        //act
        assignments.delete(a);
        int length2 = (int) assignments.count();
        length--;

        //assert
        assertEquals(length,length2);

        //undo operations
        users.delete(u);
    }
    @Test
    //id= 10
    public void updateAssignmentShouldUpdateInDB(){

        //arrange
        User u = new User("test","test","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        Assignment a = new Assignment("test","test","test",5,6,"2019-05-05","2019-05-06",false,false,u);
        assignments.save(a);
        String input = "success";

        //act
        a.setTitle(input);
        assignments.save(a);

        //assert
        assertEquals(input, a.getTitle());

        //undo operations
        assignments.delete(a);
        users.delete(u);
    }

}
