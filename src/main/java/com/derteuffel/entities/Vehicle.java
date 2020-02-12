package com.derteuffel.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String matricule;
    private String nomClient;
    private String marque;
    private String typeVehicle;
    private int nombreApparition;

    private Date dateJour = new Date();

    @OneToMany(mappedBy = "vehicle")
    private List<Apparition> apparitions;

}
