package jme.domain.target.type;

import jme.domain.ListObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TargetTypes extends ListObject<TargetType> {

    public List<String> getTypeNames() {
        return this.list.stream()
                .map(type -> type.getName().toString())
                .collect(Collectors.toList());
    }

    public Optional<TargetType> find(TypeName name) {
        if (name == null) {
            throw new NullPointerException("name is null.");
        }

        return this.list.stream().filter(type -> type.getName().equals(name)).findFirst();
    }
}
