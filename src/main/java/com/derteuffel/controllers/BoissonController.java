package com.derteuffel.controllers;

import com.derteuffel.entities.Ajout;
import com.derteuffel.entities.Boisson;
import com.derteuffel.repositories.AjoutRepository;
import com.derteuffel.repositories.BoissonRepository;
import com.derteuffel.uploads.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class BoissonController {

    @Autowired
    private BoissonRepository boissonRepository;

    @Autowired
    private AjoutRepository ajoutRepository;

    private final FileService fileService;

    @Autowired
    public BoissonController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping("/boissons")
    public ResponseEntity<List<Boisson>> getAllBoissons(){
        List<Boisson> boissons = new ArrayList<>();

        try {
            boissonRepository.findAll().forEach(boissons :: add);
            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Boisson>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/boissons/{id}")
    public ResponseEntity<Boisson> getOne(@PathVariable Long id){

        Optional<Boisson> boisson = boissonRepository.findById(id);
        if (boisson.isPresent()){
            return new ResponseEntity<>(boisson.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/boissons")
    public ResponseEntity<Boisson> postBoisson(@RequestBody Boisson boisson) throws IOException {
        Boisson boisson1 = boissonRepository.findByNameAndModel(boisson.getName().toUpperCase(), boisson.getModel());
        if (boisson1 != null){
            Ajout ajout = new Ajout();
            ajout.setName(boisson.getName().toUpperCase());
            ajout.setQuantite(boisson.getNbreCasier());
            ajout.setComment("Ajout d'un stock de "+boisson.getName() + " existante");
            ajoutRepository.save(ajout);
            boisson1.setNbreCasier(boisson1.getNbreCasier()+ajout.getQuantite());
            if (boisson1.getModel() == "PETIT"){
                boisson1.setQuantite((int) (boisson1.getNbreCasier()*24));
            }else {
                boisson1.setQuantite((int) (boisson1.getNbreCasier()*12));
            }
            boissonRepository.save(boisson1);
            return new ResponseEntity<>(boisson1, HttpStatus.CREATED);
        }else {
            try {
                if (boisson.getModel() == "PETIT"){
                    boisson.setQuantite((int) (boisson.getNbreCasier()*24));
                }else {
                    boisson.setQuantite((int) (boisson.getNbreCasier()*12));
                }
                boisson.setName(boisson.getName().toUpperCase());
                boisson.setType(boisson.getType().toUpperCase());
                Boisson _boisson = boissonRepository.save(boisson);
                return new ResponseEntity<>(_boisson, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>((Boisson) null, HttpStatus.EXPECTATION_FAILED);
            }
        }
    }


    @DeleteMapping("/boissons/{id}")
    public ResponseEntity<HttpStatus> deleteBoisson(@PathVariable Long id){
        try {
            boissonRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/boissons")
    public ResponseEntity<HttpStatus> deleteAllBoissons(){
        try {
            boissonRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/boissons/price/{price}")
    public ResponseEntity<List<Boisson>> getAllByPrice(@PathVariable int price){

        try {
            List<Boisson> boissons = boissonRepository.findAllByPrice(price);

            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/boissons/type/{type}")
    public ResponseEntity<List<Boisson>> getAllByType(@PathVariable String type){

        try {
            List<Boisson> boissons = boissonRepository.findAllByType(type);

            if (boissons.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(boissons,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/boissons/{id}")
    public ResponseEntity<Boisson> update(@PathVariable Long id, @RequestBody Boisson boisson){
        Optional<Boisson> boisson1 = boissonRepository.findById(id);

        if (boisson1.isPresent()){
            Boisson _boisson = boisson1.get();
            _boisson.setName(boisson.getName().toUpperCase());
            _boisson.setPrice(boisson.getPrice());
            _boisson.setModel(boisson.getModel());
            _boisson.setNbreCasier(boisson.getNbreCasier());
            if (boisson.getModel() == "PETIT"){
                _boisson.setQuantite((int) (boisson.getNbreCasier()*24));
            }else {
                _boisson.setQuantite((int)boisson.getNbreCasier()*12);
            }
            _boisson.setType(boisson.getType().toUpperCase());

            return new ResponseEntity<>(boissonRepository.save(_boisson),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
