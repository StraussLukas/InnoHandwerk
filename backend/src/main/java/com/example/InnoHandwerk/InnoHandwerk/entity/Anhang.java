package com.example.InnoHandwerk.InnoHandwerk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
public class Anhang {

    @Id
    /*@GeneratedValue(generator = "beitrags-generator")
    @GenericGenerator(
            name = "beitrags-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "beitrags_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "7"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )*/
    private Integer id;
    private String datei;
    @Column(name = "beitrag_id")
    private Integer beitragId;
}