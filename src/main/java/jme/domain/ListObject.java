package jme.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ListObject<T> {
    protected List<T> list = new ArrayList<>();

    public ListObject(List<T> list) {
        if (list == null) {
            throw new NullPointerException("list is null.");
        }
        if (list.contains(null)) {
            throw new IllegalArgumentException("list contains null.");
        }
        this.list = new ArrayList<>(list);
    }

    public void addAll(ListObject<T> listObject) {
        this.list.addAll(listObject.list);
    }

    public ListObject() {}

    public void add(T value) {
        this.list.add(value);
    }


    public int size() {
        return this.list.size();
    }

    public T first() {
        return this.list.get(0);
    }

    public void forEach(Consumer<T> consumer) {
        this.list.forEach(consumer);
    }
}
