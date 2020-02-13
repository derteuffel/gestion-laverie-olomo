package com.derteuffel.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ajout")
public class Ajout implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;


    private String dateJour;

    private String comment;

    private float quantite;

    @ManyToOne
    @JsonIgnore
    private Boisson boisson;
}
