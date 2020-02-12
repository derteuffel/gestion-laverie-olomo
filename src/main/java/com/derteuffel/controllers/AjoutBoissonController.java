package com.derteuffel.controllers;

import com.derteuffel.entities.AjoutBoisson;
import com.derteuffel.entities.Boisson;
import com.derteuffel.repositories.AjoutBoissonRepository;
import com.derteuffel.repositories.BoissonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AjoutBoissonController {

    @Autowired
    private AjoutBoissonRepository boissonRepository;

    @Autowired
    private BoissonRepository boissonRepository1;

    @GetMapping("/ajouts")
    public ResponseEntity<List<AjoutBoisson>> getAllAjoutBoissons(){
        List<AjoutBoisson> boissons = new ArrayList<>();

        try {
            boissonRepository.findAll().forEach(boissons :: add);
            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            System.out.println(boissons);
            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("present");
            return new ResponseEntity<>((List<AjoutBoisson>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/ajouts/{id}")
    public ResponseEntity<AjoutBoisson> getOne(@PathVariable Long id){

        Optional<AjoutBoisson> boisson = boissonRepository.findById(id);
        if (boisson.isPresent()){
            return new ResponseEntity<>(boisson.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/ajouts/{id}")
    public ResponseEntity<AjoutBoisson> postBoisson(@RequestBody AjoutBoisson ajoutBoisson, @PathVariable Long id){
        Boisson boisson = boissonRepository1.getOne(id);
        try {
            if (boisson != null) {
                ajoutBoisson.setBoisson(boisson);
                ajoutBoisson.setName(boisson.getName());
                boisson.setNbreCasier((boisson.getNbreCasier() + ajoutBoisson.getQuantite()));
                if (boisson.getModel() == "PETIT"){
                    boisson.setQuantite((int) (boisson.getNbreCasier() * 24));
                }else {
                    boisson.setQuantite((int) (boisson.getNbreCasier() * 12));
                }
                boissonRepository1.save(boisson);
            }
                AjoutBoisson _boisson = boissonRepository.save(ajoutBoisson);

            return new ResponseEntity<>(_boisson, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>((AjoutBoisson) null, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @DeleteMapping("/ajouts/{id}")
    public ResponseEntity<HttpStatus> deleteBoisson(@PathVariable Long id){

        Boisson boisson = boissonRepository1.getOne(boissonRepository.getOne(id).getBoisson().getId());
        boisson.getAjoutBoissons().remove(boissonRepository.getOne(id));
        boissonRepository1.save(boisson);
        try {
            boissonRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/ajouts")
    public ResponseEntity<HttpStatus> deleteAllBoissons(){
        try {
            boissonRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/ajouts/boisson/{id}")
    public ResponseEntity<List<AjoutBoisson>> getAllByBoisson(@PathVariable Long id){

        try {
            List<AjoutBoisson> boissons = boissonRepository.findAllByBoisson_Id(id);
            System.out.println(boissons);

            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/ajouts/name/{name}")
    public ResponseEntity<List<AjoutBoisson>> getAllByName(@PathVariable String name){

        try {
            List<AjoutBoisson> boissons = boissonRepository.findAllByName(name);

            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/ajouts/{id}")
    public ResponseEntity<AjoutBoisson> update(@PathVariable Long id, @RequestBody AjoutBoisson boisson){
        Optional<AjoutBoisson> boisson1 = boissonRepository.findById(id);
        if (boisson1.isPresent()){
            AjoutBoisson _boisson = boisson1.get();
            Boisson boisson2 = boissonRepository1.getOne(_boisson.getBoisson().getId());

            _boisson.setComment(boisson.getComment());
           _boisson.setName(boisson.getName());
           _boisson.setQuantite(boisson.getQuantite());

           boisson2.setQuantite((int) (boisson2.getQuantite() + _boisson.getQuantite()));
           boissonRepository1.save(boisson2);

            return new ResponseEntity<>(boissonRepository.save(_boisson),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
