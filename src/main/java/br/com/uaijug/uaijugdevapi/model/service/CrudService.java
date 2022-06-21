package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;

public interface CrudService<T, ID extends Serializable> {
    T findById(ID id) throws ResourceNotFoundException;
    List<T> findAll(int pageNumber, int rowPerPage);
    T save(T t) throws BadResourceException, ResourceAlreadyExistsException;
    void update(T t)
            throws BadResourceException, ResourceNotFoundException;
    void deleteById(ID id) throws ResourceNotFoundException;
    Long count();
}
