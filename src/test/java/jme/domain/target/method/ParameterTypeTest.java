package jme.domain.target.method;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ParameterTypeTest {

    @Test
    public void 型名にジェネリクスがある場合は除去される() throws Exception {
        // setup
        ParameterType type = new ParameterType("List<Foo>");

        // exercise
        String actual = type.toString();

        // verify
        assertThat(actual).isEqualTo("List");
    }
}