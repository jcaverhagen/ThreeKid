package nl.janverhagen.threekid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@AllArgsConstructor
@ToString(callSuper = true)
public class PersonRequest extends PersonIdentity {

    private final String name;
    private final String birthDate;
    private final PersonIdentity parent1;
    private final PersonIdentity parent2;
    private final PersonIdentity partner;
    private final ArrayList<PersonIdentity> Children;

}
