package com.architec.crediti.repositories;

import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Page;
import com.architec.crediti.models.Assignment;

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
}
