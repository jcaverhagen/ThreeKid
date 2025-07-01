package nl.janverhagen.threekid.service;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.domain.PersonIdentity;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RelationshipServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private RelationshipService relationshipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ensureBidirectional_childNotFoundOfPersonDoNothing() {
        // given
        Person parent = Person.builder()
                .id(1L)
                .name("Parent")
                .children(List.of(PersonIdentity.builder().id(2L).build())) // Initially no children
                .build();

        Person child = Person.builder()
                .id(2L)
                .name("Child")
                .parentIds(new ArrayList<>()) // Initially no parents
                .build();

        when(personRepository.findById(2L)).thenReturn(Optional.empty());

        // when
        relationshipService.fixChildsParentsIds(parent);

        // then
        verify(personRepository, never()).saveOrUpdate(any());
    }

    @Test
    void updatesChildWithParentIfChildExists() {
        // given
        Person parent = Person.builder()
                .id(1L)
                .name("Parent")
                .children(List.of(PersonIdentity.builder().id(2L).build()))
                .build();

        Person child = Person.builder()
                .id(2L)
                .name("Child")
                .parentIds(new ArrayList<>()) // no parent yet
                .build();

        when(personRepository.findById(2L)).thenReturn(Optional.of(child));

        // when
        relationshipService.fixChildsParentsIds(parent);

        // then
        // Capture what was saved
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).saveOrUpdate(captor.capture());
        Person updatedChild = captor.getValue();

        // Assert parent ID was added
        assertTrue(updatedChild.getParentIds().contains(PersonIdentity.builder().id(1L).build()),
                "Child should have the parent ID added to its parentIds");
    }

    @Test
    void updatesParentsChildrenIds() {
        // given
        Person person = Person.builder()
                .id(10L)
                .name("Child")
                .parentIds(List.of(PersonIdentity.builder().id(20L).build()))
                .build();

        Person parent = Person.builder()
                .id(20L)
                .name("Parent")
                .children(new ArrayList<>()) // no children yet
                .build();

        when(personRepository.findById(20L)).thenReturn(Optional.of(parent));

        // when
        relationshipService.fixParentsChildrenIds(person);

        // then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).saveOrUpdate(captor.capture());
        Person updatedParent = captor.getValue();

        assertTrue(updatedParent.getChildren().stream()
                        .anyMatch(child -> child.getId() == 10L),
                "Parent should have the child added to its children list");
    }

    @Test
    void updatesPartnerWhenPartnerExistsAndMissingReference() {
        // given
        Person person = Person.builder()
                .id(100L)
                .name("Person A")
                .partner(PersonIdentity.builder().id(200L).build())
                .build();

        Person partner = Person.builder()
                .id(200L)
                .name("Person B")
                .partner(null) // B does not yet list A as partner
                .build();

        when(personRepository.findById(200L)).thenReturn(Optional.of(partner));

        // when
        relationshipService.fixPartnerPartnerIds(person);

        // then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).saveOrUpdate(captor.capture());
        Person updatedPartner = captor.getValue();

        assertEquals(100L, updatedPartner.getPartner().getId(),
                "Partner should now list the person as their partner");
    }

    @Test
    void ensureBidirectional_updatesAllRelationshipsIfNeeded() {
        // given
        Person person = Person.builder()
                .id(1L)
                .name("Person A")
                .children(List.of(PersonIdentity.builder().id(2L).build()))
                .parentIds(List.of(PersonIdentity.builder().id(3L).build()))
                .partner(PersonIdentity.builder().id(4L).build())
                .build();

        // Child (2) missing parent reference
        Person child = Person.builder()
                .id(2L)
                .name("Child")
                .parentIds(new ArrayList<>())
                .build();

        // Parent (3) missing child reference
        Person parent = Person.builder()
                .id(3L)
                .name("Parent")
                .children(new ArrayList<>())
                .build();

        // Partner (4) missing partner reference
        Person partner = Person.builder()
                .id(4L)
                .name("Partner")
                .partner(null)
                .build();

        when(personRepository.findById(2L)).thenReturn(Optional.of(child));
        when(personRepository.findById(3L)).thenReturn(Optional.of(parent));
        when(personRepository.findById(4L)).thenReturn(Optional.of(partner));

        // when
        relationshipService.ensureBidirectional(person);

        // then
        // Capture and verify all saveOrUpdate calls
        verify(personRepository, times(3)).saveOrUpdate(any());

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository, times(3)).saveOrUpdate(captor.capture());

        List<Person> updated = captor.getAllValues();

        // Child should have parent ID 1
        assertTrue(
                updated.stream().anyMatch(p ->
                        p.getId() == 2L && p.getParentIds().contains(PersonIdentity.builder().id(1L).build())),
                "Child should have Person A as parent"
        );

        // Parent should have child ID 1
        assertTrue(
                updated.stream().anyMatch(p ->
                        p.getId() == 3L && p.getChildren().stream()
                                .anyMatch(c -> c.getId() == 1L)),
                "Parent should have Person A as child"
        );

        // Partner should have partner ID 1
        assertTrue(
                updated.stream().anyMatch(p ->
                        p.getId() == 4L && p.getPartner() != null &&
                                p.getPartner().getId() == 1L),
                "Partner should have Person A as partner"
        );
    }

}