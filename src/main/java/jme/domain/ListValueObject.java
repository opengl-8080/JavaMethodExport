package jme.domain;

import java.util.ArrayList;
import java.util.List;

public class ListValueObject<T> {
    protected final List<T> list;

    public ListValueObject(List<T> list) {
        if (list == null) {
            throw new NullPointerException("list is null.");
        }
        if (list.contains(null)) {
            throw new IllegalArgumentException("list contains null.");
        }
        this.list = new ArrayList<>(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListValueObject<?> that = (ListValueObject<?>) o;

        return list.equals(that.list);

    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
