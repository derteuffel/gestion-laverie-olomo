package com.derteuffel.repositories;

import com.derteuffel.entities.Ajout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjoutRepository extends JpaRepository<Ajout,Long> {

    List<Ajout> findAllByName(String name);
    List<Ajout> findAllByBoisson_Id(Long id);
}
