package com.architec.crediti;
import static org.junit.Assert.assertEquals;


import java.util.List;
import com.architec.crediti.models.ArchivedAssignment;
import com.architec.crediti.repositories.ArchiveRepository;

import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArchiveTest {
    @Autowired
    private ArchiveRepository archive;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StudentRepository stuRepo;

    @Test
    //id= 48
    public void addNewArchivedAssignmentShouldIncreaseInDB() {

        //arange
        List<ArchivedAssignment> list = (List<ArchivedAssignment>) archive.findAll();
        int length = list.size();

        //act
        ArchivedAssignment a = new ArchivedAssignment("test","ZAP","test",5,5,"2019-05-05","2019-05-06","testuser");
        archive.save(a);
        List<ArchivedAssignment> list2 = (List<ArchivedAssignment>) archive.findAll();
        int length2 = list2.size();

        //assert
        assertEquals(length +1, length2);
    }
    @Test
    //id= 51
    public void findByIdArchiveShouldReturnCorrectArchivedAssignment() {

        //arrange
        ArchivedAssignment a = new ArchivedAssignment("test","ZAP","test",5,5,"2019-05-05","2019-05-06","testuser");
        archive.save(a);
        long assignmentIdForA = a.getAssignmentId();

        //act
        ArchivedAssignment copyA = archive.findByAssignmentId(assignmentIdForA);

        //assert
        assertEquals(assignmentIdForA, copyA.getAssignmentId());
    }
    @Test
    //id= 52
    public void findByNameArchiveShouldReturnCorrectArchivedAssignment() {

        //arrange
        int count = 0;

        //act
        for(ArchivedAssignment a : archive.findAll()){
            if(a.getTitle().equalsIgnoreCase("test")){
                count++;
            }
        }
        List<ArchivedAssignment> list = archive.findByTitleContaining("test");
        int totalCount = list.size();

        //assert
        assertEquals(count, totalCount);
    }

}
