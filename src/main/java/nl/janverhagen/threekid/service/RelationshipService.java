package nl.janverhagen.threekid.service;

import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RelationshipService {

    private final PersonRepository personRepository;

    public void ensureBidirectional(Person person) {
        fixChildsParentsIds(person);
        fixParentsChildrenIds(person);
        fixPartnerPartnerIds(person);
    }

    // First check if the child's parentIds contains the person id
    protected void fixChildsParentsIds(Person person) {
        for (Long child : person.getChildren()) {
            personRepository.findById(child).ifPresent(childItem -> {
                if(!childItem.getParentIds().contains(person.getId())) {
                    childItem.addParent(person.getId());
                    personRepository.saveOrUpdate(childItem);
                }
            });
        }
    }

    // Then check if the person's parentIds contains the child id of this person
    protected void fixParentsChildrenIds(Person person) {
        for (Long parent : person.getParentIds()) {
            personRepository.findById(parent).ifPresent(parentItem -> {
                if(!parentItem.getChildren().contains(person.getId())) {
                    parentItem.getChildren().add(person.getId());
                    personRepository.saveOrUpdate(parentItem);
                }
            });
        }
    }

    // Finally, check if the partner's partnerId contains the person id
    protected void fixPartnerPartnerIds(Person person) {
        if (person.getPartner() != null) {
            personRepository.findById(person.getPartner()).ifPresent(partnerItem -> {
                if(person.getId() != partnerItem.getId()) {
                    partnerItem.setPartner(person.getId());
                    personRepository.saveOrUpdate(partnerItem);
                }
            });
        }
    }

}
