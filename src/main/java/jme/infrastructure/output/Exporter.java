package jme.infrastructure.output;

import jme.domain.target.method.TargetMethod;
import jme.domain.target.pkg.TargetPackage;
import jme.domain.target.pkg.TargetPackages;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Exporter {

    private final File rootDir;

    public Exporter(File rootDir) {
        if (rootDir == null) {
            throw new NullPointerException("rootDir is null.");
        }
        this.rootDir = rootDir;
    }

    public void export(TargetPackages packages) {
        packages.forEach(pkg -> {
            File pkgDir = pkg.isRoot() ? this.rootDir : pkg.resolveDir(this.rootDir);
            pkgDir.mkdirs();

            this.exportClassInfo(pkg, pkgDir);
        });
    }

    private void exportClassInfo(TargetPackage pkg, File packageDir) {
        pkg.getTargetTypes().forEach(type -> {
            File classFileDir = type.resolveDir(packageDir);
            classFileDir.mkdirs();

            type.getMethods().forEach(method -> {
                this.exportMethodInfo(method, classFileDir);
            });
        });
    }

    private void exportMethodInfo(TargetMethod method, File classFileDir) {
        String methodFileName = method.getSignature().toString() + ".java";
        File methodFile = new File(classFileDir, methodFileName);

        try {
            Files.write(methodFile.toPath(), method.getBody().toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
