package jme.domain.target.method;

public class MethodSignature {
    private final MethodName name;
    private final MethodParameters parameters;

    public MethodSignature(MethodName name, MethodParameters parameters) {
        if (name == null || parameters == null) {
            throw new NullPointerException("name or parameters is null. (name=" + name + ", parameters=" + parameters + ")");
        }
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.parameters + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodSignature that = (MethodSignature) o;

        if (!name.equals(that.name)) return false;
        return parameters.equals(that.parameters);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }
}
