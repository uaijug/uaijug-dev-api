package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.EventRegistration;
import br.com.uaijug.uaijugdevapi.model.repository.EventRegistrationRepository;
import br.com.uaijug.uaijugdevapi.model.service.EventRegistrationService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventRegistrationServiceImpl implements EventRegistrationService {
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    private boolean existsById(Long id) {
        return eventRegistrationRepository.existsById(id);
    }

    @Override
    public EventRegistration findById(Long id) throws ResourceNotFoundException {
        EventRegistration eventRegistration = eventRegistrationRepository.findById(id).orElse(null);
        if (eventRegistration == null) {
            throw new ResourceNotFoundException("Cannot find EventRegistration with id: " + id);
        } else return eventRegistration;
    }

    @Override
    public List<EventRegistration> findAll(int pageNumber, int rowPerPage) {
        List<EventRegistration> eventRegistrations = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        eventRegistrationRepository.findAll(sortedByIdAsc).forEach(eventRegistrations::add);
        return eventRegistrations;
    }

    @Override
    public List<EventRegistration> list() {
        Iterable<EventRegistration> eventRegistrations = eventRegistrationRepository.findAll();
        return IteratorUtils.toList(eventRegistrations.iterator());
    }

    @Override
    public EventRegistration save(EventRegistration eventRegistration) throws BadResourceException, ResourceAlreadyExistsException {
        return eventRegistrationRepository.save(eventRegistration);
    }

    @Override
    public void update(EventRegistration eventRegistration) throws BadResourceException, ResourceNotFoundException {

    }

   /* @Override
    public void update(EventRegistration eventRegistration)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(eventRegistration.getName())) {
            if (!existsById(eventRegistration.getId())) {
                throw new ResourceNotFoundException("Cannot find EventRegistration with id: " + eventRegistration.getId());
            }
            eventRegistrationRepository.save(eventRegistration);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save eventRegistration");
            exc.addErrorMessage("EventRegistration is null or empty");
            throw exc;
        }
    }*/

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find eventRegistration with id: " + id);
        } else {
            eventRegistrationRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return eventRegistrationRepository.count();
    }
}