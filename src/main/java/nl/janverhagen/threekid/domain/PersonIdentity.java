package nl.janverhagen.threekid.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PersonIdentity {
    private long id;
}
