package nl.janverhagen.threekid.service;

import lombok.AllArgsConstructor;
import nl.janverhagen.threekid.domain.Person;
import nl.janverhagen.threekid.dto.PersonRequest;
import nl.janverhagen.threekid.mapper.PersonMapper;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public void savePerson(PersonRequest personRequest) {
        personRepository.save(personMapper.map(personRequest));
    }

    public long findPersonById(Long id) {
        Person person = personRepository.findById(id);
        if(person == null) {
            return -1;
        }
        return person.getId();
    }
}
