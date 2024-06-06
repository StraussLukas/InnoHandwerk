
package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnhangServiceTest {

    @Autowired
    private AnhangService anhangService;

    private final Anhang anhang1 = new Anhang();
    private final Anhang anhang2 = new Anhang();

    @BeforeAll
    void setUp() {
        anhang1.setId(-1);
        anhang1.setDatei("path1");
        anhang1.setBeitragId(5);

        anhang2.setId(-2);
        anhang2.setDatei("path2");
        anhang2.setBeitragId(4);
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
        assertThat(actualId1).isEqualTo(-1);
        assertThat(actualId2).isEqualTo(-2);
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
        var actualEntity = anhangService.getAnhangById(-1);
        //assert
        assertThat(actualEntity).isPresent();
        assertEquals(5, actualEntity.get().getBeitragId());
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
        updatedAnhang.setId(-1);
        updatedAnhang.setDatei("path1");
        updatedAnhang.setBeitragId(4);
        //actual
        var actualId = anhangService.updateAnhang(updatedAnhang);
        var actualEntity = anhangService.getAnhangById(actualId);
        //assert
        assertThat(actualId).isEqualTo(-1);
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
        assertThat(actual).hasSize(3);
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
        anhangService.deleteAnhangById(-1);
        anhangService.deleteAnhangById(-2);
        // assert
        assertThat(anhangService.getAnhangById(-1)).isNotPresent();
    }

    @Order(10)
    @Test
    void deleteAnhangByBaustellenId_whenExcuted_thenNumberOfEntitiesmustBe5() {
        //arrange
        var expectedEntities = 5;
        // act
        anhangService.deleteAllAnhaengeByBeitragId(-20);
        var actualEntities = anhangService.getAllAnhaenge();
        // assert
        assertThat(anhangService.getAllAnhaengeByBeitragId(-20)).isEmpty();
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(11)
    @Test
    @Sql(statements = {
            "DELETE FROM anhang WHERE id = '-1'",
            "DELETE FROM anhang WHERE id = '-2'"
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
