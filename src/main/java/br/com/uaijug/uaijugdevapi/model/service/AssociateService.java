package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;

import java.util.Optional;

public interface AssociateService extends CrudService<Associate, Long>{
    //Optional<Associate> findByName(String name);
    Optional<Associate> findByCode(String code);
    //Optional<Associate> findByEmail(String email);

    Optional<Associate> findByNameOrCodeOrEmail(String name, String code, String email);
}
