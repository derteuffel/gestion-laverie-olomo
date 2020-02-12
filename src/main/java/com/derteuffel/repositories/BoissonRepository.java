package com.derteuffel.repositories;

import com.derteuffel.entities.Boisson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoissonRepository extends JpaRepository<Boisson,Long> {

    Boisson findByNameAndModel(String name, String model);
    Boisson findByName(String name);

    List<Boisson> findAllByPrice(int price);
    List<Boisson> findAllByType(String type);
}
