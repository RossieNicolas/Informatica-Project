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
    private Log log = LogFactory.getLog(this.getClass());

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

    @GetMapping(value = "/allassignments/{searchbar}")
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
            if (a.getAmountStudents() != a.getMaxStudents()) {
                model.addAttribute("assignments", a);
                List<Assignment> listsd = new ArrayList();
                listsd.add(a);
                fiches = new PageImpl<>(listsd);
            }

        } catch (Exception e) {

            fiches = assignmentRepo.findByTitleContainingAndFullOrderByAssignmentIdDesc(searchbar, false,
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
    @PathVariable(required = false, value = "tag") String tags, Model model){
        String[] tags2 = tags.split("&");
        int[] arrayOftagIds = new int[tags2.length -1];
        for(int i =0 ; i <arrayOftagIds.length; i++){
            arrayOftagIds[i] = (Integer.parseInt(tags2[i+1]));
        }
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
            List<Assignment> list3 = (List<Assignment>) assignmentRepo
                    .findByTitleContainingAndFullOrderByAssignmentIdDesc("", false);
            List<Long> list = new ArrayList<>();
            for (int item : arrayOftagIds) {
                for (Assignment a : list3) {
                    if (a.getTags().contains(tagRepo.findBytagId(item))) {
                        list.add(a.getAssignmentId());
                    }
                }
            }
            list = list.stream().distinct().collect(Collectors.toList());
            if (!list.isEmpty()) {
                fiches = assignmentRepo.findByTagsOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE));
            } else {
                List<Assignment> list54 = new ArrayList();
                fiches = new PageImpl<>(list54);
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
        return "assignments/listAllAssignments";
    }


   // search assignments
    @GetMapping(value = "/allassignments/{searchbar}/{tag}")
    public String getAssignment(@PathVariable(value = "searchbar" , required = false) String searchbar, Model model,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @PathVariable(required = false, value = "tag") String tags) {
        String[] tags2 = tags.split("&");
        int[] arrayOftagIds = new int[tags2.length -1];
        for(int i =0 ; i <arrayOftagIds.length; i++){
            arrayOftagIds[i] = (Integer.parseInt(tags2[i+1]));
        }
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
            List<Assignment> list3 = (List<Assignment>) assignmentRepo
                    .findByTitleContainingAndFullOrderByAssignmentIdDesc(searchbar, false);
            List<Long> list = new ArrayList<>();
            for (int item : arrayOftagIds) {
                for (Assignment a : list3) {
                    if (a.getTags().contains(tagRepo.findBytagId(item))) {
                        list.add(a.getAssignmentId());
                    }
                }
            }
            list = list.stream().distinct().collect(Collectors.toList());
            if (!list.isEmpty()) {
                fiches = assignmentRepo.findByTagsOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE));
            } else {
                List<Assignment> list54 = new ArrayList();
                fiches = new PageImpl<>(list54);
            }

            ArrayList<Tag> tag = new ArrayList<>();

            for (int var : arrayOftagIds) {
                tag.add(tagRepo.findBytagId(var));
            }

            boolean[] status = AssignmentMethods.getStatus(tag , tagRepo.findAll());

        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        return "assignments/listAllAssignments";
    }


    @GetMapping(value = "/allFullAssignments/{searchbar}")
    public String searchFullByTitleorId(@PathVariable(value = "searchbar", required = false) String searchbar, Model model,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page, Principal principal) {
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        try {
            Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(searchbar)));
            if (a.getAmountStudents() != a.getMaxStudents()) {
                model.addAttribute("assignments", a);
                List<Assignment> listsd = new ArrayList();
                listsd.add(a);
                fiches = new PageImpl<>(listsd);
            }

        } catch (Exception e) {

            fiches = assignmentRepo.findByTitleContainingAndFullOrderByAssignmentIdDesc(searchbar, true,
                    PageRequest.of(evalPage, PAGE_SIZE));
            model.addAttribute("assignments", fiches);
        }
        List<Tag> allTags = tagRepo.findAll();

        boolean[] status = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            status[i] = false;

        }
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

    @GetMapping(value = "/allFullAssignments/tag/{tag}")
    public String searchFullByTags(@RequestParam(value = "page", required = false) Optional<Integer> page,
                               @PathVariable(required = false, value = "tag") String tags, Model model, Principal principal) {
        String[] tags2 = tags.split("&");
        int[] test = new int[tags2.length - 1];
        for (int i = 0; i < test.length; i++) {
            test[i] = (Integer.parseInt(tags2[i + 1]));
        }
        for (int item : test) {
            log.info(item);
        }
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        List<Assignment> list3 = assignmentRepo
                .findByTitleContainingAndFullOrderByAssignmentIdDesc("", true);
        List<Long> list = new ArrayList<>();
        for (int item : test) {
            for (Assignment a : list3) {
                if (a.getTags().contains(tagRepo.findBytagId(item))) {
                    list.add(a.getAssignmentId());
                }
            }
        }
        list = list.stream().distinct().collect(Collectors.toList());
        if (list.isEmpty()) {
            fiches = assignmentRepo.findByTagsAndFullOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE));
        } else {
            List<Assignment> list54 = new ArrayList();
            fiches = new PageImpl<>(list54);
        }

        ArrayList<Tag> tag = new ArrayList<>();
        List<Tag> allTags = tagRepo.findAll();

        for (int var : test) {
            tag.add(tagRepo.findBytagId(var));
        }

        boolean[] status = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            for (Tag item : tag) {
                if (allTags.get(i).getTagId() == item.getTagId()) {
                    status[i] = true;
                }
            }
        }

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
    public String getFullAssignmentByTagAndText(@PathVariable(value = "searchbar", required = false) String searchbar, Model model,
                                @RequestParam(value = "page", required = false) Optional<Integer> page,
                                @PathVariable(required = false, value = "tag") String tags, Principal principal) {
        String[] tags2 = tags.split("&");
        int[] test = new int[tags2.length - 1];
        for (int i = 0; i < test.length; i++) {
            test[i] = (Integer.parseInt(tags2[i + 1]));
        }

        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        List<Assignment> list3 = assignmentRepo
                .findByTitleContainingAndFullOrderByAssignmentIdDesc(searchbar, true);
        List<Long> list = new ArrayList<>();
        for (int item : test) {
            for (Assignment a : list3) {
                if (a.getTags().contains(tagRepo.findBytagId(item))) {
                    list.add(a.getAssignmentId());
                }
            }
        }
        list = list.stream().distinct().collect(Collectors.toList());
        if (list.isEmpty()) {
            fiches = assignmentRepo.findByTagsAndFullOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE));
        } else {
            List<Assignment> list54 = new ArrayList();
            fiches = new PageImpl<>(list54);
        }

        ArrayList<Tag> tag = new ArrayList<>();
        List<Tag> allTags = tagRepo.findAll();

        for (int var : test) {
            tag.add(tagRepo.findBytagId(var));
        }

        boolean[] status = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            for (Tag item : tag) {
                if (allTags.get(i).getTagId() == item.getTagId()) {
                    status[i] = true;
                }
            }
        }

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
