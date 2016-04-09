package jme.infrastructure.input;

import jme.domain.target.pkg.PackageName;

import java.util.Stack;
import java.util.stream.Collectors;

public class PackageNameStack {
    private Stack<String> packageNames = new Stack<>();

    public void push(String name) {
        this.packageNames.push(name);
    }

    public void pop() {
        this.packageNames.pop();
    }

    public PackageName toPackageName() {
        String name = this.packageNames.stream().collect(Collectors.joining("."));
        return new PackageName(name);
    }
}
