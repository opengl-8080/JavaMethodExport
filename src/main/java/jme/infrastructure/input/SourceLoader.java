package jme.infrastructure.input;

import jme.infrastructure.output.Exporter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceLoader {

    private Path baseDir;
    private Exporter exporter;

    public void setBaseDir(Path baseDir) {
        if (baseDir == null) {
            throw new NullPointerException("rootDir is null.");
        }
        this.baseDir = baseDir;
    }

    public void load() {
        FileVisitor visitor = new FileVisitor(this.baseDir, this.exporter);
        try {
            Files.walkFileTree(this.baseDir, visitor);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }
}
