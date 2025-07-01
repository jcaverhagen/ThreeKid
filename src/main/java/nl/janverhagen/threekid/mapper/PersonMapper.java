package nl.janverhagen.threekid.mapper;

import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.dto.PersonIdentityRequest;
import nl.janverhagen.threekid.dto.PersonRequest;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonMapper {

    public Person map(PersonRequest personRequest) {
        return Person.builder()
                .id(personRequest.getId())
                .name(personRequest.getName())
                .birthDate(personRequest.getBirthDate())
                .parentIds(flattenParentIds(personRequest.getParent1(), personRequest.getParent2()))
                .partner((personRequest.getPartner() != null ? personRequest.getPartner().getId() : null))
                .children(flattenChildrenIds(personRequest.getChildren()))
                .build();
    }

    private List<Long> flattenParentIds(PersonIdentityRequest parent1, PersonIdentityRequest parent2) {
        List<Long> parentIds = new ArrayList<>();
        if (parent1 != null) {
            parentIds.add(parent1.getId());
        }
        if (parent2 != null) {
            parentIds.add(parent2.getId());
        }
        return parentIds;
    }

    private List<Long> flattenChildrenIds(List<PersonIdentityRequest> children) {
        List<Long> childrenIds = new ArrayList<>();
        if (children != null) {
            for (PersonIdentityRequest child : children) {
                childrenIds.add(child.getId());
            }
        }
        return childrenIds;
    }
}
