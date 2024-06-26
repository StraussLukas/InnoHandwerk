package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BaustellenBesetzungRepository extends JpaRepository<Baustellenbesetzung, Integer> {

    List<Baustellenbesetzung> findByBaustellenIdAndDatum(Integer baustellenId, Date datum);
    @Query(value = "SELECT DISTINCT b.baustellen_id FROM baustellenbesetzung b WHERE b.personalnummer = :personalnummer", nativeQuery = true)
    List<Integer> findDistinctBaustellenIdsByPersonalnummer(Integer personalnummer);

}
