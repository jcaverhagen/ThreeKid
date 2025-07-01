package nl.janverhagen.threekid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@ToString(callSuper = true)
public class PersonRequest extends PersonIdentityRequest {

    private final String name;
    private final String birthDate;
    private final PersonIdentityRequest parent1;
    private final PersonIdentityRequest parent2;
    private final PersonIdentityRequest partner;
    private final ArrayList<PersonIdentityRequest> children;

}
