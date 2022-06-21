package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.exceptions.BadResourceException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceAlreadyExistsException;
import br.com.uaijug.uaijugdevapi.exceptions.ResourceNotFoundException;
import br.com.uaijug.uaijugdevapi.model.domain.Developer;
import br.com.uaijug.uaijugdevapi.model.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeveloperServiceImpl implements DeveloperService {
    @Autowired
    private DeveloperRepository developerRepository;

    private boolean existsById(Long id) {
        return developerRepository.existsById(id);
    }

    @Override
    public Developer findById(Long id) throws ResourceNotFoundException {
        Developer developer = developerRepository.findById(id).orElse(null);
        if (developer==null) {
            throw new ResourceNotFoundException("Cannot find Developer with id: " + id);
        }
        else return developer;
    }

    @Override
    public List<Developer> findAll(int pageNumber, int rowPerPage) {
        List<Developer> developers = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        developerRepository.findAll(sortedByIdAsc).forEach(developers::add);
        return developers;
    }

    @Override
    public Developer save(Developer developer) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(developer.getName())) {
            if (developer.getId() != null && existsById(developer.getId())) {
                throw new ResourceAlreadyExistsException("Developer with id: " + developer.getId() +
                        " already exists");
            }
            return developerRepository.save(developer);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save developer");
            exc.addErrorMessage("Developer is null or empty");
            throw exc;
        }
    }

    @Override
    public void update(Developer developer)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(developer.getName())) {
            if (!existsById(developer.getId())) {
                throw new ResourceNotFoundException("Cannot find Developer with id: " + developer.getId());
            }
            developerRepository.save(developer);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save developer");
            exc.addErrorMessage("Developer is null or empty");
            throw exc;
        }
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find developer with id: " + id);
        }
        else {
            developerRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return developerRepository.count();
    }
}