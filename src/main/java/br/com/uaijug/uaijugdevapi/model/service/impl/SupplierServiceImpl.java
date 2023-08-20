package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Supplier;
import br.com.uaijug.uaijugdevapi.model.repository.SupplierRepository;
import br.com.uaijug.uaijugdevapi.model.service.SupplierService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    private boolean existsById(Long id) {
        return supplierRepository.existsById(id);
    }

    @Override
    public Supplier findById(Long id) throws ResourceNotFoundException {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier == null) {
            throw new ResourceNotFoundException("Cannot find Supplier with id: " + id);
        } else return supplier;
    }

    @Override
    public List<Supplier> findAll(int pageNumber, int rowPerPage) {
        List<Supplier> suppliers = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        supplierRepository.findAll(sortedByIdAsc).forEach(suppliers::add);
        return suppliers;
    }

    @Override
    public List<Supplier> list() {
        Iterable<Supplier> suppliers = supplierRepository.findAll();
        return IteratorUtils.toList(suppliers.iterator());
    }

    @Override
    public Supplier save(Supplier supplier) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(supplier.getName())) {
            if (supplier.getId() != null && existsById(supplier.getId())) {
                throw new ResourceAlreadyExistsException("Supplier with id: " + supplier.getId() +
                        " already exists");
            }

            if (supplier.getCode() == null)
                supplier.setCode(UUID.randomUUID().toString().substring(0, 6));

            return supplierRepository.save(supplier);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save supplier");
            exc.addErrorMessage("Supplier is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Supplier supplier)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(supplier.getName())) {
            if (!existsById(supplier.getId())) {
                throw new ResourceNotFoundException("Cannot find Supplier with id: " + supplier.getId());
            }
            supplierRepository.save(supplier);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save supplier");
            exc.addErrorMessage("Supplier is null or empty");
            throw exc;
        }
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find supplier with id: " + id);
        } else {
            supplierRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return supplierRepository.count();
    }
}