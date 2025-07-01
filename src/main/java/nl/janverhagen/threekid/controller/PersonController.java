package nl.janverhagen.threekid.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.dto.PersonRequest;
import nl.janverhagen.threekid.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> savePerson(@RequestBody PersonRequest personRequest) {
        personService.savePerson(personRequest);
        return ResponseEntity.ok("Person saved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPerson(@PathVariable Long id) {
        long personId = personService.findPersonById(id);
        if(personId == -1) {
            return ResponseEntity.status(404).body("Person not found");
        }
        return ResponseEntity.ok("Person found with ID: " + personId);
    }

    @DeleteMapping
    public void deletePerson() {

    }

}
