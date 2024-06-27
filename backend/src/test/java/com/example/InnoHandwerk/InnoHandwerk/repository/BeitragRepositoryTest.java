package com.example.InnoHandwerk.InnoHandwerk.repository;



import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeitragRepositoryTest {

    @Autowired
    BeitragRepository repository;

    Beitrag beitrag1 = new Beitrag();
    Beitrag beitrag2 = new Beitrag();

    @BeforeAll
    void setUp() {

        beitrag1.setFreitext("text1");
       // beitrag1.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));
        beitrag1.setBaustelleId(-10);
        beitrag1.setPersonalnummer(-10);
        repository.saveAndFlush(beitrag1);


        beitrag2.setFreitext("text2");
       // beitrag2.setZeitstempel(Timestamp.valueOf("2024-07-14 14:30:00"));
        beitrag2.setBaustelleId(-20);
        beitrag2.setPersonalnummer(-10);
        repository.saveAndFlush(beitrag2);
    }

    @Test
    void smokeTest(){ assertThat(repository).isNotNull();}

    @Test
    void findAll_whenFindAll_thenResultHASSize2(){
        // arrange
        int expectedSize = 2;
        // act
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    void findById_whenFindById_thenResultHasCertainFreitext(){
        // act
        var actual = repository.findById(beitrag1.getId());
        // assert
        assertThat(actual).isNotNull();
        assertThat(actual.stream().findFirst().get().getFreitext()).isEqualTo(beitrag1.getFreitext());
    }

    @Test
    void findAllByBaustelleIdEquals_whenFound_ThenResultHasCertainFreitext(){
        // arrange
        int expectedSize = 1;
        // act
        var actual = repository.findAllByBaustelleIdEquals(beitrag1.getBaustelleId());
        // assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(expectedSize);
        assertThat(actual.stream().findFirst().get().getFreitext()).isEqualTo(beitrag1.getFreitext());
    }

    @Test
    void deleteById_WhenDeleted_thenResultHASSize1(){
        // arrange
        int expectedSize = 1;
        int id = beitrag1.getId();
        // act
        repository.deleteById(beitrag1.getId());
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
        assertThat(repository.findById(id).isEmpty());
    }
    @Test
    void deleteAllByBaustelleIdEquals_WhenDeleted_thenResultHASSize0() {
        // arrange
        int expectedSize = 1;
        int id = beitrag1.getId();
        // act
        repository.deleteAllByBaustelleIdEquals(beitrag1.getBaustelleId());
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
        assertThat(repository.findById(id).isEmpty());
    }

    @Test
    void save_whenSave_thenResultHASSize3(){
        // arrange
        int expectedSize = 3;
        // act
        Beitrag beitrag3 = new Beitrag();
        beitrag3.setFreitext("text3");
        beitrag3.setZeitstempel(LocalDateTime.of(2024,6,21,21,14,45));
        beitrag3.setBaustelleId(-30);
        beitrag3.setPersonalnummer(-10);
        repository.save(beitrag3);
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
        assertThat(repository.findById(beitrag3.getId()).isPresent());
    }

    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }
}
