package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Schedule;
import br.com.uaijug.uaijugdevapi.model.repository.ScheduleRepository;
import br.com.uaijug.uaijugdevapi.model.service.ScheduleService;
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
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    private boolean existsById(Long id) {
        return scheduleRepository.existsById(id);
    }

    @Override
    public Schedule findById(Long id) throws ResourceNotFoundException {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule == null) {
            throw new ResourceNotFoundException("Cannot find Course with id: " + id);
        } else return schedule;
    }

    @Override
    public List<Schedule> findAll(int pageNumber, int rowPerPage) {
        List<Schedule> schedulees = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        scheduleRepository.findAll(sortedByIdAsc).forEach(schedulees::add);
        return schedulees;
    }

    @Override
    public List<Schedule> list() {
        Iterable<Schedule> schedulees = scheduleRepository.findAll();
        return IteratorUtils.toList(schedulees.iterator());
    }

    @Override
    public Schedule save(Schedule schedule) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(schedule.getName())) {
            if (schedule.getId() != null && existsById(schedule.getId())) {
                throw new ResourceAlreadyExistsException("Course with id: " + schedule.getId() +
                        " already exists");
            }
            return scheduleRepository.save(schedule);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save course");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Schedule schedule)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(schedule.getName())) {
            if (!existsById(schedule.getId())) {
                throw new ResourceNotFoundException("Cannot find Course with id: " + schedule.getId());
            }
            scheduleRepository.save(schedule);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save course");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find course with id: " + id);
        } else {
            scheduleRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return scheduleRepository.count();
    }
}