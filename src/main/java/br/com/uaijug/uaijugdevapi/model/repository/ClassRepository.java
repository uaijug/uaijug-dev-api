package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.Class;
import br.com.uaijug.uaijugdevapi.model.domain.Course;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends PagingAndSortingRepository<Class, Long>, JpaSpecificationExecutor<Class> {
}
