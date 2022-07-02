package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.Course;
import br.com.uaijug.uaijugdevapi.model.domain.Developer;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {
}
