package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaustelleRepository extends JpaRepository<Baustelle, Integer> {
}