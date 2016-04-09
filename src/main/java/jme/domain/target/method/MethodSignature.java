package jme.domain.target.method;

public class MethodSignature {
    private final MethodName name;
    private final ParameterTypes types;

    public MethodSignature(MethodName name, ParameterTypes types) {
        if (name == null || types == null) {
            throw new NullPointerException("name or types is null. (name=" + name + ", types=" + types + ")");
        }
        this.name = name;
        this.types = types;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.types + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodSignature that = (MethodSignature) o;

        if (!name.equals(that.name)) return false;
        return types.equals(that.types);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + types.hashCode();
        return result;
    }
}
