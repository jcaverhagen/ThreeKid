package nl.janverhagen.threekid.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/***
 * Represents a person in the system.
 * Contains personal details such as name, birthdate, and relationships with other persons.
 */

@Getter
@Setter
@SuperBuilder
public class Person {
    private final long id;
    private final String name;
    private final LocalDate birthDate;
    private List<Long> parentIds;
    private Long partner;
    private List<Long> children;

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public void addParent(Long parent) {
        if (getParentIds() == null) {
            setParentIds(new ArrayList<>());
        }
        getParentIds().add(parent);
    }
}
