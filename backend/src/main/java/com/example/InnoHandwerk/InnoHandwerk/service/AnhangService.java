package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import com.example.InnoHandwerk.InnoHandwerk.repository.AnhangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnhangService {
    @Autowired
    AnhangRepository repository;

    public Optional<Anhang> getAnhangById(Integer id) {
        return repository.findById(id);
    }
    public List<Anhang> getAllAnhaenge() {
        return repository.findAll();
    }

   public List<Anhang> getAllAnhaengeByBeitragId(Integer beitragId) {
       return repository.findAllByBeitragIdEquals(beitragId);
    }

    public void deleteAnhangById(Integer id) {
        repository.deleteById(id);
    }

    public void deleteAllAnhaengeByBeitragId(Integer beitragId){
        repository.deleteAllByBeitragIdEquals(beitragId);
    }

    public Integer addAnhang(Anhang anhang) {
        return repository.save(anhang).getId();
    }

    public Integer updateAnhang(Anhang anhang) {
        return repository.save(anhang).getId();
    }

    public String addAnhangRefactored(Integer beitragId) {

        Integer newId = repository.findTopByOrderByIdDesc().getId() + 1;
        String datei = "../db/files/pic" + newId + ".png";

        Anhang anhang = new Anhang(newId, datei, beitragId);

        repository.save(anhang);

        return datei;
    }
}