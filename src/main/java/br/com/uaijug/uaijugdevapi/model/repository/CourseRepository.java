package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.Course;
import br.com.uaijug.uaijugdevapi.model.domain.Developer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findByTitle(String title);
}
