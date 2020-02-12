package com.derteuffel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "article")
public class Article implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int quantite;
    private int prixU;
    private int prixT;
    private String model;

    @ManyToOne
    @JsonIgnore
    private Bar bar;

}
