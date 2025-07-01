package nl.janverhagen.threekid.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ArrayList;


@Getter
@Setter
@SuperBuilder
public class Person extends PersonIdentity {
    private final String name;
    private final LocalDate birthDate;
    private List<PersonIdentity> parentIds;
    private PersonIdentity partner;
    private ArrayList<PersonIdentity> children;

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
