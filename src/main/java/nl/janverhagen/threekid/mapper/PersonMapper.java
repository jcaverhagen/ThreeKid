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
                .parentIds(flattenParentIds(personRequest.getParent1().getId(), personRequest.getParent2().getId()))
                .partner(personRequest.getPartner().getId())
                .children(flattenChildrenIds(personRequest.getChildren()))
                .build();
    }

    private List<Long> flattenParentIds(Long parent1, Long parent2) {
        List<Long> parentIds = new ArrayList<>();
        if (parent1 != null) {
            parentIds.add(parent1);
        }
        if (parent2 != null) {
            parentIds.add(parent2);
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
