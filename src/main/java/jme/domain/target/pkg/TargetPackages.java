package jme.domain.target.pkg;

import jme.domain.ListObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TargetPackages extends ListObject<TargetPackage> {

    public List<String> getPackageNames() {
        return this.list.stream().map(pkg -> pkg.getPackageName().toString()).collect(Collectors.toList());
    }

    public Optional<TargetPackage> find(PackageName packageName) {
        return this.list.stream().filter(pkg -> pkg.getPackageName().equals(packageName)).findFirst();
    }
}
