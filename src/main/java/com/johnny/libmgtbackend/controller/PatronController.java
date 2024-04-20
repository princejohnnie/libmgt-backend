package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.PatronDto;
import com.johnny.libmgtbackend.request.CreatePatronRequest;
import com.johnny.libmgtbackend.request.UpdatePatronRequest;
import com.johnny.libmgtbackend.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    PatronDto store(@RequestBody @Valid CreatePatronRequest request) {
        return patronService.createPatron(request);
    }

    @PutMapping("/patrons/{id}")
    PatronDto update(@RequestBody @Valid UpdatePatronRequest request, @PathVariable Long id) {
        return patronService.updatePatron(request, id);
    }

    @DeleteMapping("/patrons/{id}")
    void delete(@PathVariable Long id) {
        patronService.deletePatron(id);
    }
}
