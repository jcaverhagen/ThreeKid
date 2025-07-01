package nl.janverhagen.threekid.repository;

import nl.janverhagen.threekid.domain.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository for managing Person entities.
 * Normally this repository will write data to a database or other persistent storage.
 * in this case it is a stub for the purpose of this challenge.
 * Using a simple hashmap to simulate the storage.
 */
public class InMemoryPersonRepository implements PersonRepository {

    private final Map<Long, Person> fakeDatabase = new HashMap<>();

    @Override
    public void save(Person person) {
        fakeDatabase.put(person.getId(), person);
    }

    @Override
    public Person findById(Long id) {
        return fakeDatabase.get(id);
    }

    @Override
    public void delete(Long id) {
        fakeDatabase.remove(id);
    }

    @Override
    public Collection<Person> findAll() {
        return fakeDatabase.values();
    }
}
