package nl.janverhagen.threekid.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ArrayList;


@Getter
@SuperBuilder
public class Person extends PersonIdentity {
    private final String name;
    private final LocalDate birthDate;
    private final PersonIdentity parent1;
    private final PersonIdentity parent2;
    private final PersonIdentity partner;
    private final ArrayList<PersonIdentity> children;

    public List<Long> getParentIds() {
        return List.of(
            parent1 != null ? parent1.getId() : null,
            parent2 != null ? parent2.getId() : null
        );
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
