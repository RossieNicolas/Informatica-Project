package com.architec.crediti;
import static org.junit.Assert.assertEquals;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoordinatorTest {

    @Autowired
    UserRepository users;

    @Test
    //id= 87
    public void listCoordinator(){

        //arrange
        List<User> listUsers = users.findAll();
        List<User> listCoordinator = users.findAllByRole(Role.COORDINATOR);
        List<User> listFiltered = new ArrayList<User>();

        //act
        for (User u : listUsers ) {
            if(u.getRole()==Role.COORDINATOR){
                listFiltered.add(u);
            }
        }

        //assert
        assertEquals(listCoordinator.size(), listFiltered.size());
    }

    @Test
    //id= 87
    public void addCoordinator(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        List<User> usrs = users.findAllByRole(Role.COORDINATOR);
        int count = usrs.size();

        //act
        users.save(u);
        usrs = users.findAllByRole(Role.COORDINATOR);
        int count2 = usrs.size();

        //assert
        assertEquals(count + 1, count2);

        //undo operations
        users.delete(u);
    }

    @Test
    //id= 88
    public void deleteCoordinator(){

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        List<User> usrs = users.findAllByRole(Role.COORDINATOR);
        int count = usrs.size();

        //act
        users.delete(u);
        usrs = users.findAllByRole(Role.COORDINATOR);
        int count2 = usrs.size();

        //assert
        assertEquals(count - 1, count2);
    }
}
