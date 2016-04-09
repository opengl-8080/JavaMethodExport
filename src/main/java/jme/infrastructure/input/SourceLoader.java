package jme.infrastructure.input;

import jme.domain.target.pkg.TargetPackages;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceLoader {

    private Path baseDir;

    public void setBaseDir(Path baseDir) {
        if (baseDir == null) {
            throw new NullPointerException("rootDir is null.");
        }
        this.baseDir = baseDir;
    }

    public TargetPackages load() {
        FileVisitor visitor = new FileVisitor(this.baseDir);
        try {
            Files.walkFileTree(this.baseDir, visitor);
            return visitor.getPackages();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
