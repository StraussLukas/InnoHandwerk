package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Entity
@Getter
@Setter
public class Baustellenbesetzung {
    @Id
    private Integer personalnummer;
    private String baustellen_id;
    private Double datum;
    private Time uhrzeit_von;
    private Time uhrzeit_bis;
}
