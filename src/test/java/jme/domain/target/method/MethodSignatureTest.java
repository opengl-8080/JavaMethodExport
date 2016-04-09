package jme.domain.target.method;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class MethodSignatureTest {

    @Test
    public void name() throws Exception {
        // setup
        MethodName methodName = new MethodName("myMethod");
        MethodParameters parameters = new MethodParameters(Arrays.asList(
                createMethodParameter(int.class, "integer"),
                createMethodParameter(long.class, "long")
        ));

        MethodSignature signature = new MethodSignature(methodName, parameters);

        // exercise
        String actual = signature.toString();

        // verify
        assertThat(actual).isEqualTo("myMethod(int_long)");
    }

    private MethodParameter createMethodParameter(Class<?> type, String name) {
        ParameterType _type = new ParameterType(type);
        ParameterName _name = new ParameterName(name);
        return new MethodParameter(_type, _name);
    }
}