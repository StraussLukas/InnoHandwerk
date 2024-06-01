package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.BaustellenBesetzung;
import com.example.InnoHandwerk.InnoHandwerk.repository.BaustellenBesetzungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaustellenBesetzungService {

    @Autowired
    BaustellenBesetzungRepository repository;

    public String addBaustellenBesetzung(BaustellenBesetzung baustellenBesetzung) {
        return repository.save(baustellenBesetzung).getPersonalnummer().toString();
    }

    public List<BaustellenBesetzung> getAllBaustellenBesetzung() {
        return repository.findAll();
    }

    public Optional<BaustellenBesetzung> getBaustellenBesetzungByPersonalnummer(Integer personalnummer) {
        return repository.findById(personalnummer);
    }

    public String updateBaustellenBesetzung(BaustellenBesetzung baustellenBesetzung) {
        return repository.save(baustellenBesetzung).getPersonalnummer().toString();
    }

    public void deleteBaustellenBesetzungByPersonalnummer(Integer personalnummer) {
        repository.deleteById(personalnummer);
    }
}
