package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Class;
import br.com.uaijug.uaijugdevapi.model.domain.Schedule;
import br.com.uaijug.uaijugdevapi.model.service.ClassService;
import br.com.uaijug.uaijugdevapi.model.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class ScheduleController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassService classService;

    @GetMapping(value = "/schedules")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Schedule> schedules = scheduleService.findAll(pageNumber, ROW_PER_PAGE);

        long count = scheduleService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("schedules", schedules);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        List<Class> classList = classService.list();
        model.addAttribute("classList", classList);
        return "schedule-list";
    }

    @GetMapping(value = "/schedules/{scheduleId}")
    public String getById(Model model, @PathVariable long scheduleId) {
        Schedule schedule = null;
        try {
            schedule = scheduleService.findById(scheduleId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Schedule not found");
        }
        model.addAttribute("schedule", schedule);
        return "schedule";
    }

    @GetMapping(value = {"/schedules/add"})
    public String showAdd(Model model) {
        Schedule schedule = new Schedule();
        model.addAttribute("add", true);
        model.addAttribute("schedule", schedule);

        List<Class> classList = classService.list();
        model.addAttribute("classList", classList);
        return "schedule-edit";
    }

    @PostMapping(value = "/schedules/add")
    public String add(Model model,
                      @ModelAttribute("schedule") Schedule schedule) {
        try {
            Schedule newSchedule = scheduleService.save(schedule);
            return "redirect:/schedules/" + String.valueOf(newSchedule.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("schedule", schedule);
            model.addAttribute("add", true);
            return "schedule-edit";
        }
    }

    @GetMapping(value = {"/schedules/{scheduleId}/edit"})
    public String showEdit(Model model, @PathVariable long scheduleId) {

        Schedule schedule = null;
        try {
            schedule = scheduleService.findById(scheduleId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Schedule not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("schedule", schedule);

        return "schedule-edit";
    }

    @PostMapping(value = {"/schedules/{scheduleId}/edit"})
    public String update(Model model,
                         @PathVariable long scheduleId,
                         @ModelAttribute("schedule") Schedule schedule) {
        try {
            schedule.setId(scheduleId);
            scheduleService.update(schedule);
            return "redirect:/schedules/" + String.valueOf(schedule.getId());
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

    @GetMapping(value = {"/schedules/{scheduleId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long scheduleId) {
        Schedule schedule = null;
        try {
            schedule = scheduleService.findById(scheduleId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Schedule not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("schedule", schedule);
        return "schedule";
    }

    @PostMapping(value = {"/schedules/{scheduleId}/delete"})
    public String deleteById(
            Model model, @PathVariable long scheduleId) {
        try {
            scheduleService.deleteById(scheduleId);
            return "redirect:/schedules";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "schedule";
        }
    }
}
