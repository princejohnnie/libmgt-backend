package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.dtos.PatronDto;
import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.request.CreatePatronRequest;
import com.johnny.libmgtbackend.request.UpdatePatronRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class PatronService {
    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PagedResourcesAssembler<PatronDto> pagedResourcesAssembler;


    public PagedModel<EntityModel<PatronDto>> getAllPatrons(Pageable paging) {
        Page<Patron> result = patronRepository.findAll(paging);

        return pagedResourcesAssembler.toModel(result.map(PatronDto::new));
    }

    public PatronDto getPatron(Long id) {
        return new PatronDto(patronRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Patron.class, id)));
    }

    public PatronDto createPatron(CreatePatronRequest request) {
        var patron = new Patron(request.name, request.contact);
        return new PatronDto(patronRepository.save(patron));
    }

    public PatronDto updatePatron(UpdatePatronRequest request, Long id) {
        var patron = patronRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Patron.class, id));

        if (request.name != null && !patron.getName().equals(request.name)) {
            patron.setName(request.name);
        }
        if (request.contact != null && !patron.getContact().equals(request.contact)) {
            patron.setContact(request.contact);
        }

        return new PatronDto(patronRepository.save(patron));
    }

    public void deletePatron(Long id) {
        patronRepository.deleteById(id);
    }
}
