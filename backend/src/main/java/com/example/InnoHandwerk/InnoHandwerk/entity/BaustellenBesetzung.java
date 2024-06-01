package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BaustellenBesetzung {
    @Id
    private Integer personalnummer;
    private String baustellen_id;
    private Double datum;
    private String uhrzeit_von;
    private String uhrzeit_bis;
}
