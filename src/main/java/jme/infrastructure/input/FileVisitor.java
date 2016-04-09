package jme.infrastructure.input;

import jme.domain.target.pkg.PackageName;
import jme.domain.target.pkg.TargetPackage;
import jme.infrastructure.output.Exporter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Stack;

public class FileVisitor extends SimpleFileVisitor<Path> {
    private final Path rootDir;
    private final Exporter exporter;
    private Stack<TargetPackage> currentPackageStack = new Stack<>();
    private PackageNameStack packageNameStack = new PackageNameStack();

    public FileVisitor(Path rootDir, Exporter exporter) {
        this.rootDir = rootDir;
        this.exporter = exporter;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (!dir.equals(this.rootDir)) {
            this.packageNameStack.push(dir.toFile().getName());
        }

        PackageName packageName = this.packageNameStack.toPackageName();
        this.currentPackageStack.push(new TargetPackage(packageName));

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String src = this.toString(file);

        ASTNodeVisitor nodeVisitor = this.parse(src);

        this.currentPackageStack.peek().addAll(nodeVisitor.getTypes());

        return FileVisitResult.CONTINUE;
    }

    private String toString(Path file) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Files.copy(file, out);

        return new String(out.toByteArray());
    }

    private ASTNodeVisitor parse(String src) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setSource(src.toCharArray());
        CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
        ASTNodeVisitor nodeVisitor = new ASTNodeVisitor();
        unit.accept(nodeVisitor);

        return nodeVisitor;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (!dir.equals(this.rootDir)) {
            this.packageNameStack.pop();
        }
        TargetPackage pkg = this.currentPackageStack.pop();

        this.exporter.exportClassInfo(pkg, dir.toFile());

        return FileVisitResult.CONTINUE;
    }
}
