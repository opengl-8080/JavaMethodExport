package jme.domain.target.method;

public class MethodParameter {
    private final ParameterType type;
    private final ParameterName name;

    public MethodParameter(ParameterType type, ParameterName name) {
        if (type == null || name == null) {
            throw new NullPointerException("type or name is null. (type=" + type + ", name=" + name + ")");
        }
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.toString(" ");
    }

    public String toString(String separator) {
        return this.type + separator + this.name;
    }

    public String toStringOnlyType() {
        return this.type.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodParameter that = (MethodParameter) o;

        if (!type.equals(that.type)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
