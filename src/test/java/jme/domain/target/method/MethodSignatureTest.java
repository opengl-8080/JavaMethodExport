package jme.domain.target.method;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class MethodSignatureTest {

    @Test
    public void name() throws Exception {
        // setup
        MethodName methodName = new MethodName("myMethod");
        ParameterTypes parameters = new ParameterTypes(Arrays.asList(
                createMethodParameter("int"),
                createMethodParameter("long")
        ));

        MethodSignature signature = new MethodSignature(methodName, parameters);

        // exercise
        String actual = signature.toString();

        // verify
        assertThat(actual).isEqualTo("myMethod(int_long)");
    }

    private ParameterType createMethodParameter(String type) {
        return new ParameterType(type);
    }
}