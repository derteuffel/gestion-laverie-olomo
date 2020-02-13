package com.derteuffel.controllers;

import com.derteuffel.entities.Article;
import com.derteuffel.entities.Bar;
import com.derteuffel.entities.Boisson;
import com.derteuffel.repositories.ArticleRepository;
import com.derteuffel.repositories.BoissonRepository;
import com.derteuffel.repositories.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BarRepository barRepository;

    @Autowired
    private BoissonRepository boissonRepository;

    @GetMapping("/{id}")
    public ResponseEntity<List<Article>> findAllbyBar(@PathVariable Long id) {
        List<Article> articles = new ArrayList<>();

        try {
            articleRepository.findAllByBar_Id(id).forEach(articles:: add);
            if (articles.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(articles,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Article>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Article> save(@RequestBody Article article, @PathVariable Long id){

        Bar bar = barRepository.getOne(id);
        Boisson boisson = boissonRepository.findByNameAndModel(article.getName().toUpperCase(),article.getModel());
        Article artcle = articleRepository.findByNameAndModel(article.getName(),article.getModel());

        try{
            if (artcle != null){
                artcle.setQuantite(artcle.getQuantite() + article.getQuantite());
                artcle.setPrixT(artcle.getQuantite() * artcle.getPrixU());
                boisson.setQuantite(boisson.getQuantite() - artcle.getQuantite());
                bar.setMontantTotal(bar.getMontantTotal() + artcle.getPrixT());
            }else {
                article.setBar(bar);
                article.setPrixU(boisson.getPrice());
                article.setPrixT(article.getQuantite() * boisson.getPrice());
                boisson.setQuantite(boisson.getQuantite() - article.getQuantite());
                bar.setMontantTotal(bar.getMontantTotal() + article.getPrixT());
            }

            barRepository.save(bar);
            if (boisson.getModel() == "PETIT"){
                boisson.setNbreCasier(boisson.getQuantite() / 24);
            }else {
                boisson.setNbreCasier(boisson.getQuantite() / 12);
            }
            boissonRepository.save(boisson);
            articleRepository.save(article);
            return new ResponseEntity<>(article, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>((Article) null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/add/{id}")
    public ResponseEntity<Article> add(@PathVariable Long id){

        try {
            System.out.println("present");
            Article article = articleRepository.getOne(id);
            Bar bar = barRepository.getOne(article.getBar().getId());
            bar.setMontantTotal(bar.getMontantTotal()+article.getPrixU());
            barRepository.save(bar);
            System.out.println("present 1");
            Boisson boisson = boissonRepository.findByNameAndModel(article.getName(), article.getModel());
            System.out.println("present 2");
            article.setQuantite(article.getQuantite()+1);
            boisson.setQuantite(boisson.getQuantite()-1);
            if (boisson.getModel() == "PETIT"){
                boisson.setNbreCasier(boisson.getQuantite()/24);
            }else {
                boisson.setNbreCasier(boisson.getQuantite()/12);
            }
            article.setPrixT(article.getPrixU() * article.getQuantite());
            System.out.println("present 3");
            articleRepository.save(article);
            boissonRepository.save(boisson);
            return new ResponseEntity<>(article, HttpStatus.CREATED);
        }catch (Exception e){

            return new ResponseEntity<>((Article)null, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<Article> remove(@PathVariable Long id){

        try {
            Article article = articleRepository.getOne(id);
            Boisson boisson = boissonRepository.findByNameAndModel(article.getName(), article.getModel());
            article.setQuantite(article.getQuantite()-1);
            boisson.setQuantite(boisson.getQuantite()+1);
            if (boisson.getModel() == "PETIT"){
                boisson.setNbreCasier(boisson.getQuantite()/24);
            }else {
                boisson.setNbreCasier(boisson.getQuantite()/12);
            }
            article.setPrixT(article.getPrixU() * article.getQuantite());
            articleRepository.save(article);
            boissonRepository.save(boisson);
            return new ResponseEntity<>(article, HttpStatus.OK);
        }catch (Exception e){

            return new ResponseEntity<>((Article)null, HttpStatus.EXPECTATION_FAILED);
        }

    }

}
