package nl.janverhagen.threekid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a request to identify a person in the system by their ID.
 * This is typically used for operations that require referencing an existing person.
 */
@AllArgsConstructor
@Getter
@ToString
public class PersonIdentityRequest {
    private long id;
}
