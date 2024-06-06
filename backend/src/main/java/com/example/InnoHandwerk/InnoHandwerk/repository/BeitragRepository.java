package com.example.InnoHandwerk.InnoHandwerk.repository;


import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeitragRepository extends JpaRepository<Beitrag, Integer> {
    List<Beitrag> findAllByBaustelleIdEquals(Integer id);
    void deleteAllByBaustelleIdEquals(Integer id);
}