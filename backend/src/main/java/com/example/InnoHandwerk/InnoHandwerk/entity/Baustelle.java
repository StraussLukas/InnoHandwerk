package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Baustelle {
    @Id
    private Integer id;
    private String titel;
    private String name_bauherr;
    private String adresse;
    private String status;
    private String telefon;
    private String email;
    private Integer arbeitsaufwand;
    private Timestamp zeitstempel;
}
