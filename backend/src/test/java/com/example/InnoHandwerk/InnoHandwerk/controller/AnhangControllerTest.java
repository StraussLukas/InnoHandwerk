package com.example.InnoHandwerk.InnoHandwerk.controller;
import com.example.InnoHandwerk.InnoHandwerk.entity.Beitrag;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.example.InnoHandwerk.InnoHandwerk.entity.Anhang;
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
public class AnhangControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Anhang anhang1 = new Anhang();
    private final Anhang anhang2 = new Anhang();
    private final Anhang anhangUpdated = new Anhang();

    @BeforeAll
    void setUp() {
        anhang1.setId(-1);
        anhang1.setDatei("path1");
        anhang1.setBeitragId(4);

        anhang2.setId(-2);
        anhang2.setDatei("path2");
        anhang2.setBeitragId(5);

        anhangUpdated.setId(-2);
        anhangUpdated.setDatei("path1");
        anhangUpdated.setBeitragId(5);
    }
    @Test
    @Order(1)
    void getAllAnhaenge_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/anhang")
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
    void postAnhaenge_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(anhang1);
        this.mockMvc.perform(
                        post("/anhang")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(anhang2);
        this.mockMvc.perform(
                        post("/anhang")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllAnhaenge_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe7() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/anhang")
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
    void getAnhaengeById_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/anhang/" + "1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.datei").value("../db/files/pic1.png"))
                .andExpect(jsonPath("$.beitragId").value(1));
    }

    @Test
    @Order(5)
    void getAnhaengeById_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(
                        get("/anhang/" + "-5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putAnhang_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(anhangUpdated);
        this.mockMvc.perform(
                        post("/anhang")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/anhang/" + "-2")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-2))
                .andExpect(jsonPath("$.datei").value("path1"))
                .andExpect(jsonPath("$.beitragId").value(5));
    }

    @Test
    @Order(7)
    void getAnaengeByBeitragId_checkNumberOfEntities_MustBe2() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/anhaengeByBeitrag/" + "1")
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
    void deleteAnhangById_checkIfEntityNoMoreExists_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/anhang/" + "-1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/anhang/" + "-1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Order(9)
    void deleteAnhaengeByBeitragId_checkIfEntitysNoMoreExists_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/anhaengeByBeitrag/" + "5")
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
            "DELETE FROM anhang WHERE id = '-1'",
            "DELETE FROM anhang WHERE id = '-2'"
    })
    void getAllAnhaenge_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/anhang")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Anhang> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(5);
    }
}
