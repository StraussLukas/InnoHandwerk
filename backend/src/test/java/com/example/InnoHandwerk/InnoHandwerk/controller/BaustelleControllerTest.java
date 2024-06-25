package com.example.InnoHandwerk.InnoHandwerk.controller;

import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationstest")
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

    private final Baustellenbesetzung baustellenbesetzung1 = new Baustellenbesetzung();
    private final Baustellenbesetzung baustellenbesetzung2 = new Baustellenbesetzung();

    @BeforeAll
    void setUp() {
        validBaustelle1.setTitel("Baustelle 1");
        validBaustelle1.setName_bauherr("Bauherr1");
        validBaustelle1.setAdresse("Adresse1");
        validBaustelle1.setStatus("Erstellt");
        validBaustelle1.setTelefon("123456789");
        validBaustelle1.setEmail("bauherr1@example.com");
        validBaustelle1.setArbeitsaufwand(10);

        validBaustelle2.setTitel("Baustelle 2");
        validBaustelle2.setName_bauherr("Bauherr2");
        validBaustelle2.setAdresse("Adresse2");
        validBaustelle2.setStatus("In Arbeit");
        validBaustelle2.setTelefon("987654321");
        validBaustelle2.setEmail("bauherr2@example.com");
        validBaustelle2.setArbeitsaufwand(20);

        updatedBaustelle2.setId(6);
        updatedBaustelle2.setTitel("Baustelle 2");
        updatedBaustelle2.setName_bauherr("Bauherr2");
        updatedBaustelle2.setAdresse("Adresse2");
        updatedBaustelle2.setStatus("In Arbeit");
        updatedBaustelle2.setTelefon("987654321");
        updatedBaustelle2.setEmail("bauherr2@example.com");
        updatedBaustelle2.setArbeitsaufwand(45);
        updatedBaustelle2.setZeitstempel(LocalDateTime.of(2024,6,21,21,14,45));

        baustellenbesetzung1.setPersonalnummer(500);
        baustellenbesetzung1.setBaustellenId(5);
        baustellenbesetzung1.setDatum(20230530.0);
        baustellenbesetzung1.setUhrzeitVon(Time.valueOf("08:00:00"));
        baustellenbesetzung1.setUhrzeitBis(Time.valueOf("16:00:00"));

        baustellenbesetzung2.setPersonalnummer(500);
        baustellenbesetzung2.setBaustellenId(6);
        baustellenbesetzung2.setDatum(20230530.0);
        baustellenbesetzung2.setUhrzeitVon(Time.valueOf("08:00:00"));
        baustellenbesetzung2.setUhrzeitBis(Time.valueOf("16:00:00"));
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
                        get("/baustelle/" + 5)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.titel").value("Baustelle 1"))
                .andExpect(jsonPath("$.name_bauherr").value("Bauherr1"))
                .andExpect(jsonPath("$.adresse").value("Adresse1"))
                .andExpect(jsonPath("$.status").value("Erstellt"))
                .andExpect(jsonPath("$.telefon").value("123456789"))
                .andExpect(jsonPath("$.email").value("bauherr1@example.com"))
                .andExpect(jsonPath("$.arbeitsaufwand").value(10));
    }

    @Test
    @Order(5)
    void getAllBaustelleByStatus_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustellenbystatus/"+"Erstellt")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);

    }

    @Order(6)
    @Test
    void getAllBaustellenByPersonalnummer_thenReturnEntities() throws Exception {

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(baustellenbesetzung1);
        this.mockMvc.perform(
                        post("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(baustellenbesetzung2);
        this.mockMvc.perform(
                        post("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        // actual
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustellenbypersonalnummer/500")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});

        // assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(5);
        assertThat(result.get(1).getId()).isEqualTo(6);

    }

    @Test
    @Order(7)
    void getBaustelleById_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/baustelle/" + 999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
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
                .andExpect(jsonPath("$.titel").value("Baustelle 2"))
                .andExpect(jsonPath("$.name_bauherr").value("Bauherr2"))
                .andExpect(jsonPath("$.adresse").value("Adresse2"))
                .andExpect(jsonPath("$.status").value("In Arbeit"))
                .andExpect(jsonPath("$.telefon").value("987654321"))
                .andExpect(jsonPath("$.email").value("bauherr2@example.com"))
                .andExpect(jsonPath("$.arbeitsaufwand").value(45));
    }

    @Test
    @Order(9)
    @Sql(statements = {
            "DELETE FROM baustellenbesetzung WHERE id = 5",
            "DELETE FROM baustellenbesetzung WHERE id = 6",
            "ALTER SEQUENCE baustellenbesetzung_id_seq RESTART"
    })
    void deleteBaustelleById_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/baustelle/" + 6)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @Sql(statements = {
            "DELETE FROM baustelle WHERE id = 5",
            "DELETE FROM baustelle WHERE id = 6",
            "ALTER SEQUENCE baustelle_id_seq RESTART"

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
