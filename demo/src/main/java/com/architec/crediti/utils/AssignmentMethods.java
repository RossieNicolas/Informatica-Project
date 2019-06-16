package com.architec.crediti.utils;

import java.util.ArrayList;
import java.util.List;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Tag;

public class AssignmentMethods {

    public static List<Assignment> removeFullAssignments(List<Assignment> assignments) {
        assignments.removeIf(item -> item.getAmountStudents() == item.getMaxStudents());
        return assignments;
    }


    public static boolean[] getStatusFalse(List<Tag> allTags) {
        boolean[] status = new boolean[allTags.size()];
        for (int i = 0; i < allTags.size(); i++) {
            status[i] = false;

        }
        return status;
    }

    public static boolean[] getStatus(ArrayList<Tag> tag, List<Tag> allTags) {
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

    public static int[] getTagIds(String tags) {
        String[] tags2 = tags.split("&");
        int[] arrayOftagIds = new int[tags2.length - 1];
        for (int i = 0; i < arrayOftagIds.length; i++) {
            arrayOftagIds[i] = (Integer.parseInt(tags2[i + 1]));
        }
        return arrayOftagIds;
    }
}
