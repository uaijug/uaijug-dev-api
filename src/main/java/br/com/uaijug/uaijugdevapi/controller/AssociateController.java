package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import br.com.uaijug.uaijugdevapi.util.PDFGenerator;
import br.com.uaijug.uaijugdevapi.util.QRCodeGenerator;
import br.com.uaijug.uaijugdevapi.util.UrlUtils;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class AssociateController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private AssociateService associateService;

    @GetMapping(value = "/associates")
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

        return "associate-list";
    }

    @GetMapping(value = "/associates/{associateId}")
    public String getById(Model model, @PathVariable long associateId) {
        Associate associate = null;
        try {
            associate = associateService.findById(associateId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Associate not found");
        }
        model.addAttribute("associate", associate);
        return "associate";
    }

    @GetMapping(value = "/associates/{associateCode}/code")
    public ResponseEntity<InputStreamResource> getByCode(Model model, @PathVariable String associateCode) {

        try {
            Optional<Associate> associate = associateService.findByCode(associateCode);
            if (associate.isPresent()) {
                URI urlBase = UrlUtils.getUriAssociateFondedView(associate.get().getId());

                byte[] pngData = QRCodeGenerator.getQRCodeImage(String.valueOf(urlBase), 0, 0);

                InputStreamResource inputStreamResource =
                        PDFGenerator.InputStreamResource(pngData, "AWS UG Brain", associate.get());
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "inline; "
                        + "filename=ficha-inscricao-evento.pdf");

                return ResponseEntity.ok().headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(inputStreamResource);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @GetMapping(value = {"/associates/add"})
    public String showAdd(Model model) {
        Associate associate = new Associate();
        model.addAttribute("add", true);
        model.addAttribute("associate", associate);

        return "associate-edit";
    }

    @PostMapping(value = "/associates/add")
    public String add(Model model,
                      @ModelAttribute("associate") Associate associate) {
        try {
            Associate newAssociate = associateService.save(associate);
            return "redirect:/associates/" + String.valueOf(newAssociate.getId());
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

    @GetMapping(value = {"/associates/{associateId}/edit"})
    public String showEdit(Model model, @PathVariable long associateId) {

        Associate associate = null;
        try {
            associate = associateService.findById(associateId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Associate not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("associate", associate);

        return "associate-edit";
    }

    @PostMapping(value = {"/associates/{associateId}/edit"})
    public String update(Model model,
                         @PathVariable long associateId,
                         @ModelAttribute("associate") Associate associate) {
        try {
            associate.setId(associateId);
            associateService.update(associate);
            return "redirect:/associates/" + String.valueOf(associate.getId());
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

    @GetMapping(value = {"/associates/{associateId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long associateId) {
        Associate associate = null;
        try {
            associate = associateService.findById(associateId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Associate not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("associate", associate);
        return "associate";
    }

    @PostMapping(value = {"/associates/{associateId}/delete"})
    public String deleteById(
            Model model, @PathVariable long associateId) {
        try {
            associateService.deleteById(associateId);
            return "redirect:/associates";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "associate";
        }
    }
}
