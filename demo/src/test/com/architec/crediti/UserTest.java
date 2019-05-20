package com.architec.crediti;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    @Autowired
    UserRepository users;

    @Test
    public void addUserShouldIncreaseInDB() {

        //arrange
        int count = (int) users.count();
        User u = new User("testUser", "testUser", "test@test.test", Role.COORDINATOR, false);

        //act
        users.save(u);
        int count2 = (int) users.count();
        count++;

        //assert
        assertEquals(count, count2);

        //undo operations
        users.delete(u);
    }

    @Test
    public void deleteUserShouldDecreaseInDB() {

        //arrange
        int count = (int) users.count();
        User u = new User("testUser", "testUser", "test@test.test", Role.COORDINATOR, false);
        users.save(u);

        //act
        users.delete(u);
        int count2 = (int) users.count();

        //assert
        assertEquals(count, count2);
    }

    @Test
    public void editUserShouldChangeInDB() {

        //arrange
        User u = new User("testUser", "testUser", "test@test.test", Role.COORDINATOR, false);
        users.save(u);
        String input = "success";

        //act
        u.setFirstname(input);
        users.save(u);

        //assert
        assertEquals(input, u.getFirstname());

        //undo operations
        users.delete(u);
    }

    @Test
    public void findByUserIdShouldReturnCorrectUser() {

        //arrange
        User u = new User("testUser", "testUser", "test@test.test", Role.COORDINATOR, false);
        users.save(u);
        long userId = u.getUserId();

        //act
        User found = users.findByUserId(userId);

        //arrange
        assertEquals(u.getUserId(), found.getUserId());

        //undo operations
        users.delete(u);
    }

    @Test
    public void existByUserEmailShouldReturnExistingUser() {

        //arrange
        String email = "test@test.test";
        User u = new User("testUser", "testUser", email, Role.COORDINATOR, false);
        users.save(u);

        //act
        boolean found = users.existsByEmail(email);

        //assert
        assertTrue(found);

        //undo operations
        users.delete(u);
    }

    @Test
    public void findByNameUserShouldReturnCorrectUsers() {

        //arrange
        int count = 0;
        String input = "testUser";

        //act
        // no mock user needs to be made because multiple users can have the same name
        for(User u : users.findAll()){
            if(u.getFirstname().equalsIgnoreCase(input) || u.getLastname().equalsIgnoreCase(input)){
                count++;
            }
        }
        List<User> list = users.findByFirstnameContainingOrLastnameContaining(input,input);
        int totalCount = list.size();

        //assert
        assertEquals(count, totalCount);
    }

    @Test
    public void findByRoleUserShouldReturnCorrectUsers() {

        //arrange
        int count = 0;
        Role input = Role.COORDINATOR;

        //act
        // no mock user needs to be made because multiple users can have the same name
        for(User u : users.findAll()){
            if(u.getRole() == input){
                count++;
            }
        }
        List<User> list = users.findAllByRole(input);
        int totalCount = list.size();

        //assert
        assertEquals(count, totalCount);
    }
}
