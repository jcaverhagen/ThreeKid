package nl.janverhagen.threekid.service;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.repository.PersonRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validatePersonPartner() {
        // Given
        Person personWithoutPartner = Person.builder()
                .id(1L)
                .name("Jan")
                .partner(null) // No partner set
                .build();

        Person personWithPartner = Person.builder()
                .id(2L)
                .name("Piet")
                .partner(3L) // Partner set
                .build();

        // When
        boolean isValidWithoutPartner = personService.validatePerson(personWithoutPartner);
        boolean isValidWithPartner = personService.validatePerson(personWithPartner);

        // Then
        assertFalse(isValidWithoutPartner, "Person without partner should not be valid");
        assertTrue(isValidWithPartner, "Person with partner should be valid");
    }

    @Test
    void validateChildren() {
        // Given
        Person personWithValidChildren = Person.builder()
                .id(1L)
                .partner(2L)
                .children(new ArrayList<>(List.of(3L, 4L, 5L)))
                .build();

        Person personWithInvalidChildren = Person.builder()
                .id(2L)
                .partner(3L)
                .children(new ArrayList<>(List.of(6L, 7L)))
                .build();

        // When
        boolean isValidWithValidChildren = personService.validateChildren(personWithValidChildren);
        boolean isValidWithInvalidChildren = personService.validateChildren(personWithInvalidChildren);

        // Then
        assertTrue(isValidWithValidChildren, "Person with valid children should be valid");
        assertFalse(isValidWithInvalidChildren, "Person with invalid children should not be valid");
    }
}