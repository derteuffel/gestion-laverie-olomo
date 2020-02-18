package com.derteuffel.controllers;

import com.derteuffel.entities.Ajout;
import com.derteuffel.entities.Boisson;
import com.derteuffel.repositories.AjoutRepository;
import com.derteuffel.repositories.BoissonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AjoutController {

    @Autowired
    private AjoutRepository boissonRepository;

    @Autowired
    private BoissonRepository boissonRepository1;

    @GetMapping("/ajouts")
    public ResponseEntity<List<Ajout>> getAllAjoutBoissons(){
        List<Ajout> boissons = new ArrayList<>();

        try {
            boissonRepository.findAll().forEach(boissons :: add);
            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            System.out.println(boissons);
            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("present");
            return new ResponseEntity<>((List<Ajout>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/ajouts/{id}")
    public ResponseEntity<Ajout> getOne(@PathVariable Long id){

        Optional<Ajout> boisson = boissonRepository.findById(id);
        if (boisson.isPresent()){
            return new ResponseEntity<>(boisson.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/ajouts/{id}")
    public ResponseEntity<Ajout> postBoisson(@RequestBody Ajout ajout, @PathVariable Long id){
        Boisson boisson = boissonRepository1.getOne(id);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        try {
            if (boisson != null) {
                ajout.setBoisson(boisson);
                ajout.setName(boisson.getName());
                ajout.setDateJour(strDate);
                boisson.setNbreCasier((boisson.getNbreCasier() + ajout.getQuantite()));
                if (boisson.getModel().equals("PETIT")){
                    boisson.setQuantite((int) (boisson.getNbreCasier() * 24));
                }else {
                    boisson.setQuantite((int) (boisson.getNbreCasier() * 12));
                }
                boissonRepository1.save(boisson);
            }
                Ajout _boisson = boissonRepository.save(ajout);

            return new ResponseEntity<>(_boisson, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>((Ajout) null, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @DeleteMapping("/ajouts/{id}")
    public ResponseEntity<HttpStatus> deleteBoisson(@PathVariable Long id){

        Boisson boisson = boissonRepository1.getOne(boissonRepository.getOne(id).getBoisson().getId());
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
    public ResponseEntity<List<Ajout>> getAllByBoisson(@PathVariable Long id){

        try {
            List<Ajout> boissons = boissonRepository.findAllByBoisson_Id(id);
            System.out.println(boissons);
            System.out.println("je suis la");

            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/ajouts/name/{name}")
    public ResponseEntity<List<Ajout>> getAllByName(@PathVariable String name){

        try {
            List<Ajout> boissons = boissonRepository.findAllByName(name);

            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/ajouts/{id}")
    public ResponseEntity<Ajout> update(@PathVariable Long id, @RequestBody Ajout ajout){
        Optional<Ajout> boisson1 = boissonRepository.findById(id);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        if (boisson1.isPresent()){
            Ajout _boisson = boisson1.get();
            Boisson boisson2 = boissonRepository1.getOne(_boisson.getBoisson().getId());

            _boisson.setComment(ajout.getComment());
           _boisson.setName(ajout.getName());
           _boisson.setQuantite(ajout.getQuantite());
           _boisson.setDateJour(strDate);

           boisson2.setQuantite((int) (boisson2.getQuantite() + _boisson.getQuantite()));
           boissonRepository1.save(boisson2);

            return new ResponseEntity<>(boissonRepository.save(_boisson),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
