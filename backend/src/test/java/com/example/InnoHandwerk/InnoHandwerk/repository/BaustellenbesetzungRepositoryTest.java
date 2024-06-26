package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.awt.image.BufferStrategy;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaustellenbesetzungRepositoryTest {

    @Autowired
    BaustellenBesetzungRepository repository;

    Baustellenbesetzung besetzung1 = new Baustellenbesetzung();
    Baustellenbesetzung besetzung2 = new Baustellenbesetzung();

    Baustellenbesetzung besetzung3 = new Baustellenbesetzung();


    @BeforeAll
    void setUp(){
        besetzung1.setPersonalnummer(1001);
        besetzung1.setBaustellenId(1);
        besetzung1.setDatum(Date.valueOf(LocalDate.of(2024, 6, 24)));
        besetzung1.setUhrzeitVon(Time.valueOf("08:00:00")); // Format angepasst
        besetzung1.setUhrzeitBis(Time.valueOf("16:00:00")); // Format angepasst
        repository.saveAndFlush(besetzung1);

        besetzung2.setPersonalnummer(1002);
        besetzung2.setBaustellenId(2);
        besetzung2.setDatum(Date.valueOf(LocalDate.of(2024, 6, 24)));
        besetzung2.setUhrzeitVon(Time.valueOf("09:00:00")); // Format angepasst
        besetzung2.setUhrzeitBis(Time.valueOf("17:00:00")); // Format angepasst
        repository.saveAndFlush(besetzung2);

        besetzung3.setPersonalnummer(1001);
        besetzung3.setBaustellenId(2);
        besetzung3.setDatum(Date.valueOf(LocalDate.of(2024, 6, 24)));
        besetzung3.setUhrzeitVon(Time.valueOf("08:00:00")); // Format angepasst
        besetzung3.setUhrzeitBis(Time.valueOf("16:00:00")); // Format angepasst
        repository.saveAndFlush(besetzung3);
    }

    @Test
    void smokeTest(){ assertThat(repository).isNotNull(); }

    @Test
    void findAll_whenFindAll_thenResultHasSize2(){
        // arrange
        var expectedSize = 3;
        // act
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    void findByID_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findById(5);
        // assert
        assertThat(actual.get().getPersonalnummer()).isEqualTo(1001);
        assertThat(actual.get().getBaustellenId()).isEqualTo(1);
        assertThat(actual.get().getDatum()).isEqualTo(Date.valueOf(LocalDate.of(2024, 6, 24)));
        assertThat(actual.get().getUhrzeitVon()).isEqualTo(Time.valueOf("08:00:00")); // Format angepasst
        assertThat(actual.get().getUhrzeitBis()).isEqualTo(Time.valueOf("16:00:00")); // Format angepasst
    }

    @Test
    void update_whenSuccesfull_thenEntityIsNowUpdated(){
        // arrange
        var updatedBesetzung = new Baustellenbesetzung();
        updatedBesetzung.setId(5);
        updatedBesetzung.setPersonalnummer(1001);
        updatedBesetzung.setBaustellenId(3);
        updatedBesetzung.setDatum(Date.valueOf(LocalDate.of(2024, 6, 24)));
        updatedBesetzung.setUhrzeitVon(Time.valueOf("08:00:00")); // Format angepasst
        updatedBesetzung.setUhrzeitBis(Time.valueOf("17:00:00")); // Format angepasst
        // act
        repository.save(updatedBesetzung);
        var actual = repository.findById(5);
        // assert
        assertThat(actual.get().getPersonalnummer()).isEqualTo(1001);
        assertThat(actual.get().getBaustellenId()).isEqualTo(3);
        assertThat(actual.get().getDatum()).isEqualTo(Date.valueOf(LocalDate.of(2024, 6, 24)));
        assertThat(actual.get().getUhrzeitVon()).isEqualTo(Time.valueOf("08:00:00")); // Format angepasst
        assertThat(actual.get().getUhrzeitBis()).isEqualTo(Time.valueOf("17:00:00")); // Format angepasst
    }

    @Test
    void getAllByPersonalnummer_thenReturnList(){
        List<Baustellenbesetzung> baustellenbesetzung = repository.findByBaustellenIdAndDatum(1, Date.valueOf(LocalDate.of(2024, 6, 24)));
        assertThat(baustellenbesetzung.get(0).getPersonalnummer()).isEqualTo(1001);
        assertThat(baustellenbesetzung.get(0).getDatum()).isEqualTo(Date.valueOf(LocalDate.of(2024, 6, 24)));

    }



    @AfterAll
    void tearDown(){
        repository.deleteAll();
    }
}
