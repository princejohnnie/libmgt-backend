package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.PatronDto;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.service.PatronService;
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
    private PatronService patronService;

    @GetMapping("/patrons")
    PagedModel<EntityModel<PatronDto>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"name"}) Pageable paging) {
        return patronService.getAllPatrons(paging);
    }

    @GetMapping("/patrons/{id}")
    PatronDto show(@PathVariable Long id) {
        return patronService.getPatron(id);
    }

    @PostMapping("/patrons")
    PatronDto store(@RequestBody Patron patron) {
        return patronService.createPatron(patron);
    }

    @PutMapping("/patrons/{id}")
    PatronDto update(@RequestBody Patron newPatron, @PathVariable Long id) {
        return patronService.updatePatron(newPatron, id);
    }

    @DeleteMapping("/patrons/{id}")
    void delete(@PathVariable Long id) {
        patronService.deletePatron(id);
    }
}
