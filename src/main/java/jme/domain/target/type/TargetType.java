package jme.domain.target.type;

import jme.domain.target.method.TargetMethod;
import jme.domain.target.method.TargetMethods;

import java.io.File;

public class TargetType {
    private final TypeName name;
    private final TargetMethods methods = new TargetMethods();

    public TargetType(TypeName name) {
        if (name == null) {
            throw new NullPointerException("name is null.");
        }
        this.name = name;
    }

    public void add(TargetMethod method) {
        if (method == null) {
            throw new NullPointerException("method is null.");
        }
        this.methods.add(method);
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

    public TypeName getName() {
        return name;
    }

    public TargetMethods getMethods() {
        return methods;
    }

    public File resolveDir(File baseDir) {
        return new File(baseDir, this.name.toString());
    }
}
