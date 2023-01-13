package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Tag;
import br.com.uaijug.uaijugdevapi.model.repository.TagRepository;
import br.com.uaijug.uaijugdevapi.model.service.TagService;
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

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    private boolean existsById(Long id) {
        return tagRepository.existsById(id);
    }

    @Override
    public Tag findById(Long id) throws ResourceNotFoundException {
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            throw new ResourceNotFoundException("Cannot find Course with id: " + id);
        } else return tag;
    }

    @Override
    public List<Tag> findAll(int pageNumber, int rowPerPage) {
        List<Tag> tages = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        tagRepository.findAll(sortedByIdAsc).forEach(tages::add);
        return tages;
    }

    @Override
    public List<Tag> list() {
        Iterable<Tag> tages = tagRepository.findAll();
        return IteratorUtils.toList(tages.iterator());
    }

    @Override
    public Tag save(Tag tag) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(tag.getName())) {
            if (tag.getId() != null && existsById(tag.getId())) {
                throw new ResourceAlreadyExistsException("Course with id: " + tag.getId() +
                        " already exists");
            }
            return tagRepository.save(tag);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save Tag");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Tag tag)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(tag.getName())) {
            if (!existsById(tag.getId())) {
                throw new ResourceNotFoundException("Cannot find Course with id: " + tag.getId());
            }
            tagRepository.save(tag);
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
            tagRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return tagRepository.count();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }
}