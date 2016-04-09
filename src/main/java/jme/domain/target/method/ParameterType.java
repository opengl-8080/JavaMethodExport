package jme.domain.target.method;

import jme.domain.ValueObject;

public class ParameterType extends ValueObject<String> {
    public ParameterType(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return this.value.replaceAll("<[^>]+>", "");
    }
}
