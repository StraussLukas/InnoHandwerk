package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaustellenBesetzungRepository extends JpaRepository<Baustellenbesetzung, Integer> {

    List<Baustellenbesetzung> findByBaustellenId(Integer baustellenId);
    @Query(value = "SELECT DISTINCT b.baustellen_id FROM baustellenbesetzung b WHERE b.personalnummer = :personalnummer", nativeQuery = true)
    List<Integer> findDistinctBaustellenIdsByPersonalnummer(Integer personalnummer);

}
