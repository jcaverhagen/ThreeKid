package nl.janverhagen.threekid.service;

import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void savePerson(Person person) {
        personRepository.saveOrUpdate(person);
    }

    public long findPersonById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.map(Person::getId).orElse(-1L);
    }
}
