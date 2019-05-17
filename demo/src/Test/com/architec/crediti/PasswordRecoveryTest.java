package com.architec.crediti;


import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
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
    private UserRepository userRepo;
    @Test
    public void testFindByEmail() {
        List<User> list = userRepo.findAll();
        User u = userRepo.findByEmail("s100735@ap.be");
        list.contains(u);
        // email user bestaat is test
        assertTrue(u.getEmail(),true);
    }

    @Test
    public void testNonExistingEmail() {
        List<User> list = userRepo.findAll();
        User u = userRepo.findByEmail("test@test.be");
        list.contains(u);
        // email user bestaat niet is test
        assertNotEquals("test@test.be",u);

    }

}
