package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Course;
import br.com.uaijug.uaijugdevapi.model.repository.CourseRepository;
import br.com.uaijug.uaijugdevapi.model.service.CourseService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    private boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    private boolean existsByTitle(String title) {
        Optional<Course> byTitle = courseRepository.findByTitle(title);
        if (byTitle.isPresent()) {
            return Boolean.TRUE;
        } else  {
            return Boolean.FALSE;
        }
    }

    @Override
    public Course findById(Long id) throws ResourceNotFoundException {
        Course course = courseRepository.findById(id).orElse(null);
        if (course==null) {
            throw new ResourceNotFoundException("Cannot find Course with id: " + id);
        }
        else return course;
    }

    @Override
    public Course findByTitle(String title) throws ResourceNotFoundException {
        Course course = courseRepository.findByTitle(title).orElse(null);
        if (course==null) {
            throw new ResourceNotFoundException("Cannot find Course with title: " + title);
        }
        else return course;
    }

    @Override
    public List<Course> findAll(int pageNumber, int rowPerPage) {
        List<Course> courses = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        courseRepository.findAll(sortedByIdAsc).forEach(courses::add);
        return courses;
    }

    @Override
    public Page<Course> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return courseRepository.findAll(pageable);
    }

    @Override
    public List<Course> list() {
        Iterable<Course> courses = courseRepository.findAll();
        return IteratorUtils.toList(courses.iterator());
    }

    @Override
    public Course save(Course course) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(course.getTitle())) {
            if (course.getId() != null && existsById(course.getId())) {
                throw new ResourceAlreadyExistsException("Course with id: " + course.getId() +
                        " already exists");
            }

            if (existsByTitle(course.getTitle())) {
                throw new ResourceAlreadyExistsException("Course with id: " + course.getTitle() +
                        " already exists");
            }

            if (course.getEndDate().isBefore(course.getStartDate())) {
                throw new ResourceAlreadyExistsException("End date: " + course.getEndDate() + " is before Start Date: " + course.getStartDate());
            }

            return courseRepository.save(course);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save course");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Course course)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(course.getTitle())) {
            if (!existsById(course.getId())) {
                throw new ResourceNotFoundException("Cannot find Course with id: " + course.getId());
            }
            courseRepository.save(course);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save course");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find course with id: " + id);
        }
        else {
            courseRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return courseRepository.count();
    }
}