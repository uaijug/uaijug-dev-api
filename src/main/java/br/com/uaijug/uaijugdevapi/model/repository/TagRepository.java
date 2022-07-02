package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.Tag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long>, JpaSpecificationExecutor<Tag > {
}
