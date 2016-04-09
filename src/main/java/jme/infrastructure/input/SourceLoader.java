package jme.infrastructure.input;

import jme.domain.target.method.MethodBody;
import jme.domain.target.method.MethodName;
import jme.domain.target.method.MethodParameter;
import jme.domain.target.method.MethodParameters;
import jme.domain.target.method.MethodSignature;
import jme.domain.target.method.ParameterName;
import jme.domain.target.method.ParameterType;
import jme.domain.target.method.TargetMethod;
import jme.domain.target.pkg.PackageName;
import jme.domain.target.pkg.TargetPackage;
import jme.domain.target.pkg.TargetPackages;
import jme.domain.target.type.TargetType;
import jme.domain.target.type.TargetTypes;
import jme.domain.target.type.TypeName;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class SourceLoader {

    private Path baseDir;

    public void setBaseDir(Path baseDir) {
        if (baseDir == null) {
            throw new NullPointerException("rootDir is null.");
        }
        this.baseDir = baseDir;
    }

    public TargetPackages load() {
        MyFileVisitor visitor = new MyFileVisitor(this.baseDir);
        try {
            Files.walkFileTree(this.baseDir, visitor);
            return visitor.packages;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static class MyFileVisitor extends SimpleFileVisitor<Path> {
        private TargetPackages packages = new TargetPackages();
        private Stack<TargetPackage> currentPackageStack = new Stack<>();
        private PackageNameStack packageNameStack = new PackageNameStack();
        private final Path rootDir;

        private MyFileVisitor(Path rootDir) {
            this.rootDir = rootDir;
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

            MyNodeVisitor nodeVisitor = this.parse(src);

            this.currentPackageStack.peek().addAll(nodeVisitor.types);

            return FileVisitResult.CONTINUE;
        }

        private String toString(Path file) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Files.copy(file, out);

            return new String(out.toByteArray());
        }

        private MyNodeVisitor parse(String src) {
            ASTParser parser = ASTParser.newParser(AST.JLS8);
            parser.setSource(src.toCharArray());
            CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
            MyNodeVisitor nodeVisitor = new MyNodeVisitor();
            unit.accept(nodeVisitor);

            return nodeVisitor;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            TargetPackage pkg = this.currentPackageStack.pop();
            this.packages.add(pkg);
            return FileVisitResult.CONTINUE;
        }
    }

    private static class PackageNameStack {
        private Stack<String> packageNames = new Stack<>();

        public void push(String name) {
            this.packageNames.push(name);
        }

        public void pop() {
            this.packageNames.pop();
        }

        @Override
        public String toString() {
            return this.packageNames.stream()
                    .collect(Collectors.joining("."));
        }

        public PackageName toPackageName() {
            String name = this.packageNames.stream().collect(Collectors.joining("."));
            return new PackageName(name);
        }
    }

    private static class MyNodeVisitor extends ASTVisitor {
        private TargetTypes types = new TargetTypes();
        private TargetType currentType;

        @Override
        public boolean visit(TypeDeclaration node) {
            String parentClassPrefix = "";

            if (this.isInnerClass(node)) {
                TypeDeclaration outer = (TypeDeclaration)node.getParent();
                parentClassPrefix = outer.getName().toString() + "$";
            }

            TypeName typeName1 = new TypeName(parentClassPrefix + node.getName().toString());
            this.currentType = new TargetType(typeName1);
            this.types.add(this.currentType);

            return true;
        }

        private boolean isInnerClass(TypeDeclaration node) {
            return node.isMemberTypeDeclaration()
                    && node.getParent() instanceof TypeDeclaration;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            TargetMethod targetMethod = this.toTargetMethod(node);
            this.currentType.add(targetMethod);

            return true;
        }

        private TargetMethod toTargetMethod(MethodDeclaration node) {
            MethodSignature signature = this.toMethodSignature(node);
            MethodBody body = new MethodBody(node.toString());

            return new TargetMethod(signature, body);
        }

        private MethodSignature toMethodSignature(MethodDeclaration node) {
            MethodParameters parameters = this.toMethodParameters(node);
            MethodName methodName = new MethodName(node.getName().toString());

            return new MethodSignature(methodName, parameters);
        }

        private MethodParameters toMethodParameters(MethodDeclaration node) {
            @SuppressWarnings("unchecked")
            List<SingleVariableDeclaration> rawParameters = node.parameters();

            List<MethodParameter> list = rawParameters.stream()
                    .map(this::toMethodParameter)
                    .collect(toList());

            return new MethodParameters(list);
        }

        private MethodParameter toMethodParameter(SingleVariableDeclaration parameter) {
            ParameterType type = new ParameterType(parameter.getType().toString());
            ParameterName name = new ParameterName(parameter.getName().toString());

            return new MethodParameter(type, name);
        }
    }
}
