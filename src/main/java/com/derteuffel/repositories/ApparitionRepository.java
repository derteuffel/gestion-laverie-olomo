package com.derteuffel.repositories;

import com.derteuffel.entities.Apparition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApparitionRepository extends JpaRepository<Apparition, Long> {
    List<Apparition> findAllByVehicle_Id(Long id);
    List<Apparition> findAllByMatricule(String matricule);
    List<Apparition> findAllByNomClient(String nomClient);
}
