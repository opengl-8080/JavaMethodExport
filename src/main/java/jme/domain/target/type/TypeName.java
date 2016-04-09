package jme.domain.target.type;

import jme.domain.ValueObject;

public class TypeName extends ValueObject<Class<?>> {

    public TypeName(Class<?> value) {
        super(value);
    }

    @Override
    public String toString() {
        if (this.value.isMemberClass()) {
            String name = this.value.getName();
            int lastPeriodIndex = name.lastIndexOf(".");
            return name.substring(lastPeriodIndex + 1);
        }

        return this.value.getSimpleName();
    }
}
