package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Pager;
import com.architec.crediti.models.Tag;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.*;
import com.architec.crediti.utils.AssignmentMethods;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SearchController {
    private static final int PAGE_SIZE = 15;
    private static final int INITAL_PAGE = 0;

    private final
    TagRepo tagRepo;

    private final
    AssignmentRepository assignmentRepo;

    private final
    UserRepository userRepo;

    @Autowired
    public SearchController(TagRepo tagRepo, AssignmentRepository assignmentRepo, UserRepository userRepo) {
        this.tagRepo = tagRepo;
        this.assignmentRepo = assignmentRepo;
        this.userRepo = userRepo;

    }

    @GetMapping("/allassignments/{searchbar}")
    public String searchByTitleorId(@PathVariable(value = "searchbar", required = false) String searchbar, Model model,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page, Principal principal) {
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        try {
            Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(searchbar)));
            List<Assignment> listAssignemnt = new ArrayList();
            if (!a.isFull() && a.isValidated()) {
                model.addAttribute("assignments", a);
                listAssignemnt.add(a);
            }
            fiches = new PageImpl<>(listAssignemnt);

        } catch (Exception e) {

            fiches = assignmentRepo.findByValidatedAndTitleContainingAndFullOrderByAssignmentIdDesc(true, searchbar, false,
                    PageRequest.of(evalPage, PAGE_SIZE));
            model.addAttribute("assignments", fiches);
        }

        boolean[] status = AssignmentMethods.getStatusFalse(tagRepo.findAll());

        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllAssignments";
    }

    //search by tag

    @GetMapping(value = "/allassignments/tag/{tag}")
    public String searchByTags(@RequestParam(value = "page", required = false) Optional<Integer> page,
                               @PathVariable(required = false, value = "tag") String tags, Model model, Principal principal) {
        int[] arrayOftagIds = AssignmentMethods.getTagIds(tags);

        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        List<Assignment> listOfAllAssignments = assignmentRepo
                .findByValidatedAndTitleContainingAndFullOrderByAssignmentIdDesc(true, "", false);
        List<Long> listoftagIds = new ArrayList<>();
        for (int item : arrayOftagIds) {
            for (Assignment a : listOfAllAssignments) {
                if (a.getTags().contains(tagRepo.findBytagId(item))) {
                    listoftagIds.add(a.getAssignmentId());
                }
            }
        }
        listoftagIds = listoftagIds.stream().distinct().collect(Collectors.toList());
        if (!listoftagIds.isEmpty()) {
            fiches = assignmentRepo.findByValidatedAndTagsOrderByAssignmentIdDesc(listoftagIds,
                    PageRequest.of(evalPage, PAGE_SIZE), true);
        } else {
            // als deze else verwijderd wordt dat gaat er er fout gebeuren als en geen
            // assignments gevonden worden bij een tag
            List<Assignment> emptyList = new ArrayList();
            fiches = new PageImpl<>(emptyList);
        }
        ArrayList<Tag> tag = new ArrayList<>();

        for (int var : arrayOftagIds) {
            tag.add(tagRepo.findBytagId(var));
        }
        boolean[] status = AssignmentMethods.getStatus(tag, tagRepo.findAll());
        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllAssignments";
    }

    // search assignments
    @GetMapping(value = "/allassignments/{searchbar}/{tag}")
    public String getAssignment(@PathVariable(value = "searchbar", required = false) String searchbar, Model model,
                                @RequestParam(value = "page", required = false) Optional<Integer> page,
                                @PathVariable(required = false, value = "tag") String tags, Principal principal) {
        int[] arrayOftagIds = AssignmentMethods.getTagIds(tags);
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        List<Assignment> listOfAllAssignments = assignmentRepo
                .findByValidatedAndTitleContainingAndFullOrderByAssignmentIdDesc(true, searchbar, false);
        List<Long> list = new ArrayList<>();
        for (int item : arrayOftagIds) {
            for (Assignment a : listOfAllAssignments) {
                if (a.getTags().contains(tagRepo.findBytagId(item))) {
                    list.add(a.getAssignmentId());
                }
            }
        }
        list = list.stream().distinct().collect(Collectors.toList());
        if (!list.isEmpty()) {
            fiches = assignmentRepo.findByValidatedAndTagsOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE), true);
        } else {
            // als deze else verwijderd wordt dat gaat er er fout gebeuren als en geen
            // assignments gevonden worden bij een tag
            List<Assignment> emptyList = new ArrayList();
            fiches = new PageImpl<>(emptyList);
        }

        ArrayList<Tag> tag = new ArrayList<>();
        for (int var : arrayOftagIds) {
            tag.add(tagRepo.findBytagId(var));
        }
        boolean[] status = AssignmentMethods.getStatus(tag, tagRepo.findAll());

        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());

        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllAssignments";
    }

    @GetMapping(value = "/allFullAssignments/{searchbar}")
    public String searchFullByTitleorId(@PathVariable(value = "searchbar", required = false) String searchbar,
                                        Model model, @RequestParam(value = "page", required = false) Optional<Integer> page, Principal principal) {
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        try {
            Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(searchbar)));
            List<Assignment> listFullAssignemet = new ArrayList();
            if (a.isFull()) {
                model.addAttribute("assignments", a);
                listFullAssignemet.add(a);
            }
            fiches = new PageImpl<>(listFullAssignemet);

        } catch (Exception e) {

            fiches = assignmentRepo.findByTitleContainingAndFullOrderByAssignmentIdDesc(searchbar, true,
                    PageRequest.of(evalPage, PAGE_SIZE));
            model.addAttribute("assignments", fiches);
        }

        boolean[] status = AssignmentMethods.getStatusFalse(tagRepo.findAll());

        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        // pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllFullAssignments";
    }

    @GetMapping(value = "/allFullAssignments/tag/{tag}")
    public String searchFullByTags(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                   @PathVariable(required = false, value = "tag") String tags, Model model, Principal principal) {
        int[] arrayOftagIds = AssignmentMethods.getTagIds(tags);

        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        List<Assignment> listOfAllAssignments = assignmentRepo
                .findByTitleContainingAndFullOrderByAssignmentIdDesc("", true);
        List<Long> listoftagIds = new ArrayList<>();
        for (int item : arrayOftagIds) {
            for (Assignment a : listOfAllAssignments) {
                if (a.getTags().contains(tagRepo.findBytagId(item))) {
                    listoftagIds.add(a.getAssignmentId());
                }
            }
        }
        listoftagIds = listoftagIds.stream().distinct().collect(Collectors.toList());
        if (!listoftagIds.isEmpty()) {
            fiches = assignmentRepo.findByTagsOrderByAssignmentIdDesc(listoftagIds,
                    PageRequest.of(evalPage, PAGE_SIZE));
        } else {
            // als deze else verwijderd wordt dat gaat er er fout gebeuren als en geen
            // assignments gevonden worden bij een tag
            List<Assignment> emptyList = new ArrayList();
            fiches = new PageImpl<>(emptyList);
        }

        ArrayList<Tag> tag = new ArrayList<>();

        for (int var : arrayOftagIds) {
            tag.add(tagRepo.findBytagId(var));
        }
        boolean[] status = AssignmentMethods.getStatus(tag, tagRepo.findAll());
        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllFullAssignments";
    }

    // search assignments
    @GetMapping(value = "/allFullAssignments/{searchbar}/{tag}")
    public String getFullAssignmentByTagAndText(@PathVariable(value = "searchbar", required = false) String searchbar,
                                                Model model, @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                @PathVariable(required = false, value = "tag") String tags, Principal principal) {
        int[] arrayOftagIds = AssignmentMethods.getTagIds(tags);
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        List<Assignment> listOfAllAssignments = assignmentRepo
                .findByTitleContainingAndFullOrderByAssignmentIdDesc(searchbar, true);
        List<Long> list = new ArrayList<>();
        for (int item : arrayOftagIds) {
            for (Assignment a : listOfAllAssignments) {
                if (a.getTags().contains(tagRepo.findBytagId(item))) {
                    list.add(a.getAssignmentId());
                }
            }
        }
        list = list.stream().distinct().collect(Collectors.toList());
        if (!list.isEmpty()) {
            fiches = assignmentRepo.findByTagsOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE));
        } else {
            // als deze else verwijderd wordt dat gaat er er fout gebeuren als en geen
            // assignments gevonden worden bij een tag
            List<Assignment> emptyList = new ArrayList();
            fiches = new PageImpl<>(emptyList);
        }

        ArrayList<Tag> tag = new ArrayList<>();
        for (int var : arrayOftagIds) {
            tag.add(tagRepo.findBytagId(var));
        }
        boolean[] status = AssignmentMethods.getStatus(tag, tagRepo.findAll());

        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllFullAssignments";
    }
}
