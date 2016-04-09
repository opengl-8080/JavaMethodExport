package jme.domain.target.pkg;

import jme.domain.target.type.TargetType;
import jme.domain.target.type.TargetTypes;
import jme.domain.target.type.TypeName;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class TargetPackage {
    private final PackageName name;
    private TargetTypes types = new TargetTypes();

    public TargetPackage(PackageName name) {
        if (name == null) {
            throw new NullPointerException("name is null.");
        }
        this.name = name;
    }

    public void addAll(TargetTypes types) {
        this.types.addAll(types);
    }

    public List<String> getTypeNames() {
        return this.types.getTypeNames();
    }

    public Optional<TargetType> find(TypeName name) {
        return this.types.find(name);
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


    public boolean isRoot() {
        return this.name.isRoot();
    }

    public TargetTypes getTargetTypes() {
        return this.types;
    }

    public PackageName getPackageName() {
        return this.name;
    }

    public File resolveDir(File baseDir) {
        String pkg = this.getPackageName().toString();
        return new File(baseDir, pkg.replace(".", "/"));
    }
}
