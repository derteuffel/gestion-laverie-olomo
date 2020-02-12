package com.derteuffel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "boisson")
public class Boisson implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private float nbreCasier;
    private String model;
    private int quantite;
    private int price;
}
