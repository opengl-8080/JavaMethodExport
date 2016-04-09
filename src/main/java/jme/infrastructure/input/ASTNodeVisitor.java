package jme.infrastructure.input;

import jme.domain.target.method.MethodBody;
import jme.domain.target.method.MethodName;
import jme.domain.target.method.MethodParameter;
import jme.domain.target.method.MethodParameters;
import jme.domain.target.method.MethodSignature;
import jme.domain.target.method.ParameterName;
import jme.domain.target.method.ParameterType;
import jme.domain.target.method.TargetMethod;
import jme.domain.target.type.TargetType;
import jme.domain.target.type.TargetTypes;
import jme.domain.target.type.TypeName;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.List;

import static java.util.stream.Collectors.*;

public class ASTNodeVisitor extends ASTVisitor {
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

    public TargetTypes getTypes() {
        return types;
    }
}
