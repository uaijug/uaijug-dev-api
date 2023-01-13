package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.repository.AssociateRepository;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
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
public class AssociateServiceImpl implements AssociateService {
    @Autowired
    private AssociateRepository associateRepository;

    private boolean existsById(Long id) {
        return associateRepository.existsById(id);
    }

    @Override
    public Associate findById(Long id) throws ResourceNotFoundException {
        Associate associate = associateRepository.findById(id).orElse(null);
        if (associate == null) {
            throw new ResourceNotFoundException("Cannot find Associate with id: " + id);
        } else return associate;
    }

    @Override
    public List<Associate> findAll(int pageNumber, int rowPerPage) {
        List<Associate> associates = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        associateRepository.findAll(sortedByIdAsc).forEach(associates::add);
        return associates;
    }

    @Override
    public List<Associate> list() {
        Iterable<Associate> associates = associateRepository.findAll();
        return IteratorUtils.toList(associates.iterator());
    }

    @Override
    public Associate save(Associate associate) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(associate.getName())) {
            if (associate.getId() != null && existsById(associate.getId())) {
                throw new ResourceAlreadyExistsException("Associate with id: " + associate.getId() +
                        " already exists");
            }

            if (associate.getCode() == null)
                associate.setCode(UUID.randomUUID().toString().substring(0, 6));

            return associateRepository.save(associate);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save associate");
            exc.addErrorMessage("Associate is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Associate associate)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(associate.getName())) {
            if (!existsById(associate.getId())) {
                throw new ResourceNotFoundException("Cannot find Associate with id: " + associate.getId());
            }
            associateRepository.save(associate);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save associate");
            exc.addErrorMessage("Associate is null or empty");
            throw exc;
        }
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find associate with id: " + id);
        } else {
            associateRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return associateRepository.count();
    }

    @Override
    public Optional<Associate> findByNameOrCodeOrEmail(String name, String code, String email) {
        return associateRepository.findByNameOrCodeOrEmail(name, code, email);
    }

    /*@Override
    public Optional<Associate> findByName(String name) {
        return associateRepository.findByName(name);
    }*/

    @Override
    public Optional<Associate> findByCode(String code) {
        return associateRepository.findByCode(code);
    }

     /*@Override
    public Optional<Associate> findByEmail(String email) {
        return associateRepository.findByEmail(email);
    }*/
}