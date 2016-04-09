package jme.domain.target.type;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TypeNameTest {

    @Test
    public void 普通のクラス() throws Exception {
        // setup
        TypeName typeName = new TypeName(TypeNameTest.class);

        // exercise
        String actual = typeName.toString();

        // verify
        assertThat(actual).isEqualTo("TypeNameTest");
    }

    @Test
    public void インナークラス() throws Exception {
        // setup
        TypeName typeName = new TypeName(InnerClass.class);

        // exercise
        String actual = typeName.toString();

        // verify
        assertThat(actual).isEqualTo("TypeNameTest$InnerClass");
    }

    public static class InnerClass {}
}