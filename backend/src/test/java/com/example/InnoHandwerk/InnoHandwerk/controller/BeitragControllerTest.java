package com.example.InnoHandwerk.InnoHandwerk.controller;
import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
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

    private final Beitrag beitrag1 = new Beitrag();
    private final Beitrag beitrag2 = new Beitrag();
    private final Beitrag beitragupdated = new Beitrag();

    @BeforeAll
    void setUp() {
        beitrag1.setId(-1);
        beitrag1.setFreitext("text1");
        beitrag1.setZeitstempel(Timestamp.valueOf("2024-03-21 09:15:45"));
        beitrag1.setBaustelleId(-10);

        beitrag2.setId(-2);
        beitrag2.setFreitext("text2");
        beitrag2.setZeitstempel(Timestamp.valueOf("2024-07-14 14:30:00"));
        beitrag2.setBaustelleId(-20);

        beitragupdated.setId(-2);
        beitragupdated.setFreitext("text2");
        beitragupdated.setZeitstempel(Timestamp.valueOf("2024-07-14 14:30:00"));
        beitragupdated.setBaustelleId(-30);
    }

    @Test
    @Order(1)
    void getAllBeitraege_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitrag")
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
                        get("/beitrag")
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
                        get("/beitrag/" + "1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.freitext").value("Dichtungen des Hauswasserwerks überprüfen"))
               // .andExpect(jsonPath("$.zeitstempel").value(Timestamp.valueOf("2024-01-01 12:00:00")))
                .andExpect(jsonPath("$.baustelleId").value(2));
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
                        get("/beitrag/" + "-2")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-2))
                .andExpect(jsonPath("$.freitext").value("text2"))
                // .andExpect(jsonPath("$.zeitstempel").value(Timestamp.valueOf("2024-07-14 14:30:00")))
                .andExpect(jsonPath("$.baustelleId").value(-30));
    }

    @Test
    @Order(7)
    void getBeitragByBaustellenId_checkNumberOfEntities_MustBe2() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitragByBaustelle/" + "3")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Beitrag> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(2);
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
                        delete("/beitraegeByBaustelle/" + "-30")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/anhang/" + "-2")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(10)
    @Sql(statements = {
            "DELETE FROM beitrag WHERE id = '-1'",
            "DELETE FROM beitrag WHERE id = '-2'"
    })
    void getAllBeitraege_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/beitrag")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Anhang> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(5);
    }

}
