package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.ErrorDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping("/patrons")
    ResponseEntity<PagedModel<EntityModel<PatronDto>>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"name"}) Pageable paging) {
        return new ResponseEntity<>(patronService.getAllPatrons(paging), HttpStatus.OK);
    }

    @GetMapping("/patrons/{id}")
    ResponseEntity<PatronDto> show(@PathVariable Long id) {
        return new ResponseEntity<>(patronService.getPatron(id), HttpStatus.OK);
    }

    @PostMapping("/patrons")
    ResponseEntity<Object> store(@RequestBody @Valid CreatePatronRequest request) {
        try {
            var patronDto = patronService.createPatron(request);
            return new ResponseEntity<>(patronDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/patrons/{id}")
    ResponseEntity<Object> update(@RequestBody @Valid UpdatePatronRequest request, @PathVariable Long id) {
        try {
            var patronDto = patronService.updatePatron(request, id);
            return new ResponseEntity<>(patronDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/patrons/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            patronService.deletePatron(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
