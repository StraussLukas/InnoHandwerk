package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import com.example.InnoHandwerk.InnoHandwerk.repository.BaustelleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationstest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeitragServiceTest {

    @Autowired
    private BeitragService beitragService;
    @Autowired
    private  BaustelleService baustelleService;

    private final Beitrag beitrag1 = new Beitrag();
    private final Beitrag beitrag2 = new Beitrag();
    private final Baustelle baustelle = new Baustelle();

    @BeforeAll
    void setUp() {
        baustelle.setId(-1);
        baustelle.setTitel("Baustelle 1");
        baustelle.setName_bauherr("Bauherr1");
        baustelle.setAdresse("Adresse1");
        baustelle.setStatus("Erstellt");
        baustelle.setTelefon("123456789");
        baustelle.setEmail("bauherr1@example.com");
        baustelle.setArbeitsaufwand(10);
        baustelle.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));


        baustelleService.addBaustelle(baustelle);


        beitrag1.setFreitext("text1");
        beitrag1.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));
        beitrag1.setBaustelleId(-1);
        beitrag1.setPersonalnummer(100);


        beitrag2.setFreitext("text2");
        beitrag2.setZeitstempel(Timestamp.valueOf("2024-07-14 14:30:00"));
        beitrag2.setBaustelleId(-1);
        beitrag2.setPersonalnummer(100);
    }

    @Test
    void smokeTest(){
        assertThat(beitragService).isNotNull();
    }

    @Order(1)
    @Test
    void getAllBeitraege_checkNumberOfEntitiesBeforeAddingTestData_mustBe5() {
        //arrange
        var expectedEntities = 5;
        //actual
        var actualEntities = beitragService.getAllBeitraege();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addBeitrag_whenValidModel_thenReturnEntityId(){
        //actual
        var actualId1 = beitragService.addBeitrag(beitrag1);
        var actualId2 = beitragService.addBeitrag(beitrag2);
        //assert
        assertThat(actualId1).isEqualTo(6);
        assertThat(actualId2).isEqualTo(7);
    }

    @Order(3)
    @Test
    void getAllBeitraege_checkNumberOfEntitiesBeforeAddingTestData_mustBe7() {
        //arrange
        var expectedEntities = 7;
        //actual
        var actualEntities = beitragService.getAllBeitraege();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getBeitragById_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = beitragService.getBeitragById(6);
        //assert
        assertThat(actualEntity).isPresent();
        assertEquals(-1, actualEntity.get().getBaustelleId());
    }

    @Order(5)
    @Test
    void getBeitragById_whenEntityNotExists_thenReturnThrowException() {
        //assert
        assertThat(beitragService.getBeitragById(-4)).isNotPresent();
    }

    @Order(6)
    @Test
    void updateBeitrag_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedBeitrag = new Beitrag();
        updatedBeitrag.setId(6);
        updatedBeitrag.setFreitext("text3");
        updatedBeitrag.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));
        updatedBeitrag.setBaustelleId(-1);
        updatedBeitrag.setPersonalnummer(100);
        //actual
        var actualId = beitragService.updateBeitrag(updatedBeitrag);
        var actualEntity = beitragService.getBeitragById(actualId);
        //assert
        assertThat(actualId).isEqualTo(6);
        assertThat(actualEntity).isPresent();
        assertThat(actualEntity.get().getFreitext()).isEqualTo("text3");
    }

    @Order(7)
    @Test
    void getBeitraegeByBaustellenId_whenFound_thenReturnListofBeitraege() {
        //actual
        var actual = beitragService.getAllBeitraegeByBaustellenId(2);
        //assert
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);
    }

    @Order(8)
    @Test
    void getBeitraegeByBaustellenId_whenFound_thenReturnEmptyListofBeitraege() {
        //actual
        var actual = beitragService.getAllBeitraegeByBaustellenId(-100);
        //assert
        assertThat(actual).isEmpty();
    }

    @Order(9)
    @Test
    void deleteBeitragById_whenExcuted_thenCertainBeitragNull() {
        // act
        beitragService.deleteBeitragById(6);
        // assert
        assertThat(beitragService.getBeitragById(6)).isNotPresent();
    }

    @Order(10)
    @Test
    @Transactional
    void deleteBeitraegeByBaustellenId_whenExcuted_thenNumberOfEntitiesmustBe5() {
        //arrange
        var expectedEntities = 5;
        // act
        beitragService.deleteAllBeitragByBaustellenId(-1);
        var actualEntities = beitragService.getAllBeitraege();
        // assert
        assertThat(beitragService.getAllBeitraegeByBaustellenId(-1)).isEmpty();
        assertEquals(expectedEntities, actualEntities.size());

    }

    @Order(11)
    @Test
    @Sql(statements = {
            "DELETE FROM beitrag WHERE id = '6'",
            "DELETE FROM beitrag WHERE id = '7'",
            "DELETE FROM baustelle WHERE id = '-1'",
            "ALTER SEQUENCE beitrags_id_seq RESTART"
    })
    void getAllBeitraege_checkNumberOfEntitiesAfterDeletingTestData_must5() {
        //arrange
        var expectedEntities = 5;
        //actual
        var actualEntities = beitragService.getAllBeitraege();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }
}
