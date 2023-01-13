package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import br.com.uaijug.uaijugdevapi.model.domain.Tag;
import br.com.uaijug.uaijugdevapi.model.dto.AssociateEventDTO;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import br.com.uaijug.uaijugdevapi.model.service.EventService;
import br.com.uaijug.uaijugdevapi.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
public class AssociateEventController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private AssociateService associateService;

    @Autowired
    private EventService eventService;

    @GetMapping(value = {"/associate-event/find"})
    public String showAdd(Model model) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        var associateEventDTO = new AssociateEventDTO();
        model.addAttribute("add", true);
        model.addAttribute("associateEventDTO", associateEventDTO);


        List<Event> eventList = eventService.list();
        model.addAttribute("events", eventList);
        return "associate-find-event";
    }


    @PostMapping(value = "/associate-event/add")
    public String add(Model model,
                      @ModelAttribute("associateEventDTO") AssociateEventDTO associateEventDTO, Errors errors) {
        try {
            log.info("Dados Recebidos: " + associateEventDTO);

            if (null != errors && errors.getErrorCount() > 0) {
                return "associate-find-event";
            }

            Optional<Associate> associate = associateService.findByNameOrCodeOrEmail(associateEventDTO.getName(), associateEventDTO.getCode(), associateEventDTO.getEmail());
            if (associate.isPresent()) {
                Associate associateResponse = associate.get();
                log.info("Dados Recebidos: " + associateResponse);

                Event byEventId = eventService.findById(associateEventDTO.getEventId());
                //TODO - EventRegistration (Event, Associate)
                model.addAttribute("associate", associateResponse);
                return "associate-fonded-event";
            }


            model.addAttribute("associate", associate);
            return "associate-fonded-event";
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("associate", associate);
            model.addAttribute("add", true);
            return "associate-edit";
        }
    }

    @PostMapping(value = {"/associate-find/{associateId}/edit"})
    public String update(Model model,
                         @PathVariable long associateId,
                         @ModelAttribute("associate") Associate associate) {
        try {
            associate.setId(associateId);
            associateService.update(associate);
            return "redirect:/associate-fonded-view/" + String.valueOf(associate.getId());
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

    @GetMapping(value = "/associate-fonded-view/{associateId}")
    public String getById(Model model, @PathVariable long associateId) {
        Associate associate = null;
        byte[] image = new byte[0];

        try {

            associate = associateService.findById(associateId);
            String associateCode = "https://github.com/rogeriofontes/" + associate.getCode();
            image = QRCodeGenerator.getQRCodeImage(associateCode, 250, 250);

        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Associate not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        String qrcode = Base64.getEncoder().encodeToString(image);
        model.addAttribute("qrcode", qrcode);
        model.addAttribute("associate", associate);
        return "associate-fonded-view";
    }

    @GetMapping(value = "/associates1")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Associate> associates = associateService.findAll(pageNumber, ROW_PER_PAGE);

        long count = associateService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("associates", associates);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "associate-find-event";
    }

}
