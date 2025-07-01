package nl.janverhagen.threekid.service;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePerson_delegatesToRepository() {
        // given
        Person person = Person.builder().id(1L).name("Ada").build();

        // when
        personService.savePerson(person);

        // then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).saveOrUpdate(captor.capture());
        assertEquals(1L, captor.getValue().getId(), "Person ID should match");
        assertEquals("Ada", captor.getValue().getName(), "Person name should match");
    }

    @Test
    void findPersonById_returnsIdWhenPersonExists() {
        // given
        Person person = Person.builder().id(42L).build();

        // when
        when(personRepository.findById(42L)).thenReturn(Optional.of(person));

        long result = personService.findPersonById(42L);

        // then
        assertEquals(42L, result, "Should return person ID when found");
    }

    @Test
    void findPersonById_returnsMinus1WhenPersonNotFound() {
        // given
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        // when
        long result = personService.findPersonById(99L);

        // then
        assertEquals(-1L, result, "Should return -1 when person not found");
    }
}