package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import com.example.InnoHandwerk.InnoHandwerk.entity.Mitarbeiter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
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

    @Autowired
    private  BaustelleService baustelleService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    private final Baustellenbesetzung besetzung1 = new Baustellenbesetzung();
    private final Baustellenbesetzung besetzung2 = new Baustellenbesetzung();
    private final Baustellenbesetzung besetzung3 = new Baustellenbesetzung();

    private final Baustelle baustelle = new Baustelle();
    private final Baustelle baustelle2 = new Baustelle();


    private final Mitarbeiter mitarbeiter1001 = new Mitarbeiter();
    private final Mitarbeiter mitarbeiter1002 = new Mitarbeiter();

    @BeforeAll
    void setUp() {
        mitarbeiter1001.setPersonalnummer(1001);
        mitarbeiter1001.setVorname("Max");
        mitarbeiter1001.setNachname("Meier");
        mitarbeiter1001.setEmail("maxmeier@email.com");
        mitarbeiter1001.setAdmin(true);

        mitarbeiter1002.setPersonalnummer(1002);
        mitarbeiter1002.setVorname("Maria");
        mitarbeiter1002.setNachname("Meier");
        mitarbeiter1002.setEmail("mariameier@email.com");
        mitarbeiter1002.setAdmin(false);

        mitarbeiterService.addMitarbeiter(mitarbeiter1001);
        mitarbeiterService.addMitarbeiter(mitarbeiter1002);

        baustelle.setTitel("Baustelle 1");
        baustelle.setName_bauherr("Bauherr1");
        baustelle.setAdresse("Adresse1");
        baustelle.setStatus("Erstellt");
        baustelle.setTelefon("123456789");
        baustelle.setEmail("bauherr1@example.com");
        baustelle.setArbeitsaufwand(10);


        baustelleService.addBaustelle(baustelle);

        baustelle2.setTitel("Baustelle 1");
        baustelle2.setName_bauherr("Bauherr1");
        baustelle2.setAdresse("Adresse1");
        baustelle2.setStatus("Erstellt");
        baustelle2.setTelefon("123456789");
        baustelle2.setEmail("bauherr1@example.com");
        baustelle2.setArbeitsaufwand(10);


        baustelleService.addBaustelle(baustelle2);


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
        List<Baustellenbesetzung> actualEntity = service.getBaustellenBesetzungByBaustellenId(6);
        // assert
        assertThat(actualEntity).isNotEmpty();
        assertEquals(5, actualEntity.get(0).getPersonalnummer());
    }

    @Order(5)
    @Test
    void getBaustellenBesetzungByPersonalnummer_whenEntityNotExists_thenReturnThrowException() {
        // assert
        assertThat(service.getBaustellenBesetzungByBaustellenId(-9999)).isEmpty();
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
        besetzung1.setBaustellenId(6);
        // actual
        var actualId = service.updateBaustellenBesetzung(besetzung1);
        var actualEntities = service.getBaustellenBesetzungByBaustellenId(7);
        // assert
        assertEquals(5, actualId);
        assertThat(actualEntities).isNotNull();
        assertEquals(6, actualEntities.get(0).getBaustellenId());
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
            "DELETE FROM baustellenbesetzung WHERE id = '5'",
            "DELETE FROM baustellenbesetzung WHERE id = '6'",
            "DELETE FROM baustellenbesetzung WHERE id = '7'",
            "DELETE FROM baustelle WHERE id = '5'",
            "DELETE FROM baustelle WHERE id = '6'",
            "DELETE FROM mitarbeiter WHERE personalnummer = '1001'",
            "DELETE FROM mitarbeiter WHERE personalnummer = '1002'",
            "ALTER SEQUENCE baustellenbesetzung_id_seq RESTART",
            "ALTER SEQUENCE baustelle_id_seq RESTART"

    })
    void getAllBaustellenBesetzung_checkNumberOfEntitiesAfterDeletingTestData_mustBe1() {
        // actual
        var actualEntities = service.getAllBaustellenBesetzung();
        // assert
        assertEquals(4, actualEntities.size());
    }
}
