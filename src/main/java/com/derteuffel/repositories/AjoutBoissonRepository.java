package com.derteuffel.repositories;

import com.derteuffel.entities.AjoutBoisson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjoutBoissonRepository extends JpaRepository<AjoutBoisson,Long> {

    List<AjoutBoisson> findAllByName(String name);
    List<AjoutBoisson> findAllByBoisson_Id(Long id);
}
