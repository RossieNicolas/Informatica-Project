package com.architec.crediti;
import com.architec.crediti.models.Documentation;
import com.architec.crediti.repositories.DocumentationRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentationTest {

    @Autowired
    DocumentationRepository documents;


    @Test
    //id= 59
    public void addDocumentationShouldIncreaseInDB(){

        //arrange
        Documentation d = new Documentation("test","test.com");
        int count = (int) documents.count();

        //act
        documents.save(d);
        count++;
        int count2 = (int) documents.count();

        //assert
        assertEquals(count,count2);

        //undo operations
        documents.delete(d);
    }

    @Test
    //id= 61
    public void deleteDocumentationShouldDecreaseInDB(){

        //arrange
        Documentation d = new Documentation("test","test.com");
        documents.save(d);
        int count = (int) documents.count();

        //act
        documents.delete(d);
        count--;
        int count2 = (int) documents.count();

        //assert
        assertEquals(count,count2);

    }

    @Test
    //id= 62
    public void findByDocumentTitleShouldReturnCorrectDocument(){

        //arrange
        Documentation d = new Documentation("test","test.com");
        documents.save(d);
        int documentId = d.getId();
        String input = "test";

        //act
        Documentation found = documents.findByTitle(input);

        //assert
        assertEquals(documentId, found.getId());

        //undo operations
        documents.delete(d);
    }
}
