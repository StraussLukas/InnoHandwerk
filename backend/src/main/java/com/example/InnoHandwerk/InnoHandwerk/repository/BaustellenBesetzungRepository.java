package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.BaustellenBesetzung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaustellenBesetzungRepository extends JpaRepository<BaustellenBesetzung, Integer> {
}
