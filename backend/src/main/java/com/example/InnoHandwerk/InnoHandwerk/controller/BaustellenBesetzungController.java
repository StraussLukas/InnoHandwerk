package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.example.InnoHandwerk.InnoHandwerk.service.BaustellenBesetzungService;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class BaustellenBesetzungController {

    @Autowired
    BaustellenBesetzungService service;

    @PostMapping(value = "/baustellenBesetzung")
    public String addBaustellenBesetzung(@RequestBody Baustellenbesetzung baustellenBesetzung){
        return service.addBaustellenBesetzung(baustellenBesetzung);
    }

    @GetMapping(value = "/baustellenBesetzung")
    public List<Baustellenbesetzung> getAllBaustellenBesetzung(){
        return service.getAllBaustellenBesetzung();
    }

    @GetMapping(value = "/baustellenBesetzung/{baustellenId}")
    public List<Baustellenbesetzung> getBaustellenBesetzungByBaustellenIdAndDatum(
            @PathVariable Integer baustellenId,
            @RequestParam("datum") Date datum) {
        return service.getBaustellenBesetzungByBaustellenIdAndDatum(baustellenId, datum);
    }

    @PutMapping(value = "/baustellenBesetzung")
    public Integer updateBaustellenBesetzung(@RequestBody Baustellenbesetzung baustellenBesetzung){
        return service.updateBaustellenBesetzung(baustellenBesetzung);
    }

    @DeleteMapping(value = "/baustellenBesetzung/{id}")
    public void deleteBaustellenBesetzungById(@PathVariable Integer id){
        service.deleteBaustellenBesetzungById(id);
    }
}
