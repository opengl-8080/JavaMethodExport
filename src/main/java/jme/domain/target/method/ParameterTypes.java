package jme.domain.target.method;

import jme.domain.ListObject;

import java.util.List;
import java.util.stream.Collectors;

public class ParameterTypes extends ListObject<ParameterType> {

    public ParameterTypes(List<ParameterType> list) {
        super(list);
    }

    @Override
    public String toString() {
        return this.list.stream().map(ParameterType::toString).collect(Collectors.joining("_"));
    }
}
