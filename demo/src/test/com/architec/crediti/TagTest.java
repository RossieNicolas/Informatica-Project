package com.architec.crediti;

import com.architec.crediti.models.Tag;
import com.architec.crediti.repositories.TagRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagTest {
    @Autowired
    private TagRepo tags;

    @Test
    //id = 17
    public void addTagShouldReturnIncreaseInDB(){

        //arrange
        int tagLength = (int) tags.count();
        Tag t = new Tag("test", "test");

        //act
        tags.save(t);
        int tagLenth2 = (int) tags.count();
        tagLength++;

        //assert
        assertEquals(tagLength,tagLenth2);

        //undo operation
        tags.delete(t);
    }

    @Test
    //id= 71
    public void findByIdTagShouldReturnCorrectTag() {

        //arrange
        Tag t = new Tag("test", "test");
        tags.save(t);
        int tagIdAdded = t.getTagId();

        //act
        Tag found = tags.findBytagId(tagIdAdded);

        //assert
        assertEquals(tagIdAdded, found.getTagId());

        //undo operation
        tags.delete(t);
    }
    @Test
    //id= 72
    public void findByTagNameExistsShouldReturnCorrectTag(){

        //arrange
        Tag t = new Tag("test", "test");
        tags.save(t);

        //act
        boolean found = tags.existsByTagName(t.getTagName());

        //assert
        assertTrue(found);

        //undo operations
        tags.delete(t);
    }

    @Test
    //id = 22
    public void RemoveTagShouldRemoveTagFromDB() {

        //arrange
        Tag t = new Tag("test", "test");
        tags.save(t);
        int tagLength = (int) tags.count();

        //act
        tags.delete(t);
        int tagLength2 = (int) tags.count();
        tagLength--;

        //assert
        assertEquals(tagLength, tagLength2);
    }

    @Test
    //id = 23
    public void editTagNameShouldReturnEditedTag() {

        //arrange
        Tag t = new Tag("test", "test");
        tags.save(t);
        String input = "testEdit";

        //act
        t.setTagName(input);

        //assert
        assertEquals(input, t.getTagName());

        //undo operation
        tags.delete(t);
    }

    @Test
    //id = 23
    public void editTagDescriptionShouldReturnEditedTag() {

        //arrange
        Tag t = new Tag("test", "test");
        tags.save(t);
        String input = "testEdit";

        //act
        t.setTagDescription(input);

        //assert
        assertEquals(input, t.getTagDescription());

        //undo operation
        tags.delete(t);
    }
}
