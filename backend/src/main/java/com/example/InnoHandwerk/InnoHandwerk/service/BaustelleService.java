package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.repository.BaustelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaustelleService {

    @Autowired
    BaustelleRepository repository;

    public String addBaustelle(Baustelle baustelle) {
        return repository.save(baustelle).getId().toString();
    }

    public List<Baustelle> getAllBaustelle() {
        return repository.findAll();
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
}
