package com.architec.crediti;

import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.repositories.ExternalUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExternalUserTest {

    @Autowired
    ExternalUserRepository externalUsers;

    @Test
    public void addExternalUserShouldIncreaseInDB(){

        //arrange
        int count = (int) externalUsers.count();
        ExternalUser e = new ExternalUser("test", "test", "test","00000000000","test","test","test");

        //act
        externalUsers.save(e);
        int count2 = (int) externalUsers.count();
        count++;

        //assert
        assertEquals(count, count2);

        //undo operations
        externalUsers.delete(e);
    }

    @Test
    public void deleteExternalUserShouldDecreaseInDB(){

        //arrange
        int count = (int) externalUsers.count();
        ExternalUser e = new ExternalUser("test", "test", "test","00000000000","test","test","test");
        externalUsers.save(e);

        //act
        externalUsers.delete(e);
        int count2 = (int) externalUsers.count();

        //assert
        assertEquals(count, count2);
    }

    @Test
    public void editExternalUserShouldChangeInDB(){

        //arrange
        ExternalUser e = new ExternalUser("test", "test", "test","00000000000","test","test","test");
        externalUsers.save(e);
        String input = "success";

        //act
        e.setFirstname(input);
        externalUsers.save(e);

        //assert
        assertEquals(input, e.getFirstname());

        //undo operations
        externalUsers.delete(e);
    }

}
