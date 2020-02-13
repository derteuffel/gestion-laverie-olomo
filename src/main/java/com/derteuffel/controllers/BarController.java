package com.derteuffel.controllers;

import com.derteuffel.entities.Bar;
import com.derteuffel.repositories.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/bars")
@CrossOrigin(origins = "*")
public class BarController {

    @Autowired
    private BarRepository barRepository;


    @GetMapping("/table/{table}")
    public ResponseEntity<List<Bar>> findAllByTable(@PathVariable String table) {

        try {
            List<Bar> bars = barRepository.findAllByNumTable(table);

            if (bars.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bars,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/titre/{titreFacture}")
    public ResponseEntity<List<Bar>> findAllByTitreFacture(@PathVariable String titreFacture) {
        try {
            List<Bar> bars = barRepository.findAllByTitreFacture(titreFacture);

            if (bars.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bars,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Bar>> findAll() {
        List<Bar> bars = new ArrayList<>();

        try {
            barRepository.findAll().forEach(bars:: add);
            if (bars.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bars,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Bar>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Bar>  save(@RequestBody Bar bar) {

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        try {
            bar.setNumTable(bar.getNumTable().toUpperCase());
            bar.setTitreFacture(("CONSOMATION BAR").toUpperCase());
            bar.setNumeroFacture(barRepository.findAll().size()+1);
            bar.setDateJour(strDate);
            Bar _bar = barRepository.save(bar);
            return new ResponseEntity<>(_bar, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>((Bar) null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bar> getOne(@PathVariable Long id) {

        Optional<Bar> bar = barRepository.findById(id);
        if (bar.isPresent()){
            return new ResponseEntity<>(bar.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public boolean existsById(Long aLong) {
        return barRepository.existsById(aLong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            barRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/{id}/{montantVerser}")
    public ResponseEntity<Bar> rembourse(@PathVariable Long id, @PathVariable int montantVerser){
        Bar bar = barRepository.getOne(id);

        try{

            bar.setMontantVerser(montantVerser);
            bar.setMontantRembourser(bar.getMontantVerser() - bar.getMontantTotal());
            barRepository.save(bar);
            return new ResponseEntity<>(bar,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>((Bar)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
