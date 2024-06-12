package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaustelleServiceTest {

    @Autowired
    private BaustelleService service;

    private final Baustelle baustelle1 = new Baustelle();
    private final Baustelle baustelle2 = new Baustelle();

    @BeforeAll
    void setUp() {
        baustelle1.setId(5);
        baustelle1.setTitel("Baustelle 1");
        baustelle1.setName_bauherr("Bauherr1");
        baustelle1.setAdresse("Adresse1");
        baustelle1.setStatus("Erstellt");
        baustelle1.setTelefon("123456789");
        baustelle1.setEmail("bauherr1@example.com");
        baustelle1.setArbeitsaufwand(10);
        baustelle1.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));

        baustelle2.setId(6);
        baustelle2.setTitel("Baustelle 2");
        baustelle2.setName_bauherr("Bauherr2");
        baustelle2.setAdresse("Adresse2");
        baustelle2.setStatus("In Arbeit");
        baustelle2.setTelefon("987654321");
        baustelle2.setEmail("bauherr2@example.com");
        baustelle2.setArbeitsaufwand(20);
        baustelle2.setZeitstempel(Timestamp.valueOf("2024-03-21 10:15:45"));
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllBaustelle_checkNumberOfEntitiesBeforeAddingTestData_mustBe4() {
        // actual
        var actualEntities = service.getAllBaustelle();
        // assert
        assertEquals(4, actualEntities.size());
    }

    @Order(2)
    @Test
    void addBaustelle_whenValidModel_thenReturnEntityId() {
        // actual
        var actualId1 = service.addBaustelle(baustelle1);
        var actualId2 = service.addBaustelle(baustelle2);
        // assert
        assertEquals("5", actualId1);
        assertEquals("6", actualId2);
    }

    @Order(3)
    @Test
    void getAllBaustelle_checkNumberOfEntitiesAfterAddingTestData_mustBe2() {
        // actual
        var actualEntities = service.getAllBaustelle();
        // assert
        assertEquals(6, actualEntities.size());
    }

    @Order(4)
    @Test
    void getBaustelleById_whenEntityExists_thenReturnEntity() {
        // actual
        Optional<Baustelle> actualEntity = service.getBaustelleById(5);
        // assert
        assertThat(actualEntity).isPresent();
        assertEquals("Bauherr1", actualEntity.get().getName_bauherr());
    }

    @Order(5)
    @Test
    void getBaustelleById_whenEntityNotExists_thenReturnThrowException() {
        // assert
        assertThat(service.getBaustelleById(100)).isNotPresent();
    }

    @Order(6)
    @Test
    void updateBaustelle_whenValidModel_thenReturnEntityId() {
        // arrange
        baustelle1.setName_bauherr("UpdatedBauherr");
        // actual
        var actualId = service.updateBaustelle(baustelle1);
        var actualEntity = service.getBaustelleById(5).orElse(null);
        // assert
        assertEquals("5", actualId);
        assertThat(actualEntity).isNotNull();
        assertEquals("UpdatedBauherr", actualEntity.getName_bauherr());
    }

    @Order(7)
    @Test
    void deleteBaustelleById_whenSuccessful_thenSizeMustBe5() {
        // actual
        service.deleteBaustelleById(5);
        var actualEntities = service.getAllBaustelle();
        // assert
        assertEquals(5, actualEntities.size());
    }

    @Order(8)
    @Test
    @Sql(statements = {
            "DELETE FROM baustelle WHERE id = 6"
    })
    void getAllBaustelle_checkNumberOfEntitiesAfterDeletingTestData_mustBe4() {
        // actual
        var actualEntities = service.getAllBaustelle();
        // assert
        assertEquals(4, actualEntities.size());
    }
}
