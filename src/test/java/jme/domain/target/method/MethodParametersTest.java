package jme.domain.target.method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class MethodParametersTest {

    @Test
    public void パラメータが１つの場合() throws Exception {
        // setup
        MethodParameters parameters = new MethodParameters(Arrays.asList(
                createMethodParameter("int", "integer")
        ));

        // exercise
        String actual = parameters.toString();

        // verify
        assertThat(actual).isEqualTo("int");
    }

    @Test
    public void パラメータが２つの場合() throws Exception {
        // setup
        MethodParameters parameters = new MethodParameters(Arrays.asList(
            createMethodParameter("int", "integer"),
            createMethodParameter("String", "string")
        ));

        // exercise
        String actual = parameters.toString();

        // verify
        assertThat(actual).isEqualTo("int_String");
    }

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void パラメータにnullが含まれていたらエラー() throws Exception {
        // verify
        ex.expect(IllegalArgumentException.class);

        // exercise
        new MethodParameters(Arrays.asList(
            createMethodParameter("int", "int"),
            null
        ));
    }

    private MethodParameter createMethodParameter(String type, String name) {
        ParameterType _type = new ParameterType(type);
        ParameterName _name = new ParameterName(name);
        return new MethodParameter(_type, _name);
    }
}