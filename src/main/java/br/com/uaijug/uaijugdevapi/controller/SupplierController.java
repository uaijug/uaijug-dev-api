package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Supplier;
import br.com.uaijug.uaijugdevapi.model.service.SupplierService;
import br.com.uaijug.uaijugdevapi.util.PDFGenerator;
import br.com.uaijug.uaijugdevapi.util.QRCodeGenerator;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class SupplierController {

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private SupplierService supplierService;

    @GetMapping(value = "/suppliers")
    public String get(Model model,
                      @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Supplier> suppliers = supplierService.findAll(pageNumber, ROW_PER_PAGE);

        long count = supplierService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "supplier-list";
    }

    @GetMapping(value = "/suppliers/{supplierId}")
    public String getById(Model model, @PathVariable long supplierId) {
        Supplier supplier = null;
        try {
            supplier = supplierService.findById(supplierId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Supplier not found");
        }
        model.addAttribute("supplier", supplier);
        return "supplier";
    }

    @GetMapping(value = {"/suppliers/add"})
    public String showAdd(Model model) {
        Supplier supplier = new Supplier();
        model.addAttribute("add", true);
        model.addAttribute("supplier", supplier);

        return "supplier-edit";
    }

    @PostMapping(value = "/suppliers/add")
    public String add(Model model,
                      @ModelAttribute("supplier") Supplier supplier) {
        try {
            Supplier newSupplier = supplierService.save(supplier);
            return "redirect:/suppliers/" + String.valueOf(newSupplier.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("supplier", supplier);
            model.addAttribute("add", true);
            return "supplier-edit";
        }
    }

    @GetMapping(value = {"/suppliers/{supplierId}/edit"})
    public String showEdit(Model model, @PathVariable long supplierId) {

        Supplier supplier = null;
        try {
            supplier = supplierService.findById(supplierId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Supplier not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("supplier", supplier);

        return "supplier-edit";
    }

    @PostMapping(value = {"/suppliers/{supplierId}/edit"})
    public String update(Model model,
                         @PathVariable long supplierId,
                         @ModelAttribute("supplier") Supplier supplier) {
        try {
            supplier.setId(supplierId);
            supplierService.update(supplier);
            return "redirect:/suppliers/" + String.valueOf(supplier.getId());
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

    @GetMapping(value = {"/suppliers/{supplierId}/delete"})
    public String showDeleteById(
            Model model, @PathVariable long supplierId) {
        Supplier supplier = null;
        try {
            supplier = supplierService.findById(supplierId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Supplier not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("supplier", supplier);
        return "supplier";
    }

    @PostMapping(value = {"/suppliers/{supplierId}/delete"})
    public String deleteById(
            Model model, @PathVariable long supplierId) {
        try {
            supplierService.deleteById(supplierId);
            return "redirect:/suppliers";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "supplier";
        }
    }
}
