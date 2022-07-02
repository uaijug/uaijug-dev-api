package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Tag;
import br.com.uaijug.uaijugdevapi.model.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class TagController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/tags")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Tag> tags = tagService.findAll(pageNumber, ROW_PER_PAGE);

        long count = tagService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("tags", tags);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "tag-list";
    }

    @GetMapping(value = "/tags/{tagId}")
    public String getById(Model model, @PathVariable long tagId) {
        Tag tag = null;
        try {
            tag = tagService.findById(tagId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Tag not found");
        }
        model.addAttribute("tag", tag);
        return "tag";
    }

    @GetMapping(value = {"/tags/add"})
    public String showAdd(Model model) {
        Tag tag = new Tag();
        model.addAttribute("add", true);
        model.addAttribute("tag", tag);

        return "tag-edit";
    }

    @PostMapping(value = "/tags/add")
    public String add(Model model,
                      @ModelAttribute("tag") Tag tag) {
        try {
            Tag newTag = tagService.save(tag);
            return "redirect:/tags/" + String.valueOf(newTag.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("tag", tag);
            model.addAttribute("add", true);
            return "tag-edit";
        }
    }

    @GetMapping(value = {"/tags/{tagId}/edit"})
    public String showEdit(Model model, @PathVariable long tagId) {

        Tag tag = null;
        try {
            tag = tagService.findById(tagId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Tag not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("tag", tag);

        return "tag-edit";
    }

    @PostMapping(value = {"/tags/{tagId}/edit"})
    public String update(Model model,
                         @PathVariable long tagId,
                         @ModelAttribute("tag") Tag tag) {
        try {
            tag.setId(tagId);
            tagService.update(tag);
            return "redirect:/tags/" + String.valueOf(tag.getId());
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

    @GetMapping(value = {"/tags/{tagId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long tagId) {
        Tag tag = null;
        try {
            tag = tagService.findById(tagId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Tag not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("tag", tag);
        return "tag";
    }

    @PostMapping(value = {"/tags/{tagId}/delete"})
    public String deleteById(
            Model model, @PathVariable long tagId) {
        try {
            tagService.deleteById(tagId);
            return "redirect:/tags";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "tag";
        }
    }
}
