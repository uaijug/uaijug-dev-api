package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import br.com.uaijug.uaijugdevapi.model.dto.AssociateEventDTO;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import br.com.uaijug.uaijugdevapi.model.service.EventService;
import br.com.uaijug.uaijugdevapi.util.QRCodeGenerator;
import br.com.uaijug.uaijugdevapi.util.UrlUtils;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;

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

        //model.addAttribute("errorMessage", "");
        List<Event> eventList = eventService.list();
        model.addAttribute("eventList", eventList);
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

            String code = associateEventDTO.getCode();

            if (StringUtils.isEmpty(code)) {
                model.addAttribute("errorMessage", "Você deve escolher uma opção para fazer a busca ");

                model.addAttribute("add", true);
                model.addAttribute("associateEventDTO", new AssociateEventDTO());


                List<Event> eventList = eventService.list();
                model.addAttribute("eventList", eventList);
                return "associate-find-event";
            } else {
                Optional<Associate> associate = associateService.findByCode(code);
                if (associate.isPresent()) {
                    Associate associateResponse = associate.get();
                    log.info("Dados Recebidos: " + associateResponse);

                    Long eventId = associateEventDTO.getEventId();
                    if (eventId != null) {
                        Event byEventId = eventService.findById(associateEventDTO.getEventId());
                    } else {
                        model.addAttribute("errorMessage", "O Evento deve ser escolhido e o código de registro não pode ficar vazio");

                        model.addAttribute("add", true);
                        model.addAttribute("associateEventDTO", new AssociateEventDTO());


                        List<Event> eventList = eventService.list();
                        model.addAttribute("eventList", eventList);
                        return "associate-find-event";
                    }
                    //TODO - EventRegistration (Event, Associate)

                    model.addAttribute("associate", associateResponse);
                    return "associate-fonded-event";
                } else {
                    model.addAttribute("associate", associate);
                    model.addAttribute("errorMessage", "O Evento deve ser escolhido e o código de registro não pode ficar vazio");

                    model.addAttribute("add", true);
                    model.addAttribute("associateEventDTO", new AssociateEventDTO());


                    List<Event> eventList = eventService.list();
                    model.addAttribute("eventList", eventList);
                    return "associate-find-event";
                }
            }
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);

            //model.addAttribute("associate", associate);
            model.addAttribute("add", true);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("associate", new Associate());
            return "associate-fonded-event";
        }
    }

    @PostMapping(value = {"/associate-find/{associateId}/edit"})
    public String update(Model model,
                         @PathVariable long associateId,
                         @Valid @ModelAttribute("associate") Associate associate, BindingResult result) {
        try {

            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();

                for (FieldError fieldError : result.getFieldErrors()) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                model.addAttribute("errorMessage", errors);

                model.addAttribute("add", false);

                Associate byId = associateService.findById(associateId);
                if (byId != null) {
                    model.addAttribute("associate", byId);
                } else {
                    model.addAttribute("associate", new Associate());
                }

                return "associate-fonded-event";
            }
            associate.update(associateId, associate);
            associateService.update(associate);
            return "redirect:/associate-fonded-view/" + associate.getId();
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("associate", new Associate());
            return "associate-fonded-event";
        }
    }


    @GetMapping(value = "/associate-fonded-view/{associateId}")
    public String getById(Model model, @PathVariable long associateId) {
        byte[] image = new byte[0];

        try {
            Associate associate = associateService.findById(associateId);
            URI urlBase = UrlUtils.getUriAssociateFondedView(associate.getId());

            image = QRCodeGenerator.getQRCodeImage(String.valueOf(urlBase), 250, 250);

            String qrcode = Base64.getEncoder().encodeToString(image);
            model.addAttribute("qrcode", qrcode);
            model.addAttribute("associate", associate);
            return "associate-fonded-view";

        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Associate not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        String qrcode = Base64.getEncoder().encodeToString(image);
        model.addAttribute("qrcode", qrcode);
        model.addAttribute("associate", new Associate());
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
