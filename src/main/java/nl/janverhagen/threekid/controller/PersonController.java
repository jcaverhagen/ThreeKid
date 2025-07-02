package nl.janverhagen.threekid.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.dto.PersonRequest;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.service.FamilyMatcherService;
import nl.janverhagen.threekid.service.PersonService;
import nl.janverhagen.threekid.service.RelationshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "Person API",
        version = "1.0",
        description = "API for managing people"
    )
)
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/people")
public class PersonController {

    private final PersonService personService;
    private final RelationshipService relationshipService;
    private final FamilyMatcherService familyMatcherService;
    private final PersonMapper personMapper;

    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Create a new person",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = PersonRequest.class),
                    examples = @ExampleObject(
                            name = "examplePerson",
                            summary = "An example person",
                            value = """
            {
              "id": 42,
              "name": "Ada Lovelace",
              "birthDate": "1815-12-10",
              "parent1": { "id": 1 },
              "parent2": { "id": 2 },
              "partner": { "id": 99 },
              "children": [
                { "id": 77 },
                { "id": 78 }
              ]
            }
            """
                    )
            )
    )
    public ResponseEntity<List<Person>> savePerson(@RequestBody PersonRequest personRequest) {
        Person person = personMapper.map(personRequest);
        personService.savePerson(person);
        relationshipService.ensureBidirectional(person);

        List<Person> matches = familyMatcherService.findMatchingPersons();

        if(!matches.isEmpty()) {
            return ResponseEntity.ok(matches);
        } else {
            return ResponseEntity.status(444).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPerson(@PathVariable Long id) {
        long personId = personService.findPersonById(id);
        if(personId == -1) {
            return ResponseEntity.status(404).body("Person not found");
        }
        return ResponseEntity.ok("Person found with ID: " + personId);
    }

    @DeleteMapping()
    public ResponseEntity<String> deletePerson(@RequestBody List<Long> ids) {
        ids.forEach(personService::deleteAndIgnore);
        return ResponseEntity.noContent().build();
    }

}
