package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Class;
import br.com.uaijug.uaijugdevapi.model.repository.ClassRepository;
import br.com.uaijug.uaijugdevapi.model.service.ClassService;
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
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    private boolean existsById(Long id) {
        return classRepository.existsById(id);
    }

    @Override
    public Class findById(Long id) throws ResourceNotFoundException {
        Class clazz = classRepository.findById(id).orElse(null);
        if (clazz == null) {
            throw new ResourceNotFoundException("Cannot find Course with id: " + id);
        } else return clazz;
    }

    @Override
    public List<Class> findAll(int pageNumber, int rowPerPage) {
        List<Class> clazzes = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        classRepository.findAll(sortedByIdAsc).forEach(clazzes::add);
        return clazzes;
    }

    @Override
    public List<Class> list() {
        Iterable<Class> classes = classRepository.findAll();
        return IteratorUtils.toList(classes.iterator());
    }

    @Override
    public Class save(Class clazz) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(clazz.getName())) {
            if (clazz.getId() != null && existsById(clazz.getId())) {
                throw new ResourceAlreadyExistsException("Course with id: " + clazz.getId() +
                        " already exists");
            }
            return classRepository.save(clazz);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save course");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Class clazz)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(clazz.getName())) {
            if (!existsById(clazz.getId())) {
                throw new ResourceNotFoundException("Cannot find Course with id: " + clazz.getId());
            }
            classRepository.save(clazz);
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
            classRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return classRepository.count();
    }
}