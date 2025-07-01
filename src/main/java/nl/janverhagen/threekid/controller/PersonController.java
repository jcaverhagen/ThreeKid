package nl.janverhagen.threekid.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import nl.janverhagen.threekid.dto.PersonRequest;
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
@RequestMapping("/api/v1/people")
public class PersonController {

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
    public ResponseEntity<String> savePerson(@Valid @RequestBody PersonRequest personRequest) {
        System.out.println("Saving person: " + personRequest);
        return ResponseEntity.ok("Person saved successfully");
    }

    @DeleteMapping
    public void deletePerson() {

    }

}
