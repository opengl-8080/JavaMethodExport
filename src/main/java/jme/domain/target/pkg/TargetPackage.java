package jme.domain.target.pkg;

import jme.domain.target.type.TargetTypes;

public class TargetPackage {
    private final PackageName name;
    private final TargetTypes types;

    public TargetPackage(PackageName name, TargetTypes types) {
        if (name == null || types == null) {
            throw new NullPointerException("name or types is null. (name=" + name + ", types=" + types + ")");
        }
        this.name = name;
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetPackage that = (TargetPackage) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
