package jme.domain.target.type;

import jme.domain.target.method.TargetMethods;

public class TargetType {
    private final TypeName name;
    private final TargetMethods methods;

    public TargetType(TypeName name, TargetMethods methods) {
        if (name == null || methods == null) {
            throw new NullPointerException("name or methods is null. (name=" + name + ", methods=" + methods + ")");
        }
        this.name = name;
        this.methods = methods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetType that = (TargetType) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
