package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Class;
import br.com.uaijug.uaijugdevapi.model.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class ClassController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private ClassService classService;

    @GetMapping(value = "/classes")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Class> classes = classService.findAll(pageNumber, ROW_PER_PAGE);

        long count = classService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("classes", classes);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "classes-list";
    }

    @GetMapping(value = "/classes/{classId}")
    public String getById(Model model, @PathVariable long classId) {
        Class clazz = null;
        try {
            clazz = classService.findById(classId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Class not found");
        }
        model.addAttribute("class", clazz);
        return "classes";
    }

    @GetMapping(value = {"/classes/add"})
    public String showAdd(Model model) {
        Class clazz = new Class();
        model.addAttribute("add", true);
        model.addAttribute("class", clazz);

        return "classes-edit";
    }

    @PostMapping(value = "/classes/add")
    public String add(Model model,
                      @ModelAttribute("class") Class clazz) {
        try {
            Class newClass = classService.save(clazz);
            return "redirect:/classes/" + String.valueOf(newClass.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("class", class);
            model.addAttribute("add", true);
            return "classes-edit";
        }
    }

    @GetMapping(value = {"/classes/{classId}/edit"})
    public String showEdit(Model model, @PathVariable long classId) {

        Class clazz = null;
        try {
            clazz = classService.findById(classId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Class not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("class", clazz);

        return "classes-edit";
    }

    @PostMapping(value = {"/classes/{classId}/edit"})
    public String update(Model model,
                         @PathVariable long classId,
                         @ModelAttribute("class") Class clazz) {
        try {
            clazz.setId(classId);
            classService.update(clazz);
            return "redirect:/classes/" + String.valueOf(clazz.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "classes-edit";
        }
    }

    @GetMapping(value = {"/classes/{classId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long classId) {
        Class clazz = null;
        try {
            clazz = classService.findById(classId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Class not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("class", clazz);
        return "classes";
    }

    @PostMapping(value = {"/classes/{classId}/delete"})
    public String deleteById(
            Model model, @PathVariable long classId) {
        try {
            classService.deleteById(classId);
            return "redirect:/classes";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "classes";
        }
    }
}
