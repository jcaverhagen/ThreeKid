package nl.janverhagen.threekid.service;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.domain.PersonIdentity;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.junit.jupiter.api.BeforeAll;
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
                .partner(PersonIdentity.builder().id(3L).build()) // Partner set
                .build();

        // When
        boolean isValidWithoutPartner = personService.validatePerson(personWithoutPartner);
        boolean isValidWithPartner = personService.validatePerson(personWithPartner);

        // Then
        assertFalse(isValidWithoutPartner, "Person without partner should not be valid");
        assertTrue(isValidWithPartner, "Person with partner should be valid");

    }
}