package jme.domain;

public abstract class ValueObject<T> {
    protected final T value;

    public ValueObject(T value) {
        if (value == null) {
            throw new NullPointerException("value is null.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueObject<?> that = (ValueObject<?>) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
