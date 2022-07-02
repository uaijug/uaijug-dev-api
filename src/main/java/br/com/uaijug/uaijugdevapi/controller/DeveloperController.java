package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Developer;
import br.com.uaijug.uaijugdevapi.model.service.DeveloperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class DeveloperController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private DeveloperService developerService;

    @GetMapping(value = "/developers")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Developer> developers = developerService.findAll(pageNumber, ROW_PER_PAGE);

        long count = developerService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("developers", developers);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "developer-list";
    }

    @GetMapping(value = "/developers/{developerId}")
    public String getById(Model model, @PathVariable long developerId) {
        Developer developer = null;
        try {
            developer = developerService.findById(developerId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Developer not found");
        }
        model.addAttribute("developer", developer);
        return "developer";
    }

    @GetMapping(value = {"/developers/add"})
    public String showAdd(Model model) {
        Developer developer = new Developer();
        model.addAttribute("add", true);
        model.addAttribute("developer", developer);

        return "developer-edit";
    }

    @PostMapping(value = "/developers/add")
    public String add(Model model,
                      @ModelAttribute("developer") Developer developer) {
        try {
            Developer newDeveloper = developerService.save(developer);
            return "redirect:/developers/" + String.valueOf(newDeveloper.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("developer", developer);
            model.addAttribute("add", true);
            return "developer-edit";
        }
    }

    @GetMapping(value = {"/developers/{developerId}/edit"})
    public String showEdit(Model model, @PathVariable long developerId) {

        Developer developer = null;
        try {
            developer = developerService.findById(developerId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Developer not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("developer", developer);

        return "developer-edit";
    }

    @PostMapping(value = {"/developers/{developerId}/edit"})
    public String update(Model model,
                         @PathVariable long developerId,
                         @ModelAttribute("developer") Developer developer) {
        try {
            developer.setId(developerId);
            developerService.update(developer);
            return "redirect:/developers/" + String.valueOf(developer.getId());
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

    @GetMapping(value = {"/developers/{developerId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long developerId) {
        Developer developer = null;
        try {
            developer = developerService.findById(developerId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Developer not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("developer", developer);
        return "developer";
    }

    @PostMapping(value = {"/developers/{developerId}/delete"})
    public String deleteById(
            Model model, @PathVariable long developerId) {
        try {
            developerService.deleteById(developerId);
            return "redirect:/developers";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "developer";
        }
    }
}
