package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.EventRegistration;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRegistrationRepository extends PagingAndSortingRepository<EventRegistration, Long>, JpaSpecificationExecutor<EventRegistration> {
}
