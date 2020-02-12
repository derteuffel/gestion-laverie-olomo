package com.derteuffel.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "bar")
@PrimaryKeyJoinColumn(name = "id")
public class Bar extends  Facture {

    private String numTable;

    @OneToMany(mappedBy = "bar")
    private List<Article> articles;
}
