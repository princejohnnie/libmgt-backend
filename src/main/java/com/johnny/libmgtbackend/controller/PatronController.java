package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PatronController {

    @Autowired
    private PatronRepository patronRepository;

    @GetMapping("/patrons")
    List<Patron> index() {
        return patronRepository.findAll();
    }

    @GetMapping("/patrons/{id}")
    Patron show(@PathVariable Long id) {
        return patronRepository.findById(id).get();
    }

    @PostMapping("/patrons")
    Patron store(@RequestBody Patron patron) {
        return patronRepository.save(patron);
    }

    @PutMapping("/patrons/{id}")
    Patron update(@RequestBody Patron newPatron, @PathVariable Long id) {
        var patron = patronRepository.findById(id).get();

        if (!patron.getName().equals(newPatron.getName())) {
            patron.setName(newPatron.getName());
        }
        if (!patron.getContact().equals(newPatron.getContact())) {
            patron.setContact(newPatron.getContact());
        }

        return patronRepository.save(patron);
    }

    @DeleteMapping("/patrons/{id}")
    void delete(@PathVariable Long id) {
        patronRepository.deleteById(id);
    }
}
