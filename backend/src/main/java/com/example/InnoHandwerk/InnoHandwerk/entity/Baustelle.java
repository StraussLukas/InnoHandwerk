package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Baustelle {
    @Id
    @GeneratedValue(generator = "baustelle-generator")
    @GenericGenerator(
            name = "baustelle-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "baustelle_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "5"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Integer id;
    private String titel;
    private String name_bauherr;
    private String adresse;
    private String status;
    private String telefon;
    private String email;
    private Integer arbeitsaufwand;
    @CreationTimestamp
    private LocalDateTime zeitstempel;
}
