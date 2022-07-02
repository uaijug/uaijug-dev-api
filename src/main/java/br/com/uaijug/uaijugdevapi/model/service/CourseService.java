package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Course;

public interface CourseService extends CrudService<Course, Long>{
    Course findByTitle(String title) throws ResourceNotFoundException;
}
