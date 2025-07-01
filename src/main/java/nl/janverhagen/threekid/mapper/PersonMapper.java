package nl.janverhagen.threekid.mapper;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.domain.PersonIdentity;
import nl.janverhagen.threekid.dto.PersonIdentityRequest;
import nl.janverhagen.threekid.dto.PersonRequest;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class PersonMapper {

    public Person map(PersonRequest personRequest) {
        return Person.builder()
                .id(personRequest.getId())
                .name(personRequest.getName())
                .birthDate(personRequest.getBirthDate())
                .parent1(toPersonIdentity(personRequest.getParent1()))
                .parent2(toPersonIdentity(personRequest.getParent2()))
                .partner(toPersonIdentity(personRequest.getPartner()))
                .Children(toPersonIdentityList(personRequest.getChildren()))
                .build();
    }

    private PersonIdentity toPersonIdentity(PersonIdentityRequest personIdentityRequest) {
        return personIdentityRequest != null ? new PersonIdentity(personIdentityRequest.getId()) : null;
    }

    private ArrayList<PersonIdentity> toPersonIdentityList(ArrayList<PersonIdentityRequest> children) {
        return children != null
                ? children.stream()
                .map(child -> new PersonIdentity(child.getId()))
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();
    }
}
