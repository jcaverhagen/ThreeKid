package nl.janverhagen.threekid.mapper;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.domain.PersonIdentity;
import nl.janverhagen.threekid.dto.PersonIdentityRequest;
import nl.janverhagen.threekid.dto.PersonRequest;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonMapper {

    public Person map(PersonRequest personRequest) {
        return Person.builder()
                .id(personRequest.getId())
                .name(personRequest.getName())
                .birthDate(personRequest.getBirthDate())
                .parentIds(flattenParentIds(personRequest.getParent1(), personRequest.getParent2()))
                .partner(toPersonIdentity(personRequest.getPartner()))
                .children(toPersonIdentityList(personRequest.getChildren()))
                .build();
    }

    private List<PersonIdentity> flattenParentIds(PersonIdentityRequest parent1, PersonIdentityRequest parent2) {
        List<PersonIdentity> parentIds = new ArrayList<>();
        if (parent1 != null) {
            parentIds.add(new PersonIdentity(parent1.getId()));
        }
        if (parent2 != null) {
            parentIds.add(new PersonIdentity(parent2.getId()));
        }
        return parentIds;
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
