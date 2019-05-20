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

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordRecoveryTest {

    @Autowired
    private UserRepository users;

    @Test
    //id= 34
    public void FindByEmailShouldReturnCorrectUser() {

        //arrange
        User u = new User("testUser", "testUser","test@test.test", Role.COORDINATOR,false);
        users.save(u);
        boolean exists = false;

        //act
        for(User copyU : users.findAll()){
            if (copyU.getEmail().equalsIgnoreCase(u.getEmail())){
                exists = true;
            }
        }

        //assert
        assertTrue(exists);

        //undo operation
        users.delete(u);
    }

    @Test
    //id= 35
    public void NotExistingEmailShouldReturnFalse() {

        //arrange
        List<User> list = users.findAll();
        User u = users.findByEmail("notAnActualEmail");
        boolean exists = true;

        //act
        if (u == null){
            exists = false;
        }

        //assert
        assertFalse(exists);

    }

}
