package nl.janverhagen.threekid.repository;

import nl.janverhagen.threekid.domain.Person;

import java.util.Collection;
import java.util.Optional;


public interface PersonRepository {

    void saveOrUpdate(Person person);
    Optional<Person> findById(Long id);
    void delete(Long id);
    Collection<Person> findAll();

    void clear();
}
