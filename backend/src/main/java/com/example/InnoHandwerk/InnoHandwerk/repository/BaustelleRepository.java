package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaustelleRepository extends JpaRepository<Baustelle, Integer> {
    List<Baustelle> findAllByStatusOrderByZeitstempelAsc(String status);
}