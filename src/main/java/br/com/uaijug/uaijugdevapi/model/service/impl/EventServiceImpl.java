package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import br.com.uaijug.uaijugdevapi.model.repository.EventRepository;
import br.com.uaijug.uaijugdevapi.model.service.EventService;
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
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    private boolean existsById(Long id) {
        return eventRepository.existsById(id);
    }

    @Override
    public Event findById(Long id) throws ResourceNotFoundException {
        Event event = eventRepository.findById(id).orElse(null);
        if (event==null) {
            throw new ResourceNotFoundException("Cannot find Event with id: " + id);
        }
        else return event;
    }

    @Override
    public List<Event> findAll(int pageNumber, int rowPerPage) {
        List<Event> events = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        eventRepository.findAll(sortedByIdAsc).forEach(events::add);
        return events;
    }

    @Override
    public List<Event> list() {
        Iterable<Event> events = eventRepository.findAll();
        return IteratorUtils.toList(events.iterator());
    }

    @Override
    public Event save(Event event) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(event.getName())) {
            if (event.getId() != null && existsById(event.getId())) {
                throw new ResourceAlreadyExistsException("Event with id: " + event.getId() +
                        " already exists");
            }

            return eventRepository.save(event);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save event");
            exc.addErrorMessage("Event is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Event event)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(event.getName())) {
            if (!existsById(event.getId())) {
                throw new ResourceNotFoundException("Cannot find Event with id: " + event.getId());
            }
            eventRepository.save(event);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save event");
            exc.addErrorMessage("Event is null or empty");
            throw exc;
        }
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find event with id: " + id);
        }
        else {
            eventRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return eventRepository.count();
    }
}