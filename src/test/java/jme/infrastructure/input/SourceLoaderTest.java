package jme.infrastructure.input;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import jme.domain.target.method.MethodBody;
import jme.domain.target.method.MethodSignature;
import jme.domain.target.method.TargetMethod;
import jme.domain.target.method.TargetMethods;
import jme.domain.target.pkg.PackageName;
import jme.domain.target.pkg.TargetPackage;
import jme.domain.target.pkg.TargetPackages;
import jme.domain.target.type.TargetType;
import jme.domain.target.type.TargetTypes;
import jme.domain.target.type.TypeName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(HierarchicalContextRunner.class)
public class SourceLoaderTest {

    private static final Path BASE_PATH = Paths.get("./src/test/java/jme/infrastructure/input/source_loader_test");
    private SourceLoader loader = new SourceLoader();

    public class 基本テスト {
        @Before
        public void setUp() throws Exception {
            loader.setBaseDir(BASE_PATH.resolve("load_class"));
        }

        @Test
        public void ルートのパッケージが読み込める() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            assertThat(targetPackages.first().isRoot()).isEqualTo(true);
        }

        @Test
        public void ソースファイルが読み込めている() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            TargetTypes targetTypes = targetPackages.first().getTargetTypes();
            TargetType type = targetTypes.first();

            assertThat(type.getName().toString()).isEqualTo("LoadClass");
        }

        @Test
        public void publicメソッドが読み込めている() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            TargetMethod method = getFirstMethod(targetPackages);

            assertTargetMethod(method, "publicMethod(int_String_long)",
                    "public String publicMethod(int i,String s,long l){",
                    "  return \"test\";",
                    "}"
            );
        }
    }

    public class privateメソッドの読み込みテスト {

        @Test
        public void privateメソッドも読み込めている() throws Exception {
            // setup
            loader.setBaseDir(BASE_PATH.resolve("private_method"));

            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            TargetMethod method = getFirstMethod(targetPackages);

            assertTargetMethod(method, "privateMethod()",
                    "private void privateMethod(){",
                    "}"
            );
        }
    }

    public class インナークラスが存在する場合 {

        @Before
        public void setUp() throws Exception {
            loader.setBaseDir(BASE_PATH.resolve("inner_class"));
        }

        @Test
        public void インナークラスも読み込めていること() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            TargetPackage pkg = targetPackages.first();
            List<String> typeNames = pkg.getTypeNames();

            assertThat(typeNames).contains("OuterClass", "OuterClass$InnerClass");
        }

        @Test
        public void インナークラスのメソッドも読み込めていること() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            TargetPackage pkg = targetPackages.first();
            TargetType type = pkg.find(new TypeName("OuterClass$InnerClass")).orElseThrow(Exception::new);
            TargetMethod method = type.getMethods().first();

            assertTargetMethod(method, "innerClassMethod()",
                    "public void innerClassMethod(){",
                    "}"
            );
        }
    }

    public class サブパッケージが存在する場合 {

        @Before
        public void setUp() throws Exception {
            loader.setBaseDir(BASE_PATH.resolve("sub_packages"));
        }

        @Test
        public void 各パッケージが読み込める() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            List<String> packageNames = targetPackages.getPackageNames();

            assertThat(packageNames).contains("foo", "foo.bar", "", "foo.fizz");
        }

        @Test
        public void 各階層のクラスが読み込まれている() throws Exception {
            // exercise
            TargetPackages targetPackages = loader.load();

            // verify
            TargetPackage rootPackage = targetPackages.find(PackageName.ROOT).orElseThrow(Exception::new);
            rootPackage.find(new TypeName("RootClass")).orElseThrow(Exception::new);

            TargetPackage fooPackage = targetPackages.find(new PackageName("foo")).orElseThrow(Exception::new);
            fooPackage.find(new TypeName("FooClass")).orElseThrow(Exception::new);

            TargetPackage fooBarPackage = targetPackages.find(new PackageName("foo.bar")).orElseThrow(Exception::new);
            fooBarPackage.find(new TypeName("BarClass")).orElseThrow(Exception::new);

            TargetPackage fooFizzPackage = targetPackages.find(new PackageName("foo.fizz")).orElseThrow(Exception::new);
            fooFizzPackage.find(new TypeName("FizzClass")).orElseThrow(Exception::new);
        }
    }


    private static void assertTargetMethod(TargetMethod method, String expectedSignature, String... expectedBodies) {
        MethodSignature signature = method.getSignature();
        assertThat(signature.toString()).as("signature").isEqualTo(expectedSignature);

        MethodBody body = method.getBody();
        String expectedBody = Arrays.stream(expectedBodies).map(s -> s + "\n").collect(joining());
        assertThat(body.toString()).as("body").isEqualTo(expectedBody);
    }

    private static TargetMethod getFirstMethod(TargetPackages packages) {
        return packages
                .first().getTargetTypes()
                .first().getMethods()
                .first();
    }
}