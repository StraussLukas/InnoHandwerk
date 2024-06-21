package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import com.example.InnoHandwerk.InnoHandwerk.repository.BaustellenBesetzungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaustellenBesetzungService {

    @Autowired
    BaustellenBesetzungRepository repository;

    public String addBaustellenBesetzung(Baustellenbesetzung baustellenBesetzung) {
        return repository.save(baustellenBesetzung).getPersonalnummer().toString();
    }

    public List<Baustellenbesetzung> getAllBaustellenBesetzung() {
        return repository.findAll();
    }

    public List<Baustellenbesetzung> getBaustellenBesetzungByBaustellenId(Integer baustellenId) {
        return repository.findByBaustellenId(baustellenId);
    }

    public Integer updateBaustellenBesetzung(Baustellenbesetzung baustellenBesetzung) {
        return repository.save(baustellenBesetzung).getId();
    }

    public List<Integer> getAllBaustellenIdsByPersonalnummer(Integer personalnummer){
        return repository.findDistinctBaustellenIdsByPersonalnummer(personalnummer);
    }

    public void deleteBaustellenBesetzungById(Integer id) {
        repository.deleteById(id);
    }
}
