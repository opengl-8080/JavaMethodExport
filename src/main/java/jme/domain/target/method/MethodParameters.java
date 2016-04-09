package jme.domain.target.method;

import jme.domain.ListObject;

import java.util.List;
import java.util.stream.Collectors;

public class MethodParameters extends ListObject<MethodParameter> {

    public MethodParameters(List<MethodParameter> list) {
        super(list);
    }

    @Override
    public String toString() {
        return this.list
                .stream()
                .map(MethodParameter::toStringOnlyType)
                .collect(Collectors.joining("_"));
    }
}
