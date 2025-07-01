package nl.janverhagen.threekid.repository;

import nl.janverhagen.threekid.domain.Person;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for managing Person entities.
 * Normally this repository will write data to a database or other persistent storage.
 * in this case it is a stub for the purpose of this challenge.
 * Using a simple hashmap to simulate the storage.
 */

@Repository
public class InMemoryPersonRepository implements PersonRepository {

    private final Map<Long, Person> fakeDatabase = new ConcurrentHashMap<>();

    @Override
    public void saveOrUpdate(Person person) {
        fakeDatabase.put(person.getId(), person);
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(fakeDatabase.get(id));
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
