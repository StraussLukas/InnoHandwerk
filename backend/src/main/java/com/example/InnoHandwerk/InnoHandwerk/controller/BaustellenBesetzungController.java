package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.example.InnoHandwerk.InnoHandwerk.service.BaustellenBesetzungService;
import com.example.InnoHandwerk.InnoHandwerk.entity.BaustellenBesetzung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class BaustellenBesetzungController {

    @Autowired
    BaustellenBesetzungService service;

    @PostMapping(value = "/baustellenBesetzung")
    public String addBaustellenBesetzung(@RequestBody BaustellenBesetzung baustellenBesetzung){
        return service.addBaustellenBesetzung(baustellenBesetzung);
    }

    @GetMapping(value = "/baustellenBesetzung")
    public List<BaustellenBesetzung> getAllBaustellenBesetzung(){
        return service.getAllBaustellenBesetzung();
    }

    @GetMapping(value = "/baustellenBesetzung/{personalnummer}")
    public BaustellenBesetzung getBaustellenBesetzungByPersonalnummer(@PathVariable Integer personalnummer){
        return service.getBaustellenBesetzungByPersonalnummer(personalnummer).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
    }

    @PutMapping(value = "/baustellenBesetzung")
    public String updateBaustellenBesetzung(@RequestBody BaustellenBesetzung baustellenBesetzung){
        return service.updateBaustellenBesetzung(baustellenBesetzung);
    }

    @DeleteMapping(value = "/baustellenBesetzung/{personalnummer}")
    public void deleteBaustellenBesetzungByPersonalnummer(@PathVariable Integer personalnummer){
        service.deleteBaustellenBesetzungByPersonalnummer(personalnummer);
    }
}
