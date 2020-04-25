package com.roomfix.api.dog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/dogs")
public class DogController {


    private DogRepository dogRepository;

    @Autowired
    public DogController(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Dog> getDogList() {
        return this.dogRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dog getDogById(@PathVariable("id") long id) {
        return this.dogRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Dog createDog(@RequestBody Dog newDog) {
        return this.dogRepository.save(newDog);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteDog(@PathVariable("id") long id) {
        Dog dogToDelete = this.dogRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        this.dogRepository.delete(dogToDelete);

        return "Successfully deleted Dog with id: " + id;
    }


}
