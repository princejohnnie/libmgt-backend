package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.dtos.PatronDto;
import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.request.CreatePatronRequest;
import com.johnny.libmgtbackend.request.UpdatePatronRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatronService {
    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PagedResourcesAssembler<PatronDto> pagedResourcesAssembler;


    @Cacheable(cacheManager = "patronCacheManager", value = "patrons")
    public PagedModel<EntityModel<PatronDto>> getAllPatrons(Pageable paging) {
        Page<Patron> result = patronRepository.findAll(paging);

        return pagedResourcesAssembler.toModel(result.map(PatronDto::new));
    }

    @Cacheable(cacheManager = "patronCacheManager", value = "patrons", key = "#id")
    public PatronDto getPatron(Long id) {
        return new PatronDto(patronRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Patron.class, id)));
    }

    public PatronDto createPatron(CreatePatronRequest request) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();
        var patron = new Patron(request.name, request.contact);
        return new PatronDto(patronRepository.save(patron));
    }

    @Transactional
    public PatronDto updatePatron(UpdatePatronRequest request, Long id) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();

        var patron = patronRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Patron.class, id));

        if (request.name != null && !patron.getName().equals(request.name)) {
            patron.setName(request.name);
        }
        if (request.contact != null && !patron.getContact().equals(request.contact)) {
            patron.setContact(request.contact);
        }

        return new PatronDto(patronRepository.save(patron));
    }

    public void deletePatron(Long id) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();
        patronRepository.deleteById(id);
    }
}
