package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Time;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationstest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaustellenbesetzungServiceTest {

    @Autowired
    private BaustellenBesetzungService service;

    private final Baustellenbesetzung besetzung1 = new Baustellenbesetzung();
    private final Baustellenbesetzung besetzung2 = new Baustellenbesetzung();
    private final Baustellenbesetzung besetzung3 = new Baustellenbesetzung();


    @BeforeAll
    void setUp() {
        besetzung1.setPersonalnummer(1001);
        besetzung1.setBaustellenId(5);
        besetzung1.setDatum(20230530.0);
        besetzung1.setUhrzeitVon(Time.valueOf("08:00:00"));
        besetzung1.setUhrzeitBis(Time.valueOf("16:00:00"));

        besetzung2.setPersonalnummer(1002);
        besetzung2.setBaustellenId(6);
        besetzung2.setDatum(20230530.0);
        besetzung2.setUhrzeitVon(Time.valueOf("09:00:00"));
        besetzung2.setUhrzeitBis(Time.valueOf("17:00:00"));

        besetzung3.setPersonalnummer(1001);
        besetzung3.setBaustellenId(6);
        besetzung3.setDatum(20230530.0);
        besetzung3.setUhrzeitVon(Time.valueOf("08:00:00"));
        besetzung3.setUhrzeitBis(Time.valueOf("16:00:00"));
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllBaustellenBesetzung_checkNumberOfEntitiesBeforeAddingTestData_mustBe4() {
        // actual
        var actualEntities = service.getAllBaustellenBesetzung();
        // assert
        assertEquals(4, actualEntities.size());
    }

    @Order(2)
    @Test
    void addBaustellenBesetzung_whenValidModel_thenReturnEntityId() {
        // actual
        var actualId1 = service.addBaustellenBesetzung(besetzung1);
        var actualId2 = service.addBaustellenBesetzung(besetzung2);
        var actualId3 = service.addBaustellenBesetzung(besetzung3);

        // assert
        assertEquals("1001", actualId1);
        assertEquals("1002", actualId2);
        assertEquals("1001", actualId3);

    }

    @Order(3)
    @Test
    void getAllBaustellenBesetzung_checkNumberOfEntitiesAfterAddingTestData_mustBe6() {
        // actual
        var actualEntities = service.getAllBaustellenBesetzung();
        // assert
        assertEquals(7, actualEntities.size());
    }

    @Order(4)
    @Test
    void getBaustellenBesetzungByPersonalnummer_whenEntityExists_thenReturnEntity() {
        // actual
        Optional<Baustellenbesetzung> actualEntity = service.getBaustellenBesetzungById(5);
        // assert
        assertThat(actualEntity).isPresent();
        assertEquals(5, actualEntity.get().getBaustellenId());
    }

    @Order(5)
    @Test
    void getBaustellenBesetzungByPersonalnummer_whenEntityNotExists_thenReturnThrowException() {
        // assert
        assertThat(service.getBaustellenBesetzungById(9999)).isNotPresent();
    }

    @Order(6)
    @Test
    void getALlByBaustellenIdsByPersonalnummer() {
        // actual
        var actualEntities = service.getAllBaustellenIdsByPersonalnummer(1001);
        // assert
        assertEquals(2, actualEntities.size());
    }

    @Order(7)
    @Test
    void updateBaustellenBesetzung_whenValidModel_thenReturnEntityId() {
        // arrange
        besetzung1.setBaustellenId(7);
        // actual
        var actualId = service.updateBaustellenBesetzung(besetzung1);
        var actualEntity = service.getBaustellenBesetzungById(5).orElse(null);
        // assert
        assertEquals(5, actualId);
        assertThat(actualEntity).isNotNull();
        assertEquals(7, actualEntity.getBaustellenId());
    }

    @Order(8)
    @Test
    void deleteBaustellenBesetzungByPersonalnummer_whenSuccessful_thenSizeMustBe5() {
        // actual
        service.deleteBaustellenBesetzungById(5);
        var actualEntities = service.getAllBaustellenBesetzung();
        // assert
        assertEquals(6, actualEntities.size());
    }

    @Order(9)
    @Test
    @Sql(statements = {
            "DELETE FROM baustellenbesetzung WHERE id = 5",
            "DELETE FROM baustellenbesetzung WHERE id = 6",
            "DELETE FROM baustellenbesetzung WHERE id = 7",
            "ALTER SEQUENCE baustellenbesetzung_id_seq RESTART"
    })
    void getAllBaustellenBesetzung_checkNumberOfEntitiesAfterDeletingTestData_mustBe1() {
        // actual
        var actualEntities = service.getAllBaustellenBesetzung();
        // assert
        assertEquals(4, actualEntities.size());
    }
}
