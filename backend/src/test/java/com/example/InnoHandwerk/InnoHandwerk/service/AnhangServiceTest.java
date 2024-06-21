
package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import com.example.InnoHandwerk.InnoHandwerk.repository.AnhangRepository;
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
public class AnhangServiceTest {

    @Autowired
    private AnhangService anhangService;
    @Autowired
    private BeitragService beitragService;
    @Autowired
    private BaustelleService baustelleService;

    private final Anhang anhang1 = new Anhang();
    private final Anhang anhang2 = new Anhang();
    private final Beitrag beitrag = new Beitrag();
    private final Baustelle baustelle = new Baustelle();

    @BeforeAll
    void setUp() {

        baustelle.setId(5);
        baustelle.setTitel("Baustelle 2");
        baustelle.setName_bauherr("Bauherr2");
        baustelle.setAdresse("Adresse2");
        baustelle.setStatus("In Arbeit");
        baustelle.setTelefon("987654321");
        baustelle.setEmail("bauherr2@example.com");
        baustelle.setArbeitsaufwand(20);
        baustelle.setZeitstempel(Timestamp.valueOf("2024-03-21 10:15:45"));

        baustelleService.addBaustelle(baustelle);

        beitrag.setFreitext("text1");
        beitrag.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));
        beitrag.setBaustelleId(5);
        beitrag.setPersonalnummer(100);

        beitragService.addBeitrag(beitrag);


        anhang1.setDatei("path1");
        anhang1.setBeitragId(6);

        anhang2.setDatei("path2");
        anhang2.setBeitragId(6);


    }

    @Test
    void smokeTest(){
        assertThat(anhangService).isNotNull();
    }

    @Order(1)
    @Test
    void getAllAnhaenge_checkNumberOfEntitiesBeforeAddingTestData_mustBe5() {
        //arrange
        var expectedEntities = 5;
        //actual
        var actualEntities = anhangService.getAllAnhaenge();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addAnhang_whenValidModel_thenReturnEntityId(){
        //actual
        var actualId1 = anhangService.addAnhang(anhang1);
        var actualId2 = anhangService.addAnhang(anhang2);
        //assert
        assertThat(actualId1).isEqualTo(6);
        assertThat(actualId2).isEqualTo(7);
    }

    @Order(3)
    @Test
    void getAllAnhaenge_checkNumberOfEntitiesBeforeAddingTestData_mustBe7() {
        //arrange
        var expectedEntities = 7;
        //actual
        var actualEntities = anhangService.getAllAnhaenge();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getAnhangById_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = anhangService.getAnhangById(6);
        //assert
        assertThat(actualEntity).isPresent();
        assertEquals(6, actualEntity.get().getBeitragId());
    }

    @Order(5)
    @Test
    void getAnhangById_whenEntityNotExists_thenReturnThrowException() {
        //assert
        assertThat(anhangService.getAnhangById(-4)).isNotPresent();
    }

    @Order(6)
    @Test
    void updateAnhang_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedAnhang = new Anhang();
        updatedAnhang.setId(6);
        updatedAnhang.setDatei("path1");
        updatedAnhang.setBeitragId(4);
        //actual
        var actualId = anhangService.updateAnhang(updatedAnhang);
        var actualEntity = anhangService.getAnhangById(actualId);
        //assert
        assertThat(actualId).isEqualTo(6);
        assertThat(actualEntity).isPresent();
        assertThat(actualEntity.get().getBeitragId()).isEqualTo(4);
    }

    @Order(7)
    @Test
    void getAnahngByBeitragId_whenFound_thenReturnListofAnhaenge() {
        //actual
        var actual = anhangService.getAllAnhaengeByBeitragId(4);
        //assert
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(2);
    }

    @Order(8)
    @Test
    void getAnhangByBeitrageId_whenFound_thenReturnEmptyListofAnhaenge() {
        //actual
        var actual = anhangService.getAllAnhaengeByBeitragId(-100);
        //assert
        assertThat(actual).isEmpty();
    }

    @Order(9)
    @Test
    void deleteAnhangById_whenExcuted_thenCertainAnhangNull() {
        // act
        anhangService.deleteAnhangById(6);

        // assert
        assertThat(anhangService.getAnhangById(6)).isNotPresent();
    }

    @Order(10)
    @Test
    @Transactional
    void deleteAnhangByBaustellenId_whenExcuted_thenNumberOfEntitiesmustBe5() {
        //arrange
        var expectedEntities = 5;
        // act
        anhangService.deleteAllAnhaengeByBeitragId(6);
        var actualEntities = anhangService.getAllAnhaenge();
        // assert
        assertThat(anhangService.getAllAnhaengeByBeitragId(6)).isEmpty();
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(11)
    @Test
    void addAnhangRefactored_whenExcuted_thenNumberOfEntitiesmustBe6(){
        // arrange
        var expectedEntities = 6;
        // act
        anhangService.addAnhangRefactored(6);
        // assert
        var actualEntities = anhangService.getAllAnhaenge();
        assertEquals(expectedEntities, actualEntities.size());
    }


    @Order(12)
    @Test
    @Sql(statements = {
            "DELETE FROM anhang WHERE id = '6'",
            "DELETE FROM anhang WHERE id = '7'",
            "DELETE FROM anhang WHERE beitrag_id = '6'",
            "DELETE FROM beitrag WHERE id = '6'",
            "ALTER SEQUENCE anhangs_id_seq RESTART",
            "ALTER SEQUENCE beitrags_id_seq RESTART",
            "DELETE FROM baustelle WHERE id = '5'"
    })
    void getAllAnhaenge_checkNumberOfEntitiesAfterDeletingTestData_must5() {
        //arrange
        var expectedEntities = 5;
        //actual
        var actualEntities = anhangService.getAllAnhaenge();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

}
