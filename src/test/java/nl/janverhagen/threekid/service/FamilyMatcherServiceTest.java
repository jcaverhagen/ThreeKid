package nl.janverhagen.threekid.service;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FamilyMatcherServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private FamilyMatcherService familyMatcherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validatePerson_returnsTrueWhenPartnerPresent() {
        // given
        Person person = Person.builder()
                .id(1L)
                .partner(2L)
                .build();

        // when

        // then
        assertTrue(familyMatcherService.validatePerson(person),
                "Should return true when person has a partner");
    }

    @Test
    void validatePerson_returnsFalseWhenNoPartner() {
        // given
        Person person = Person.builder()
                .id(1L)
                .partner(null)
                .build();

        // when

        // then
        assertFalse(familyMatcherService.validatePerson(person),
                "Should return false when person has no partner");
    }

    @Test
    void validateChildren_returnsFalseWhenNoChildren() {
        // given
        Person person = Person.builder()
                .id(1L)
                .partner(2L)
                .children(List.of())
                .build();

        // when

        // then
        assertFalse(familyMatcherService.validateChildren(person));
    }

    @Test
    void validateChildren_returnsFalseWhenNotThreeChildren() {
        // given
        Person person = Person.builder()
                .id(1L)
                .partner(2L)
                .children(List.of(3L))
                .build();

        // when
        when(personRepository.findById(3L))
                .thenReturn(Optional.of(Person.builder().id(3L).build()));

        // then
        assertFalse(familyMatcherService.validateChildren(person));
    }

    @Test
    void validateChildren_returnsFalseWhenPartnerNotParentOfAllChildren() {
        // given
        Long partnerId = 2L;
        Person person = Person.builder()
                .id(1L)
                .partner(partnerId)
                .children(List.of(3L, 4L, 5L))
                .build();

        // when
        when(personRepository.findById(3L)).thenReturn(Optional.of(Person.builder().id(3L).parentIds(List.of(partnerId)).build()));
        when(personRepository.findById(4L)).thenReturn(Optional.of(Person.builder().id(4L).parentIds(List.of(partnerId)).build()));
        when(personRepository.findById(5L)).thenReturn(Optional.of(Person.builder().id(5L).parentIds(List.of(999L)).build())); // wrong parent


        // then
        assertFalse(familyMatcherService.validateChildren(person));
    }

    @Test
    void validateChildren_returnsFalseWhenNoChildUnder18() {
        // given
        Long partnerId = 2L;
        Person person = Person.builder()
                .id(1L)
                .partner(partnerId)
                .children(List.of(3L, 4L, 5L))
                .build();

        // when

        when(personRepository.findById(3L)).thenReturn(Optional.of(Person.builder().id(3L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(20)).build()));
        when(personRepository.findById(4L)).thenReturn(Optional.of(Person.builder().id(4L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(19)).build()));
        when(personRepository.findById(5L)).thenReturn(Optional.of(Person.builder().id(5L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(25)).build()));

        // then
        assertFalse(familyMatcherService.validateChildren(person));
    }

    @Test
    void validateChildren_returnsTrueWhenAllConditionsMet() {
        // given
        Long partnerId = 2L;
        Person person = Person.builder()
                .id(1L)
                .partner(partnerId)
                .children(List.of(3L, 4L, 5L))

                .build();

        // when
        when(personRepository.findById(3L)).thenReturn(Optional.of(Person.builder().id(3L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(10)).build()));
        when(personRepository.findById(4L)).thenReturn(Optional.of(Person.builder().id(4L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(19)).build()));
        when(personRepository.findById(5L)).thenReturn(Optional.of(Person.builder().id(5L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(25)).build()));

        // then
        assertTrue(familyMatcherService.validateChildren(person));
    }

    @Test
    void matchesPattern_returnsTrueWhenPersonAndChildrenValid() {
        // given
        // reuse valid person + children from previous test
        Long partnerId = 2L;
        Person person = Person.builder()
                .id(1L)
                .partner(partnerId)
                .children(List.of(3L, 4L, 5L))
                .build();

        // when
        when(personRepository.findById(3L)).thenReturn(Optional.of(Person.builder().id(3L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(10)).build()));
        when(personRepository.findById(4L)).thenReturn(Optional.of(Person.builder().id(4L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(19)).build()));
        when(personRepository.findById(5L)).thenReturn(Optional.of(Person.builder().id(5L).parentIds(List.of(partnerId)).birthDate(LocalDate.now().minusYears(25)).build()));

        // then
        assertTrue(familyMatcherService.matchesPattern(person));
    }
}