package jme.domain.target.method;

import jme.domain.ValueObject;

public class ParameterType extends ValueObject<Class<?>> {
    public ParameterType(Class<?> value) {
        super(value);
    }

    @Override
    public String toString() {
        return this.value.getSimpleName();
    }
}
