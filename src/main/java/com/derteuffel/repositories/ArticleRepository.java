package com.derteuffel.repositories;

import com.derteuffel.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByBar_Id(Long id);
    Article findByNameAndModel(String name, String model);
}
