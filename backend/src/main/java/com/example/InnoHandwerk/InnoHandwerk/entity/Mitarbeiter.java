package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Mitarbeiter {
    @Id
    private Integer personalnummer;
    private String vorname;
    private String nachname;
    private String email;
}