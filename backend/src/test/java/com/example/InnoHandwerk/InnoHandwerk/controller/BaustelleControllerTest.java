package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
public class BaustelleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Baustelle validBaustelle1 = new Baustelle();
    private final Baustelle validBaustelle2 = new Baustelle();
    private final Baustelle updatedBaustelle2 = new Baustelle();

    @BeforeAll
    void setUp() {
        validBaustelle1.setId(5);
        validBaustelle1.setName_bauherr("Max Mustermann");
        validBaustelle1.setAdresse("Musterstraße 1, 12345 Musterstadt");
        validBaustelle1.setTelefon("+49 123 4567890");
        validBaustelle1.setEmail("max.mustermann@example.com");
        validBaustelle1.setArbeitsaufwand(40);

        validBaustelle2.setId(6);
        validBaustelle2.setName_bauherr("Erika Mustermann");
        validBaustelle2.setAdresse("Beispielweg 2, 54321 Beispielstadt");
        validBaustelle2.setTelefon("+49 987 6543210");
        validBaustelle2.setEmail("erika.mustermann@example.com");
        validBaustelle2.setArbeitsaufwand(35);

        updatedBaustelle2.setId(6);
        updatedBaustelle2.setName_bauherr("Erika Mustermann");
        updatedBaustelle2.setAdresse("Beispielweg 2, 54321 Beispielstadt");
        updatedBaustelle2.setTelefon("+49 987 6543210");
        updatedBaustelle2.setEmail("erika.mustermann@example.com");
        updatedBaustelle2.setArbeitsaufwand(45);
    }

    @Test
    @Order(1)
    void getAllBaustelle_checkNumberOfEntitiesBeforeAddingTestData_mustBe4() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustelle")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(result).hasSize(4);

    }

    @Test
    @Order(2)
    void postBaustelle_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validBaustelle1);
        this.mockMvc.perform(
                        post("/baustelle")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validBaustelle2);
        this.mockMvc.perform(
                        post("/baustelle")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllBaustelle_checkNumberOfEntitiesAfterAddingTestData_mustBe6() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustelle")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(result).hasSize(6);
    }

    @Test
    @Order(4)
    void getBaustelleById_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/baustelle/" + 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name_bauherr").value("Max Mustermann"))
                .andExpect(jsonPath("$.adresse").value("Musterstraße 1, 12345 Musterstadt"))
                .andExpect(jsonPath("$.telefon").value("+49 123 4567890"))
                .andExpect(jsonPath("$.email").value("max.mustermann@example.com"))
                .andExpect(jsonPath("$.arbeitsaufwand").value(40));
    }

    @Test
    @Order(5)
    void getBaustelleById_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/baustelle/" + 999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putBaustelle_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updatedBaustelle2);
        this.mockMvc.perform(
                        put("/baustelle")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/baustelle/" + 6)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.name_bauherr").value("Erika Mustermann"))
                .andExpect(jsonPath("$.adresse").value("Beispielweg 2, 54321 Beispielstadt"))
                .andExpect(jsonPath("$.telefon").value("+49 987 6543210"))
                .andExpect(jsonPath("$.email").value("erika.mustermann@example.com"))
                .andExpect(jsonPath("$.arbeitsaufwand").value(45));
    }

    @Test
    @Order(7)
    void deleteBaustelleById_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/baustelle/" + 5)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @Sql(statements = {
            "DELETE FROM baustelle WHERE id = 6"
    })
    void getAllBaustelle_checkNumberOfEntitiesAfterDeletingTestData_mustBe4() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustelle")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(result).hasSize(4);
    }
}
