package com.example.InnoHandwerk.InnoHandwerk.repository;


import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnhangRepository extends JpaRepository<Anhang, Integer> {
    List<Anhang> findAllByBeitragIdEquals(int beitragId);
    void deleteAllByBeitragIdEquals(Integer id);
    Anhang findTopByOrderByIdDesc();
}