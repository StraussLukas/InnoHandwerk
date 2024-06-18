package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import com.example.InnoHandwerk.InnoHandwerk.service.AnhangService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class AnhangController {

    public AnhangController() {
        super();
    }

    @Autowired
    AnhangService service;

    @GetMapping(value = "/anhang/{id}")
    public Anhang getAnhang(@PathVariable Integer id){
        return service.getAnhangById(id).orElseThrow(
                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found"));
    }

    @GetMapping(value = "/anhang")
    public List<Anhang> getAllAnhaenge(){
        return service.getAllAnhaenge();
    }

    @GetMapping(value = "/anhaengeByBeitrag/{beitragId}")
    public List<Anhang> getAnhaengeByBeitragId(@PathVariable Integer beitragId){
        return service.getAllAnhaengeByBeitragId(beitragId);
    }

    @PostMapping(value = "/anhang")
    public Integer addAnhang(@RequestBody Anhang anhang){
        return service.addAnhang(anhang);
    }

    @PostMapping(value = "/anhang/{beitragId}")
    public String addAnhangRefactored(@PathVariable Integer beitragId){
        return service.addAnhangRefactored(beitragId);
    }

    @PutMapping(value = "/anhang")
    public Integer updateAnhang(@RequestBody Anhang anhang){
        return service.updateAnhang(anhang);
    }

    @DeleteMapping(value = "/anhang/{id}")
    public void deleteAnhangById(@PathVariable Integer id){
        service.deleteAnhangById(id);
    }

    @Transactional
    @DeleteMapping(value = "/anhaengeByBeitrag/{id}")
    public void deleteAllAnhaengeByBeitragId (@PathVariable Integer id){
        service.deleteAllAnhaengeByBeitragId(id);
    }
}