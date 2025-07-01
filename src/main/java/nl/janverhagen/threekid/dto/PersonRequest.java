package nl.janverhagen.threekid.dto;

import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class PersonRequest extends PersonIdentity {

    private final String name;
    private final String birthDate;
    private final PersonIdentity parent1;
    private final PersonIdentity parent2;
    private final PersonIdentity partner;
    private final ArrayList<PersonIdentity> Children;

}
