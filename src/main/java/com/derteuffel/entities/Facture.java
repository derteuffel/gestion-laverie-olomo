package com.derteuffel.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "facture")
@Inheritance(strategy = InheritanceType.JOINED)
public class Facture implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String dateJour;
    private String titreFacture;
    private int montantTotal;
    private int montantVerser;
    private int montantRembourser;
    private int numeroFacture;


}
