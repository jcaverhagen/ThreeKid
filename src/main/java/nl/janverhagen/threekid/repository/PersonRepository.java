package nl.janverhagen.threekid.repository;

import nl.janverhagen.threekid.domain.Person;

import java.util.Collection;


public interface PersonRepository {

    void save(Person person);
    Person findById(Long id);
    void delete(Long id);
    Collection<Person> findAll();
}
