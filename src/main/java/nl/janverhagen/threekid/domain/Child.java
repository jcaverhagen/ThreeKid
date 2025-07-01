package nl.janverhagen.threekid.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@SuperBuilder
public class Child extends Person {
    public void addParent(PersonIdentity parent) {
        if (getParentIds() == null) {
            setParentIds(new ArrayList<>());
        }
        getParentIds().add(parent);
    }
}
