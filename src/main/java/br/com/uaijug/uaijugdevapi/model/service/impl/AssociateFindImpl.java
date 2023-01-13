package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.service.AssociateFind;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssociateFindImpl implements AssociateFind {

    @Autowired
    private AssociateService associateService;

    @Override
    public Optional<Associate> find(String finded, int type) {
        return null;
    }
}
