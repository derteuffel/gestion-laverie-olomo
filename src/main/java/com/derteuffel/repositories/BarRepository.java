package com.derteuffel.repositories;

import com.derteuffel.entities.Bar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarRepository extends JpaRepository<Bar, Long> {

    List<Bar> findAllByNumTable(String numTable);
    List<Bar>  findAllByTitreFacture(String titreFacture);
}
