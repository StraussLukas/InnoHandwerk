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
import java.time.LocalDateTime;
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
@ActiveProfiles("integrationstest")
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

        baustelle.setTitel("Baustelle 1");
        baustelle.setName_bauherr("Bauherr1");
        baustelle.setAdresse("Adresse1");
        baustelle.setStatus("Erstellt");
        baustelle.setTelefon("123456789");
        baustelle.setEmail("bauherr1@example.com");
        baustelle.setArbeitsaufwand(10);


        baustelleService.addBaustelle(baustelle);


        beitrag1.setFreitext("Dichtungen des Hauswasserwerks überprüfen");
        beitrag1.setBaustelleId(5);
        beitrag1.setPersonalnummer(100);

        beitrag2.setFreitext("text2");
        beitrag2.setBaustelleId(5);
        beitrag2.setPersonalnummer(100);

        beitragupdated.setId(7);
        beitragupdated.setFreitext("text3");
        beitragupdated.setZeitstempel(LocalDateTime.of(2024,6,21,21,14,45));
        beitragupdated.setBaustelleId(5);
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
                        get("/beitrag/" + "6")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.freitext").value("Dichtungen des Hauswasserwerks überprüfen"))
                .andExpect(jsonPath("$.baustelleId").value(5));
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
                        get("/beitrag/" + "7")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.freitext").value("text3"))
                .andExpect(jsonPath("$.baustelleId").value(5));
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
                        delete("/beitrag/" + "6")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitrag/" + "6")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Order(9)
    void deleteBeitraegeByBaustelllenId_checkIfEntitysNoMoreExists_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/beitraegeByBaustelle/" + "5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitrag/" + "7")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(10)
    @Sql(statements = {
            "DELETE FROM beitrag WHERE id = '6'",
            "DELETE FROM beitrag WHERE id = '7'",
            "DELETE FROM baustelle WHERE id = '5'",
            "ALTER SEQUENCE beitrags_id_seq RESTART",
            "ALTER SEQUENCE baustelle_id_seq RESTART"

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
