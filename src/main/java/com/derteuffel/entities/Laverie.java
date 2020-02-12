package com.derteuffel.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "laverie")
@PrimaryKeyJoinColumn(name = "id")
public class Laverie extends Facture {

    private String nomClient;
    private String matricule;
    private String marque;
    private String typeVehicle;
}
