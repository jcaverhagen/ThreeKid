package nl.janverhagen.threekid.repository;

import nl.janverhagen.threekid.domain.Person;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private final Set<Long> ignoreIds = ConcurrentHashMap.newKeySet();

    @Override
    public void saveOrUpdate(Person person) {
        if (isIgnored(person.getId())) {
            return;
        }
        fakeDatabase.put(person.getId(), person);
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(fakeDatabase.get(id));
    }

    @Override
    public void deleteAndIgnore(Long id) {
        fakeDatabase.remove(id);
        ignoreIds.add(id);
    }

    @Override
    public boolean isIgnored(Long id) {
        return ignoreIds.contains(id);
    }

    @Override
    public Collection<Person> findAll() {
        return fakeDatabase.values();
    }

    @Override
    public void clear() {
        fakeDatabase.clear();
    }
}
