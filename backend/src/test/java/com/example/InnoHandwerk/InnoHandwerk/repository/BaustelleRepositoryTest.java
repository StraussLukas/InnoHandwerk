package com.example.InnoHandwerk.InnoHandwerk.repository;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaustelleRepositoryTest {

    @Autowired
    BaustelleRepository repository;

    Baustelle baustelle1 = new Baustelle();
    Baustelle baustelle2 = new Baustelle();

    @BeforeAll
    void setUp(){
        baustelle1.setId(1);
        baustelle1.setName_bauherr("Bauherr1");
        baustelle1.setAdresse("Adresse1");
        baustelle1.setTelefon("123456789");
        baustelle1.setEmail("bauherr1@example.com");
        baustelle1.setArbeitsaufwand(10);
        repository.saveAndFlush(baustelle1);

        baustelle2.setId(2);
        baustelle2.setName_bauherr("Bauherr2");
        baustelle2.setAdresse("Adresse2");
        baustelle2.setTelefon("987654321");
        baustelle2.setEmail("bauherr2@example.com");
        baustelle2.setArbeitsaufwand(20);
        repository.saveAndFlush(baustelle2);
    }

    @Test
    void smokeTest(){ assertThat(repository).isNotNull(); }

    @Test
    void findAll_whenFindAll_thenResultHasSize2(){
        // arrange
        var expectedSize = 2;
        // act
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    void findByID_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findById(1);
        // assert
        assertThat(actual.get().getName_bauherr()).isEqualTo("Bauherr1");
        assertThat(actual.get().getAdresse()).isEqualTo("Adresse1");
        assertThat(actual.get().getTelefon()).isEqualTo("123456789");
        assertThat(actual.get().getEmail()).isEqualTo("bauherr1@example.com");
        assertThat(actual.get().getArbeitsaufwand()).isEqualTo(10);
    }

    @Test
    void update_whenSuccesfull_thenEntityIsNowUpdated(){
        // arrange
        var updatedBaustelle = new Baustelle();
        updatedBaustelle.setId(1);
        updatedBaustelle.setName_bauherr("UpdatedBauherr");
        updatedBaustelle.setAdresse("UpdatedAdresse");
        updatedBaustelle.setTelefon("987654321");
        updatedBaustelle.setEmail("updatedbauherr@example.com");
        updatedBaustelle.setArbeitsaufwand(30);
        // act
        repository.save(updatedBaustelle);
        var actual = repository.findById(1);
        // assert
        assertThat(actual.get().getName_bauherr()).isEqualTo("UpdatedBauherr");
        assertThat(actual.get().getAdresse()).isEqualTo("UpdatedAdresse");
        assertThat(actual.get().getTelefon()).isEqualTo("987654321");
        assertThat(actual.get().getEmail()).isEqualTo("updatedbauherr@example.com");
        assertThat(actual.get().getArbeitsaufwand()).isEqualTo(30);
    }

    @AfterAll
    void tearDown(){
        repository.deleteAll();
    }
}
