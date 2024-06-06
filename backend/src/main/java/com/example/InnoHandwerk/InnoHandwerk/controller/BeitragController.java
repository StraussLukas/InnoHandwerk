package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import com.example.InnoHandwerk.InnoHandwerk.service.BeitragService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class BeitragController {

    @Autowired
    BeitragService service;

    @GetMapping(value = "/beitrag/{id}")
    public Beitrag getBeitrag(@PathVariable Integer id){
        return service.getBeitragById(id).orElseThrow(
                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found"));
    }

    @GetMapping(value = "/beitrag")
    public List<Beitrag> getAllBeitrag(){
        return service.getAllBeitrag();
    }

    @GetMapping(value = "/beitragByBaustelle/{baustellenId}")
    public List<Beitrag> getBeitragByBaustellenId(@PathVariable Integer baustellenId){
        return service.getAllBeitragByBaustellenId(baustellenId);
    }

    @PostMapping(value = "/beitrag")
    public Integer addBeitrag(@RequestBody Beitrag beitrag){
        return service.addBeitrag(beitrag);
    }

    @PutMapping(value = "/beitrag")
    public Integer updateBeitrag(@RequestBody Beitrag beitrag){
        return service.updateBeitrag(beitrag);
    }

    @DeleteMapping(value = "/beitrag/{id}")
    public void deleteBeitragById(@PathVariable Integer id){
        service.deleteBeitragById(id);
    }

    @Transactional
    @DeleteMapping(value = "/beitraegeByBaustelle/{baustellenId}")
    public void deleteAllBeitragByBaustellenId(@PathVariable Integer baustellenId){
        service.deleteAllBeitragByBaustellenId(baustellenId);
    }
}