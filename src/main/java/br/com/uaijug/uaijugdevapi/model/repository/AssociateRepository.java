package br.com.uaijug.uaijugdevapi.model.repository;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.domain.Developer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//https://jschmitz.dev/posts/how_to_use_spring_datas_specification/
@Repository
public interface AssociateRepository extends PagingAndSortingRepository<Associate, Long>, JpaSpecificationExecutor<Developer> {

    //List<Person> findAllByIdOrCustomId(someId, someCustomId);
    Optional<Associate> findByNameOrCodeOrEmail(String name, String code, String email);
    Optional<Associate> findByCode(String code);
    //Optional<Associate> findByEmail(String email);
}
