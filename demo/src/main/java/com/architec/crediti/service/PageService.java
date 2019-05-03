package com.architec.crediti.service;

import java.util.Collections;
import java.util.List;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.repositories.AssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageService {
    @Autowired
    AssignmentRepository assingmentRepo;

    final private List<Assignment> assignments = assingmentRepo.findAll();

    public Page<Assignment> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Assignment> list;

        if (assignments.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, assignments.size());
            list = assignments.subList(startItem, toIndex);
        }

        Page<Assignment> bookPage = new PageImpl<Assignment>(list, PageRequest.of(currentPage, pageSize),
                assignments.size());

        return bookPage;

    }
}
