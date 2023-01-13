package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.model.domain.Tag;

import java.util.Optional;

public interface TagService extends CrudService<Tag, Long>{
    Optional<Tag> findByName(String name);
}
