package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import br.com.uaijug.uaijugdevapi.model.dto.AssociateDrawDTO;
import br.com.uaijug.uaijugdevapi.model.dto.AssociateEventDTO;
import br.com.uaijug.uaijugdevapi.model.service.DrawService;
import br.com.uaijug.uaijugdevapi.model.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Controller
public class DrawController {

    @Autowired
    private DrawService service;

    @Autowired
    private EventService eventService;

    @GetMapping(value = {"/associate-draw/find-associate"})
    public String showAdd(Model model) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        var associateDrawDTO = new AssociateDrawDTO();
        model.addAttribute("add", true);
        model.addAttribute("associateDrawDTO", new AssociateDrawDTO());

        //model.addAttribute("errorMessage", "");
        List<Event> eventList = eventService.list();
        model.addAttribute("eventList", eventList);
        model.addAttribute("peopleDrawn", Collections.EMPTY_LIST);
        return "associate-draw-find-event";
    }

    @PostMapping(value = "/associate-draw/add")
    public String add(Model model,
                      @ModelAttribute("associateDrawDTO") AssociateDrawDTO associateDrawDTO, Errors errors) {

        log.info("Dados Recebidos: " + associateDrawDTO);
        int total = associateDrawDTO.getTotal();

        if (total == 0) {
            model.addAttribute("errorMessage", "O total não deve ser vazio!");
        } else {

            Set<Associate> peopleDrawn = service.prizeDrawing(total);
            model.addAttribute("peopleDrawn", peopleDrawn);
        }
        List<Event> eventList = eventService.list();
        model.addAttribute("eventList", eventList);
        return "associate-draw-find-event";
    }

    @GetMapping(value = {"/associate-draw"})
    public String showAdd(Model model, @PathVariable int associateId) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        var associateEventDTO = new AssociateEventDTO();
        model.addAttribute("add", true);
        model.addAttribute("associateEventDTO", associateEventDTO);

        //model.addAttribute("errorMessage", "");

        //model.addAttribute("eventList", eventList);
        return "associate-draw-event";
    }

}
