package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaustellenBesetzungRepository extends JpaRepository<Baustellenbesetzung, Integer> {

    List<Baustellenbesetzung> getDistinctByPersonalnummer(Integer personalnummer);
    @Query(value = "SELECT DISTINCT b.baustellen_id FROM baustellenbesetzung b WHERE b.personalnummer = :personalnummer", nativeQuery = true)
    List<Integer> findDistinctBaustellenIdsByPersonalnummer(Integer personalnummer);
}
