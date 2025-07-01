package nl.janverhagen.threekid.service;

import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FamilyMatcherService {

    private final PersonRepository personRepository;

    public List<Person> findMatchingPersons() {
        return personRepository.findAll()
                .stream()
                .filter(this::matchesPattern)
                .toList();
    }

    public boolean matchesPattern(Person person) {
        return validatePerson(person) && validateChildren(person);
    }

    boolean validatePerson(Person person) {
        return person.getPartner() != null;
    }

    boolean validateChildren(Person person) {
        if (person.getChildren() == null || person.getChildren().isEmpty()) return false;

        // Retrieve existing children from the repository if they exist
        List<Person> children = person.getChildren()
                .stream()
                .map(personRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // Validate that there are exactly three existing children
        if (children.size() != 3) return false;

        // Validate that each child has the person as a parent
        long parentId = person.getPartner();
        boolean allChildrenHavePersonAsParent = children.stream()
                .allMatch(child -> child.getParentIds().contains(parentId));
        if (!allChildrenHavePersonAsParent) return false;

        // Validate that at least one child is under 18 years old
        boolean hasChildUnder18 = children.stream()
                .anyMatch(child -> child.getBirthDate() != null && child.getAge() < 18);
        if (!hasChildUnder18) return false;

        return true;
    }

}
