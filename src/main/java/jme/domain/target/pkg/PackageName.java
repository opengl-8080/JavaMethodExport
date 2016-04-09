package jme.domain.target.pkg;

import jme.domain.ValueObject;

public class PackageName extends ValueObject<String> {
    public static final PackageName ROOT = new PackageName("");
    private boolean empty;

    public PackageName(String value) {
        super(value);
    }

    public boolean isRoot() {
        return this.value.isEmpty();
    }
}
