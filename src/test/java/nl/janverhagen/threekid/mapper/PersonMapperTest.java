package nl.janverhagen.threekid.mapper;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.dto.PersonIdentityRequest;
import nl.janverhagen.threekid.dto.PersonRequest;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonMapperTest {

    private final PersonMapper personMapper = new PersonMapper();

    @Test
    void map_correctlyMapsPersonRequestToPerson() {
        // given
        PersonRequest request = new PersonRequest(
                42L,
                "Ada Lovelace",
                LocalDate.of(1815, 12, 10),
                new PersonIdentityRequest(1L),
                new PersonIdentityRequest(2L),
                new PersonIdentityRequest(99L),
                List.of(
                        new PersonIdentityRequest(77L),
                        new PersonIdentityRequest(78L)
                )
        );

        // when
        Person person = personMapper.map(request);

        // then
        assertEquals(42L, person.getId());
        assertEquals("Ada Lovelace", person.getName());
        assertEquals(LocalDate.of(1815, 12, 10), person.getBirthDate());

        assertTrue(person.getParentIds().contains(1L), "Should contain parent1 id");
        assertTrue(person.getParentIds().contains(2L), "Should contain parent2 id");
        assertEquals(2, person.getParentIds().size(), "Should have exactly 2 parent ids");

        assertEquals(99L, person.getPartner(), "Should have correct partner id");

        assertTrue(person.getChildren().contains(77L), "Should contain child 77");
        assertTrue(person.getChildren().contains(78L), "Should contain child 78");
        assertEquals(2, person.getChildren().size(), "Should have exactly 2 children ids");
    }

    @Test
    void map_handlesNullParentsAndChildrenGracefully() {
        // given
        PersonRequest request = new PersonRequest(
                42L,
                "Ada Lovelace",
                LocalDate.of(1815, 12, 10),
                null,  // no parent1
                null,  // no parent2
                null,  // no partner
                null   // no children
        );

        // when
        Person person = personMapper.map(request);

        // then
        assertEquals(42L, person.getId());
        assertEquals("Ada Lovelace", person.getName());
        assertEquals(LocalDate.of(1815, 12, 10), person.getBirthDate());

        assertNotNull(person.getParentIds());
        assertTrue(person.getParentIds().isEmpty(), "Parent ids should be empty");

        assertNull(person.getPartner(), "Partner should be null");

        assertNotNull(person.getChildren());
        assertTrue(person.getChildren().isEmpty(), "Children ids should be empty");
    }
}