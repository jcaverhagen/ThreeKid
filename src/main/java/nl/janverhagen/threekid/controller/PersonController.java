package nl.janverhagen.threekid.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/people")
public class PersonController {

    @PostMapping
    public void savePerson() {

    }

    @DeleteMapping
    public void deletePerson() {

    }

}
