package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.Supplier;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SupplierRepository extends PagingAndSortingRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
}
