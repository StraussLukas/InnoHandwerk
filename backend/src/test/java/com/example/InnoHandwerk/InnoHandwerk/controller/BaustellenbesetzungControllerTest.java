package com.example.InnoHandwerk.InnoHandwerk.controller;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Time;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaustellenbesetzungControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Baustellenbesetzung validBaustellenbesetzung1 = new Baustellenbesetzung();
    private final Baustellenbesetzung validBaustellenbesetzung2 = new Baustellenbesetzung();
    private final Baustellenbesetzung updatedBaustellenbesetzung2 = new Baustellenbesetzung();

    @BeforeAll
    void setUp() {
        validBaustellenbesetzung1.setPersonalnummer(1001);
        validBaustellenbesetzung1.setBaustellen_id("B-001");
        validBaustellenbesetzung1.setDatum(20230530.0);
        validBaustellenbesetzung1.setUhrzeit_von(Time.valueOf("08:00:00"));
        validBaustellenbesetzung1.setUhrzeit_bis(Time.valueOf("17:00:00"));

        validBaustellenbesetzung2.setPersonalnummer(1002);
        validBaustellenbesetzung2.setBaustellen_id("B-002");
        validBaustellenbesetzung2.setDatum(20230530.0);
        validBaustellenbesetzung2.setUhrzeit_von(Time.valueOf("09:00:00"));
        validBaustellenbesetzung2.setUhrzeit_bis(Time.valueOf("18:00:00"));

        updatedBaustellenbesetzung2.setPersonalnummer(1002);
        updatedBaustellenbesetzung2.setBaustellen_id("B-002");
        updatedBaustellenbesetzung2.setDatum(20230530.0);
        updatedBaustellenbesetzung2.setUhrzeit_von(Time.valueOf("10:00:00"));
        updatedBaustellenbesetzung2.setUhrzeit_bis(Time.valueOf("19:00:00"));
    }

    @Test
    @Order(1)
    void getAllBaustellenBesetzung_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOk() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustellenbesetzung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(4);
    }

    @Test
    @Order(2)
    void postBaustellenBesetzung_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validBaustellenbesetzung1);
        this.mockMvc.perform(
                        post("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validBaustellenbesetzung2);
        this.mockMvc.perform(
                        post("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllBaustellenBesetzung_checkNumberOfEntitiesAfterAddingTestData_thenStatusOk() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustellenbesetzung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(6);
    }

    @Test
    @Order(4)
    void getBaustellenBesetzungByPersonalnummer_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/baustellenBesetzung/" + 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(1))
                .andExpect(jsonPath("$.baustellen_id").value("B-001"))
                .andExpect(jsonPath("$.datum").value(20230530.0))
                .andExpect(jsonPath("$.uhrzeit_von").value("08:00:00"))
                .andExpect(jsonPath("$.uhrzeit_bis").value("17:00:00"));
    }

    @Test
    @Order(5)
    void getBaustellenBesetzungByPersonalnummer_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/baustellenBesetzung/" + 999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putBaustellenBesetzung_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updatedBaustellenbesetzung2);
        this.mockMvc.perform(
                        put("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/baustellenBesetzung/" + 1002)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(1002))
                .andExpect(jsonPath("$.baustellen_id").value("B-002"))
                .andExpect(jsonPath("$.datum").value(20230530.0))
                .andExpect(jsonPath("$.uhrzeit_von").value("10:00:00"))
                .andExpect(jsonPath("$.uhrzeit_bis").value("19:00:00"));
    }

    @Test
    @Order(7)
    void deleteBaustellenBesetzungByPersonalnummer_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/baustellenBesetzung/" + 1001)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @Sql(statements = {
            "DELETE FROM baustellenbesetzung WHERE personalnummer = 1002"
    })
    void getAllBaustelle_checkNumberOfEntitiesAfterDeletingTestData_mustBe4() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(result).hasSize(4);
    }


}
