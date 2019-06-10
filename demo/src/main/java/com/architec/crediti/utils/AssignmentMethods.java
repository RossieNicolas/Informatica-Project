package com.architec.crediti.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Tag;
import com.architec.crediti.repositories.TagRepo;

public class AssignmentMethods {

    private AssignmentMethods(TagRepo tagRepo){
    }

    public static List<Assignment> removeFullAssignments (List<Assignment> assignments ) {
        assignments.removeIf(item -> item.getAmountStudents() == item.getMaxStudents());
        return assignments;
    }

    public static Page<Assignment> removeFullAssignmentsPage (Page<Assignment> assignments ) {
        Iterator<Assignment> list = assignments.iterator();
        while (list.hasNext()) {
            Assignment item = list.next();
            if (item.getAmountStudents() == item.getMaxStudents()) {
                list.remove();
            }
        }
        return assignments;
    }

    public static boolean[] getStatusFalse(List<Tag> allTags){
        boolean[] status = new boolean[allTags.size()];
        for (int i = 0; i < allTags.size(); i++) {
            status[i] = false;

        }
        return status;
    }

    public static boolean[] getStatus(ArrayList<Tag> tag  , List<Tag>allTags){
        boolean[] status = new boolean[allTags.size()];
        for (int i = 0; i < allTags.size(); i++) {
            for (Tag item : tag) {
                if (allTags.get(i).getTagId() == item.getTagId()) {
                    status[i] = true;
                }
            }
        }
        return status;
    }
}
