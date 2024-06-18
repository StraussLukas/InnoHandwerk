package com.example.InnoHandwerk.InnoHandwerk.controller;
import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.service.BaustelleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeitragControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BaustelleService baustelleService;

    private final Beitrag beitrag1 = new Beitrag();
    private final Beitrag beitrag2 = new Beitrag();
    private final Beitrag beitragupdated = new Beitrag();
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

        beitrag1.setId(-1);
        beitrag1.setFreitext("Dichtungen des Hauswasserwerks überprüfen");
        beitrag1.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));
        beitrag1.setBaustelleId(-1);
        beitrag1.setPersonalnummer(100);

        beitrag2.setId(-4);
        beitrag2.setFreitext("text2");
        beitrag2.setZeitstempel(Timestamp.valueOf("2024-07-14 14:30:00"));
        beitrag2.setBaustelleId(-1);
        beitrag2.setPersonalnummer(100);

        beitragupdated.setId(-4);
        beitragupdated.setFreitext("text3");
        beitragupdated.setZeitstempel(Timestamp.valueOf("2024-07-14 14:30:00"));
        beitragupdated.setBaustelleId(-1);
        beitragupdated.setPersonalnummer(100);
    }

    @Test
    @Order(1)
    void getAllBeitraege_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitraege")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Beitrag> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(5);
    }

    @Test
    @Order(2)
    void postBeitrag_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(beitrag1);
        this.mockMvc.perform(
                        post("/beitrag")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

         body = objectWriter.writeValueAsString(beitrag2);
        this.mockMvc.perform(
                        post("/beitrag")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllBeitraege_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe7() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitraege")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Beitrag> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(7);
    }

    @Test
    @Order(4)
    void getBeitragById_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/beitrag/" + "-1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-1))
                .andExpect(jsonPath("$.freitext").value("Dichtungen des Hauswasserwerks überprüfen"))
               // .andExpect(jsonPath("$.zeitstempel").value(Timestamp.valueOf("2024-01-01 12:00:00")))
                .andExpect(jsonPath("$.baustelleId").value(-1));
    }

    @Test
    @Order(5)
    void getBeitragById_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(
                        get("/beitrag/" + "-5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putBeitrag_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(beitragupdated);
        this.mockMvc.perform(
                        post("/beitrag")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/beitrag/" + "-4")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-4))
                .andExpect(jsonPath("$.freitext").value("text3"))
                // .andExpect(jsonPath("$.zeitstempel").value(Timestamp.valueOf("2024-07-14 14:30:00")))
                .andExpect(jsonPath("$.baustelleId").value(-1));
    }

    @Test
    @Order(7)
    void getBeitraegeByBaustellenId_checkNumberOfEntities_MustBe1() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitraegeByBaustelle/" + "2")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Beitrag> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(1);
    }

    @Test
    @Order(8)
    void deleteBeitragById_checkIfEntityNoMoreExists_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/beitrag/" + "-1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

      /*  this.mockMvc.perform(
                        delete("/beitrag/" + "-2")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());*/

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitrag/" + "-1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Order(9)
    void deleteBeitraegeByBaustelllenId_checkIfEntitysNoMoreExists_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/beitraegeByBaustelle/" + "-1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitrag/" + "-2")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(10)
    @Sql(statements = {
            "DELETE FROM beitrag WHERE id = '-1'",
            "DELETE FROM beitrag WHERE id = '-4'",
            "DELETE FROM baustelle WHERE id = '-1'"
    })
    void getAllBeitraege_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitraege")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Anhang> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(5);
    }

}
