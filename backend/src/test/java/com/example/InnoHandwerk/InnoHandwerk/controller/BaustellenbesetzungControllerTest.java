package com.example.InnoHandwerk.InnoHandwerk.controller;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustelle;
import com.example.InnoHandwerk.InnoHandwerk.entity.Baustellenbesetzung;
import com.example.InnoHandwerk.InnoHandwerk.entity.Mitarbeiter;
import com.example.InnoHandwerk.InnoHandwerk.service.BaustelleService;
import com.example.InnoHandwerk.InnoHandwerk.service.MitarbeiterService;
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
import java.sql.Timestamp;
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
public class BaustellenbesetzungControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Autowired
    private BaustelleService baustelleService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    private final Baustellenbesetzung besetzung1 = new Baustellenbesetzung();
    private final Baustellenbesetzung besetzung2 = new Baustellenbesetzung();
    private final Baustellenbesetzung besetzung3 = new Baustellenbesetzung();

    private final Baustelle baustelle = new Baustelle();
    private final Baustelle baustelle2 = new Baustelle();


    private final Mitarbeiter mitarbeiter1001 = new Mitarbeiter();
    private final Mitarbeiter mitarbeiter1002 = new Mitarbeiter();

    @BeforeAll
    void setUp() {
        mitarbeiter1001.setPersonalnummer(1001);
        mitarbeiter1001.setVorname("Max");
        mitarbeiter1001.setNachname("Meier");
        mitarbeiter1001.setEmail("maxmeier@email.com");
        mitarbeiter1001.setAdmin(true);

        mitarbeiter1002.setPersonalnummer(1002);
        mitarbeiter1002.setVorname("Maria");
        mitarbeiter1002.setNachname("Meier");
        mitarbeiter1002.setEmail("mariameier@email.com");
        mitarbeiter1002.setAdmin(false);

        mitarbeiterService.addMitarbeiter(mitarbeiter1001);
        mitarbeiterService.addMitarbeiter(mitarbeiter1002);

        baustelle.setTitel("Baustelle 1");
        baustelle.setName_bauherr("Bauherr1");
        baustelle.setAdresse("Adresse1");
        baustelle.setStatus("Erstellt");
        baustelle.setTelefon("123456789");
        baustelle.setEmail("bauherr1@example.com");
        baustelle.setArbeitsaufwand(10);


        baustelleService.addBaustelle(baustelle);

        baustelle2.setTitel("Baustelle 1");
        baustelle2.setName_bauherr("Bauherr1");
        baustelle2.setAdresse("Adresse1");
        baustelle2.setStatus("Erstellt");
        baustelle2.setTelefon("123456789");
        baustelle2.setEmail("bauherr1@example.com");
        baustelle2.setArbeitsaufwand(10);


        baustelleService.addBaustelle(baustelle2);


        besetzung1.setPersonalnummer(1001);
        besetzung1.setBaustellenId(5);
        besetzung1.setDatum(20230530.0);
        besetzung1.setUhrzeitVon(Time.valueOf("08:00:00"));
        besetzung1.setUhrzeitBis(Time.valueOf("16:00:00"));

        besetzung2.setPersonalnummer(1002);
        besetzung2.setBaustellenId(6);
        besetzung2.setDatum(20230530.0);
        besetzung2.setUhrzeitVon(Time.valueOf("09:00:00"));
        besetzung2.setUhrzeitBis(Time.valueOf("17:00:00"));

        besetzung3.setPersonalnummer(1001);
        besetzung3.setBaustellenId(6);
        besetzung3.setDatum(20230530.0);
        besetzung3.setUhrzeitVon(Time.valueOf("08:00:00"));
        besetzung3.setUhrzeitBis(Time.valueOf("16:00:00"));
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
        String body = objectWriter.writeValueAsString(besetzung1);
        this.mockMvc.perform(
                        post("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(besetzung2);
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
                        get("/baustellenBesetzung/" + 5)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(1001))
                .andExpect(jsonPath("$.baustellenId").value(5))
                .andExpect(jsonPath("$.datum").value(20230530.0))
                .andExpect(jsonPath("$.uhrzeitVon").value("08:00:00"))
                .andExpect(jsonPath("$.uhrzeitBis").value("16:00:00"));
    }

    @Test
    @Order(5)
    void getBaustellenBesetzungByPersonalnummer_entityWithIdNotFound_thenNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/baustellenBesetzung/999")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Baustelle> result = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(result).isEmpty();
    }

    @Test
    @Order(6)
    void getBaustellenBesetzungByBaustellenId_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
           this.mockMvc.perform(
                        get("/baustellenBesetzung/" + 6)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(1002))
                .andExpect(jsonPath("$.datum").value(20230530.0))
                .andExpect(jsonPath("$.uhrzeitVon").value("09:00:00"))
                .andExpect(jsonPath("$.uhrzeitBis").value("18:00:00"));
    }


    @Test
    @Order(7)
    void putBaustellenBesetzung_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(besetzung3);
        this.mockMvc.perform(
                        put("/baustellenBesetzung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/baustellenBesetzung/" + 7)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(1002))
                .andExpect(jsonPath("$.baustellenId").value(6))
                .andExpect(jsonPath("$.datum").value(20230530.0))
                .andExpect(jsonPath("$.uhrzeitVon").value("09:00:00"))
                .andExpect(jsonPath("$.uhrzeitBis").value("17:00:00"));
    }

    @Test
    @Order(8)
    void deleteBaustellenBesetzungByPersonalnummer_thenStatusOk() throws Exception {
        this.mockMvc.perform(
                        delete("/baustellenBesetzung/" + 1001)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @Sql(statements = {
            "DELETE FROM baustellenbesetzung WHERE id = '5'",
            "DELETE FROM baustellenbesetzung WHERE id = '6'",
            "DELETE FROM baustellenbesetzung WHERE id = '7'",
            "DELETE FROM baustelle WHERE id = '5'",
            "DELETE FROM baustelle WHERE id = '6'",
            "DELETE FROM mitarbeiter WHERE personalnummer = '1001'",
            "DELETE FROM mitarbeiter WHERE personalnummer = '1002'",
            "ALTER SEQUENCE baustellenbesetzung_id_seq RESTART",
            "ALTER SEQUENCE baustelle_id_seq RESTART"


    })
    void getAllBaustellenbestzungen_checkNumberOfEntitiesAfterDeletingTestData_mustBe4() throws Exception {
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
