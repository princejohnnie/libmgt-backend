package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.PatronDto;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PatronController {

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PagedResourcesAssembler<PatronDto> pagedResourcesAssembler;

    @GetMapping("/patrons")
    PagedModel<EntityModel<PatronDto>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"name"}) Pageable paging) {
        Page<Patron> result = patronRepository.findAll(paging);

        return pagedResourcesAssembler.toModel(result.map(PatronDto::new));
    }

    @GetMapping("/patrons/{id}")
    PatronDto show(@PathVariable Long id) {
        return new PatronDto(patronRepository.findById(id).get());
    }

    @PostMapping("/patrons")
    PatronDto store(@RequestBody Patron patron) {
        return new PatronDto(patronRepository.save(patron));
    }

    @PutMapping("/patrons/{id}")
    PatronDto update(@RequestBody Patron newPatron, @PathVariable Long id) {
        var patron = patronRepository.findById(id).get();

        if (!patron.getName().equals(newPatron.getName())) {
            patron.setName(newPatron.getName());
        }
        if (!patron.getContact().equals(newPatron.getContact())) {
            patron.setContact(newPatron.getContact());
        }

        return new PatronDto(patronRepository.save(patron));
    }

    @DeleteMapping("/patrons/{id}")
    void delete(@PathVariable Long id) {
        patronRepository.deleteById(id);
    }
}
