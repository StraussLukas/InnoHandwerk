package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationstest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaustelleServiceTest {

    @Autowired
    private BaustelleService service;

    @Autowired
    private BaustellenBesetzungService baustellenBesetzungService;

    private final Baustelle baustelle1 = new Baustelle();
    private final Baustelle baustelle2 = new Baustelle();
    private final Baustellenbesetzung baustellenbesetzung1 = new Baustellenbesetzung();
    private final Baustellenbesetzung baustellenbesetzung2 = new Baustellenbesetzung();



    @BeforeAll
    void setUp() {
        baustelle1.setTitel("Baustelle 1");
        baustelle1.setName_bauherr("Bauherr1");
        baustelle1.setAdresse("Adresse1");
        baustelle1.setStatus("Erstellt");
        baustelle1.setTelefon("123456789");
        baustelle1.setEmail("bauherr1@example.com");
        baustelle1.setArbeitsaufwand(10);

        baustelle2.setTitel("Baustelle 2");
        baustelle2.setName_bauherr("Bauherr2");
        baustelle2.setAdresse("Adresse2");
        baustelle2.setStatus("In Arbeit");
        baustelle2.setTelefon("987654321");
        baustelle2.setEmail("bauherr2@example.com");
        baustelle2.setArbeitsaufwand(20);

        baustellenbesetzung1.setPersonalnummer(500);
        baustellenbesetzung1.setBaustellenId(5);
        baustellenbesetzung1.setDatum(Date.valueOf(LocalDate.of(2024, 6, 24)));
        baustellenbesetzung1.setUhrzeitVon(Time.valueOf("08:00:00"));
        baustellenbesetzung1.setUhrzeitBis(Time.valueOf("16:00:00"));

        baustellenbesetzung2.setPersonalnummer(500);
        baustellenbesetzung2.setBaustellenId(6);
        baustellenbesetzung2.setDatum(Date.valueOf(LocalDate.of(2024, 6, 24)));
        baustellenbesetzung2.setUhrzeitVon(Time.valueOf("08:00:00"));
        baustellenbesetzung2.setUhrzeitBis(Time.valueOf("16:00:00"));
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
        assertEquals(5, actualId1);
        assertEquals(6, actualId2);
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
    void getAllBaustellenByPersonalnummer_thenReturnEntities() {
        baustellenBesetzungService.addBaustellenBesetzung(baustellenbesetzung1);
        baustellenBesetzungService.addBaustellenBesetzung(baustellenbesetzung2);

        // actual
        var actualEntity = service.getAllBaustellenByPersonalnummer(500);
        // assert
        assertThat(actualEntity).hasSize(2);
        assertThat(actualEntity.get(0).getId()).isEqualTo(5);
        assertThat(actualEntity.get(1).getId()).isEqualTo(6);

    }

    @Order(5)
    @Test
    void getAllBaustellenByStatus_thenReturnEntities() {
             // actual
        var actualEntities = service.getAllBaustellenByStatus("Erstellt");
        // assert
        assertThat(actualEntities).hasSize(2);
        assertThat(actualEntities.get(0).getId()).isEqualTo(1);

    }


    @Order(7)
    @Test
    void getBaustelleById_whenEntityNotExists_thenReturnThrowException() {
        // assert
        assertThat(service.getBaustelleById(100)).isNotPresent();
    }

    @Order(8)
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

    @Order(9)
    @Test
    void getBaustellenByPersonalnummer_whenEntityExists_thenReturnList() {
        // actual
        List<Baustelle> actualEntities = service.getAllBaustellenByPersonalnummer(100);
        // assert
        assertThat(actualEntities).isNotEmpty();
        assertEquals(2, actualEntities.size());
        assertThat(actualEntities.get(0).getId()).isEqualTo(1);
        assertThat(actualEntities.get(1).getId()).isEqualTo(2);

    }



    @Order(10)
    @Test
    @Sql(statements = {
            "DELETE FROM baustellenbesetzung WHERE id = 5",
            "DELETE FROM baustellenbesetzung WHERE id = 6",
            "ALTER SEQUENCE baustellenbesetzung_id_seq RESTART"


    })
    void deleteBaustelleById_whenSuccessful_thenSizeMustBe5() {
        // actual
        service.deleteBaustelleById(5);
        var actualEntities = service.getAllBaustelle();
        // assert
        assertEquals(5, actualEntities.size());
    }



    @Order(11)
    @Test
    @Sql(statements = {
            "DELETE FROM baustelle WHERE id = 5",
            "DELETE FROM baustelle WHERE id = 6",
            "ALTER SEQUENCE baustelle_id_seq RESTART"

    })
    void getAllBaustelle_checkNumberOfEntitiesAfterDeletingTestData_mustBe4() {
        // actual
        var actualEntities = service.getAllBaustelle();
        // assert
        assertEquals(4, actualEntities.size());
    }
}
