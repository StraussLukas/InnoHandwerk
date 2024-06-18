package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import com.example.InnoHandwerk.InnoHandwerk.repository.BeitragRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeitragService {
    @Autowired
    BeitragRepository repository;

    public Optional<Beitrag> getBeitragById(Integer id) {
        return repository.findById(id);
    }
    public List<Beitrag> getAllBeitraege() {
        return repository.findAll();
    }

    public List<Beitrag> getAllBeitraegeByBaustellenId(Integer baustellenId) {
           return repository.findAllByBaustelleIdEquals(baustellenId);
    }

    public void deleteBeitragById(Integer id) {
        repository.deleteById(id);
    }

    public void deleteAllBeitragByBaustellenId(Integer baustellenId) {
        repository.deleteAllByBaustelleIdEquals(baustellenId);
    }

    public Integer addBeitrag(Beitrag beitrag) {
        return repository.save(beitrag).getId();
    }

    public Integer updateBeitrag(Beitrag beitrag) {
        return repository.save(beitrag).getId();
    }
}