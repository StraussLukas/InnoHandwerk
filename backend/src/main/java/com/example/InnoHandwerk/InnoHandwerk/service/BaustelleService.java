package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.repository.BaustelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BaustelleService {

    @Autowired
    BaustelleRepository repository;

    @Autowired
    BaustellenBesetzungService baustellenBesetzungService;

    public Integer addBaustelle(Baustelle baustelle) {
        return repository.save(baustelle).getId();
    }

    public List<Baustelle> getAllBaustelle() {
        return repository.findAll();
    }

    public List<Baustelle> getAllBaustellenByPersonalnummer(Integer personalnummer) {
        return repository.findAllById(baustellenBesetzungService.getAllBaustellenIdsByPersonalnummer(personalnummer));
    }

    public Optional<Baustelle> getBaustelleById(Integer id) {
        return repository.findById(id);
    }

    public String updateBaustelle(Baustelle baustelle) {
        return repository.save(baustelle).getId().toString();
    }

    public void deleteBaustelleById(Integer id) {
        repository.deleteById(id);
    }

    public List<Baustelle> getAllBaustellenByStatus(String status){
        return repository.findAllByStatusOrderByZeitstempelAsc(status);
    }
}
