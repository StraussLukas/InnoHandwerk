package com.example.InnoHandwerk.InnoHandwerk.repository;



import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnhangRepositoryTest {

    @Autowired
    AnhangRepository repository;

    Anhang anhang1 = new Anhang();
    Anhang anhang2 = new Anhang();

    @BeforeAll
    void setUp() {

        anhang1.setDatei("path1");
        anhang1.setBeitragId(5);
        repository.saveAndFlush(anhang1);

        anhang2.setDatei("path2");
        anhang2.setBeitragId(4);
        repository.saveAndFlush(anhang2);
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
        var actual = repository.findById(anhang1.getId());
        // assert
        assertThat(actual).isNotNull();
        assertThat(actual.stream().findFirst().get().getDatei()).isEqualTo(anhang1.getDatei());
    }

    @Test
    void findAllByBeitragIdEquals_whenFound_ThenResultHasCertainFreitext(){
        // arrange
        int expectedSize = 1;
        // act
        var actual = repository.findAllByBeitragIdEquals(anhang1.getBeitragId());
        // assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(expectedSize);
        assertThat(actual.stream().findFirst().get().getDatei()).isEqualTo(anhang1.getDatei());
    }

    @Test
    void deleteById_WhenDeleted_thenResultHASSize1(){
        // arrange
        int expectedSize = 1;
        int id = anhang1.getId();
        // act
        repository.deleteById(anhang1.getId());
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
        assertThat(repository.findById(id).isEmpty());
    }
    @Test
    void deleteAllByBeitragIdIdEquals_WhenDeleted_thenResultHASSize1() {
        // arrange
        int expectedSize = 1;
        int id = anhang1.getId();
        // act
        repository.deleteAllByBeitragIdEquals(anhang1.getBeitragId());
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
        Anhang anhang3 = new Anhang();
        anhang3.setId(-3);
        anhang3.setDatei("text3");
        anhang3.setBeitragId(5);
        repository.save(anhang3);
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
        assertThat(repository.findById(anhang3.getId()).isPresent());
    }

    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }
}
