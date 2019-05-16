package com.architec.crediti.repositories;

import java.util.Iterator;
import java.util.List;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.User;

public class AssignmentMethods {

    private AssignmentMethods(){

    }

    public static List<Assignment> removeFullAssignments (List<Assignment> assignments ) {
        Iterator<Assignment> list = assignments.iterator();
        while (list.hasNext()) {
            Assignment item = list.next();
            if (item.getAmountStudents() == item.getMaxStudents()) {
                list.remove();
            }
        }
        return assignments;
    }
}
