package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Time;

@Entity
@Getter
@Setter
public class Baustellenbesetzung {
    @Id
    @GeneratedValue(generator = "baustellenbesetzung-generator")
    @GenericGenerator(
            name = "baustellenbesetzung-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "baustellenbesetzung_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "5"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Integer id;
    private Integer personalnummer;
    private Integer baustellenId;
    private Double datum;
    private Time uhrzeitVon;
    private Time uhrzeitBis;
}
