package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.service.BaustelleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class BaustelleController {

    @Autowired
    BaustelleService service;

    @PostMapping(value = "/baustelle")
    public String addBaustelle(@RequestBody Baustelle baustelle){
        return service.addBaustelle(baustelle);
    }

    @GetMapping(value = "/baustelle")
    public List<Baustelle> getAllBaustelle(){
        return service.getAllBaustelle();
    }

    @GetMapping(value = "/baustelle/{id}")
    public Baustelle getBaustelleById(@PathVariable Integer id){
        return service.getBaustelleById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
    }

    @PutMapping(value = "/baustelle")
    public String updateBaustelle(@RequestBody Baustelle baustelle){
        return service.updateBaustelle(baustelle);
    }

    @DeleteMapping(value = "/baustelle/{id}")
    public void deleteBaustelleById(@PathVariable Integer id){
        service.deleteBaustelleById(id);
    }
}
