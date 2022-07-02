package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Class;
import br.com.uaijug.uaijugdevapi.model.domain.Course;
import br.com.uaijug.uaijugdevapi.model.service.ClassService;
import br.com.uaijug.uaijugdevapi.model.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class CourseController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClassService classService;

    @GetMapping(value = "/courses")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Course> courses = courseService.findAll(pageNumber, ROW_PER_PAGE);

        long count = courseService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("courses", courses);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "course-list";
    }

    @GetMapping(value = "/courses/{courseId}")
    public String getById(Model model, @PathVariable long courseId) {
        Course course = null;
        try {
            course = courseService.findById(courseId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Course not found");
        }
        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping(value = {"/courses/add"})
    public String showAdd(Model model) {
        Course course = new Course();
        model.addAttribute("add", true);
        model.addAttribute("course", course);

        model.addAttribute("classes", getClasses());
        return "course-edit";
    }

    @PostMapping(value = "/courses/add")
    public String add(Model model,
                      @ModelAttribute("course") Course course) {
        try {
            Course newCourse = courseService.save(course);
            return "redirect:/courses/" + String.valueOf(newCourse.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("course", course);
            model.addAttribute("add", true);
            return "course-edit";
        }
    }

    @GetMapping(value = {"/courses/{courseId}/edit"})
    public String showEdit(Model model, @PathVariable long courseId) {

        Course course = null;
        try {
            course = courseService.findById(courseId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Course not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("course", course);

        model.addAttribute("classes", getClasses());
        return "course-edit";
    }

    @PostMapping(value = {"/courses/{courseId}/edit"})
    public String update(Model model,
                         @PathVariable long courseId,
                         @ModelAttribute("course") Course course) {
        try {
            course.setId(courseId);
            courseService.update(course);
            return "redirect:/courses/" + String.valueOf(course.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "contact-edit";
        }
    }

    @GetMapping(value = {"/courses/{courseId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long courseId) {
        Course course = null;
        try {
            course = courseService.findById(courseId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Course not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("course", course);
        return "course";
    }

    @PostMapping(value = {"/courses/{courseId}/delete"})
    public String deleteById(
            Model model, @PathVariable long courseId) {
        try {
            courseService.deleteById(courseId);
            return "redirect:/courses";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "course";
        }
    }

    private List<Class> getClasses() {
        return classService.list();
    }
}
