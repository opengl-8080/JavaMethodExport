package jme.domain.target.method;

public class TargetMethod {
    private final MethodSignature signature;
    private final MethodBody body;

    public TargetMethod(MethodSignature signature, MethodBody body) {
        if (signature == null || body == null) {
            throw new NullPointerException("signature or body is null. (signature=" + signature + ", body=" + body + ")");
        }
        this.signature = signature;
        this.body = body;
    }

    public MethodSignature getSignature() {
        return signature;
    }

    public MethodBody getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetMethod that = (TargetMethod) o;

        return signature.equals(that.signature);

    }

    @Override
    public int hashCode() {
        return signature.hashCode();
    }
}
