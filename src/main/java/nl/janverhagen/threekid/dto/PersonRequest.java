package nl.janverhagen.threekid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
@ToString
public class PersonRequest {

    private final Long id;
    private final String name;
    private final LocalDate birthDate;
    private final PersonIdentityRequest parent1;
    private final PersonIdentityRequest parent2;
    private final PersonIdentityRequest partner;
    private final ArrayList<PersonIdentityRequest> children;

}
