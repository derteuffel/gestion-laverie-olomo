package com.derteuffel.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "apparition")
public class Apparition implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String nomClient;
    private String matricule;
    private String heureEntree;
    private int montant;

    private Date dateJour = new Date();

    @ManyToOne
    private Vehicle vehicle;
}
