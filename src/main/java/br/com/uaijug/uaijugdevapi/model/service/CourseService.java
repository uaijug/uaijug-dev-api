package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Course;
import org.springframework.data.domain.Page;

public interface CourseService extends CrudService<Course, Long>{
    Course findByTitle(String title) throws ResourceNotFoundException;

    Page<Course> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection);
}
