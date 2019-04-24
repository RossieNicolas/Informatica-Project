package com.architec.demo.assignment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AssignmentController {
    private List<Assignment> assignments;

    public AssignmentController() {
        assignments = new ArrayList<>();

        assignments.add(new Assignment(1, "opdracht 1", "soort 1", "omschriving opdracht 1",
                5, 2, "01/01/2019", "02/02/2019", "opdrachtgever"));

        assignments.add(new Assignment(2, "opdracht 2", "soort 2", "omschriving opdracht 2",
                5, 2, "01/01/2019", "02/02/2019", "opdrachtgever"));

        assignments.add(new Assignment(3, "opdracht 3", "soort 3", "omschriving opdracht 3",
                5, 2, "01/01/2019", "02/02/2019", "opdrachtgever"));
    }

    @RequestMapping(value = "/assignments", method = RequestMethod.GET)
    public String assignments(Model model) {

        String test = assignments.get(0).getTitel();

        model.addAttribute("titel", test);
        return "assignments";
    }
}
