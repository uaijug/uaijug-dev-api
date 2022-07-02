package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import br.com.uaijug.uaijugdevapi.model.domain.Tag;
import br.com.uaijug.uaijugdevapi.model.service.EventService;
import br.com.uaijug.uaijugdevapi.model.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Controller
public class EventController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private EventService eventService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/events")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Event> events = eventService.findAll(pageNumber, ROW_PER_PAGE);

        long count = eventService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("events", events);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "event-list";
    }

    @GetMapping(value = "/events/{eventId}")
    public String getById(Model model, @PathVariable long eventId) {
        Event event = null;
        try {
            event = eventService.findById(eventId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Event not found");
        }
        model.addAttribute("event", event);
        return "event";
    }

    @GetMapping(value = {"/events/add"})
    public String showAdd(Model model) {
        Event event = new Event();
        model.addAttribute("add", true);
        model.addAttribute("event", event);

        List<Tag> tagList = tagService.list();
        if (!tagList.isEmpty()) {
            event.setTags(Set.copyOf(tagList));
            model.addAttribute("tagList", tagList);
        }

        return "event-edit";
    }

    @PostMapping(value = "/events/add")
    public String add(Model model,
                      @ModelAttribute("event") Event event) {
        try {
            Event newEvent = eventService.save(event);
            return "redirect:/events/" + String.valueOf(newEvent.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("event", event);
            model.addAttribute("add", true);
            return "event-edit";
        }
    }

    @GetMapping(value = {"/events/{eventId}/edit"})
    public String showEdit(Model model, @PathVariable long eventId) {

        Event event = null;
        try {
            event = eventService.findById(eventId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Event not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("event", event);

        List<Tag> tagList = tagService.list();
        if (!tagList.isEmpty()) {
            event.setTags(Set.copyOf(tagList));
            model.addAttribute("tagList", tagList);
        }
        return "event-edit";
    }

    @PostMapping(value = {"/events/{eventId}/edit"})
    public String update(Model model,
                         @PathVariable long eventId,
                         @ModelAttribute("event") Event event) {
        try {
            event.setId(eventId);
            eventService.update(event);
            return "redirect:/events/" + String.valueOf(event.getId());
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

    @GetMapping(value = {"/events/{eventId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long eventId) {
        Event event = null;
        try {
            event = eventService.findById(eventId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Event not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("event", event);
        return "event";
    }

    @PostMapping(value = {"/events/{eventId}/delete"})
    public String deleteById(
            Model model, @PathVariable long eventId) {
        try {
            eventService.deleteById(eventId);
            return "redirect:/events";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "event";
        }
    }
}
