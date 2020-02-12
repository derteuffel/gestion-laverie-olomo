package com.derteuffel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ajout_boisson")
public class AjoutBoisson implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Date dateJour = new Date();

    private String comment;

    private float quantite;

    @ManyToOne
    @JsonIgnore
    private Boisson boisson;
}
