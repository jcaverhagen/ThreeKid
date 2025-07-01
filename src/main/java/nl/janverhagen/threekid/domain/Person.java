package nl.janverhagen.threekid.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;


@Getter
@SuperBuilder
public class Person extends PersonIdentity {
    private final String name;
    private final String birthDate;
    private final PersonIdentity parent1;
    private final PersonIdentity parent2;
    private final PersonIdentity partner;
    private final ArrayList<PersonIdentity> Children;
}
