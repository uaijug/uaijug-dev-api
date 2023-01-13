package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;

import java.util.Optional;

public interface AssociateFind {
    Optional<Associate> find(String finded, int type);
}
