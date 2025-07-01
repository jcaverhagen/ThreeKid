package nl.janverhagen.threekid.service;

import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.domain.PersonIdentity;
import nl.janverhagen.threekid.dto.PersonRequest;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public void savePerson(PersonRequest personRequest) {
        Person person = personMapper.map(personRequest);
        personRepository.saveOrUpdate(person);
    }

    boolean validatePerson(Person person) {
        return person.getPartner() != null;
    }

    boolean validateChildren(Person person) {
        if (person.getChildren() == null || person.getChildren().isEmpty()) return false;

        // Retrieve existing children from the repository if they exist, otherwise create new Person objects with just the ID
        List<Person> children = person.getChildren()
                .stream()
                .map(child -> {
                    Optional<Person> existingChild = personRepository.findById(child.getId());
                    return existingChild.orElseGet(() -> Person.builder().id(child.getId()).build());
                })
                .collect(Collectors.toList());

        // Validate that there are exactly three children
        if (children.size() != 3) return false;

        // Validate that each child has the partner as a parent
        long parentId = person.getPartner().getId();
        boolean allChildrenHavePartnerAsParent = children.stream()
                .allMatch(child -> child.getParentIds().contains(parentId));
        if (!allChildrenHavePartnerAsParent) return false;

        // Validate that at least one child is under 18 years old
        boolean hasChildUnder18 = children.stream()
                .anyMatch(child -> child.getBirthDate() != null && child.getAge() < 18);
        if (!hasChildUnder18) return false;

        return true;
    }

    public long findPersonById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.map(PersonIdentity::getId).orElse(-1L);
    }
}
