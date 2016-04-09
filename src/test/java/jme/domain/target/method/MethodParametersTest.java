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
        ParameterTypes parameters = new ParameterTypes(Arrays.asList(
                createMethodParameter("int")
        ));


        // exercise
        String actual = parameters.toString();

        // verify
        assertThat(actual).isEqualTo("int");
    }

    @Test
    public void パラメータが２つの場合() throws Exception {
        // setup
        ParameterTypes parameters = new ParameterTypes(Arrays.asList(
            createMethodParameter("int"),
            createMethodParameter("String")
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
        new ParameterTypes(Arrays.asList(
            createMethodParameter("int"),
            null
        ));
    }

    private ParameterType createMethodParameter(String type) {
        return new ParameterType(type);
    }
}