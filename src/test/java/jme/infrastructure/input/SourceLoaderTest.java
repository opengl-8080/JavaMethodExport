package jme.infrastructure.input;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import jme.domain.target.method.MethodBody;
import jme.domain.target.method.MethodSignature;
import jme.domain.target.method.TargetMethod;
import jme.domain.target.pkg.PackageName;
import jme.domain.target.pkg.TargetPackage;
import jme.domain.target.pkg.TargetPackages;
import jme.domain.target.type.TargetType;
import jme.domain.target.type.TypeName;
import jme.infrastructure.output.Exporter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(HierarchicalContextRunner.class)
public class SourceLoaderTest {

    private static final Path BASE_PATH = Paths.get("./src/test/java/jme/infrastructure/input/source_loader_test");
    private SourceLoader loader = new SourceLoader();
    private TestExporter exporter = new TestExporter();

    private static class TestExporter implements Exporter {

        private List<Param> list = new ArrayList<>();

        @Override
        public void export(TargetPackage pkg) {
            this.list.add(new Param(pkg));
        }

        public TargetPackage firstPackage() {
            return this.list.get(0).pkg;
        }

        public TargetType firstType() {
            return this.firstPackage().getTargetTypes().first();
        }

        public TargetMethod firstMethod() {
            return this.firstType().getMethods().first();
        }

        public Optional<TargetPackage> find(PackageName name) {
            return this.list.stream()
                    .map(param -> param.pkg)
                    .filter(pkg -> pkg.getPackageName().equals(name))
                    .findFirst();
        }
    }

    private static class Param {
        private final TargetPackage pkg;

        private Param(TargetPackage pkg) {
            this.pkg = pkg;
        }
    }

    @Before
    public void setUp() throws Exception {
        loader.setExporter(exporter);
    }

    public class 基本テスト {
        private File baseDir = BASE_PATH.resolve("load_class").toFile();

        @Before
        public void setUp() throws Exception {
            loader.setBaseDir(baseDir.toPath());
        }

        @Test
        public void ルートのパッケージが読み込める() throws Exception {
            // exercise
            loader.load();

            // verify
            assertThat(exporter.list)
                    .extracting(p -> p.pkg.getPackageName())
                    .contains(PackageName.ROOT);
        }

        @Test
        public void ソースファイルが読み込めている() throws Exception {
            // exercise
            loader.load();

            // verify
            TargetType type = exporter.firstType();

            assertThat(type.getName().toString()).isEqualTo("LoadClass");
        }

        @Test
        public void publicメソッドが読み込めている() throws Exception {
            // exercise
            loader.load();

            // verify
            TargetMethod method = exporter.firstMethod();

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
            loader.load();

            // verify
            TargetMethod method = exporter.firstMethod();

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
            loader.load();

            // verify
            TargetPackage pkg = exporter.firstPackage();
            List<String> typeNames = pkg.getTypeNames();

            assertThat(typeNames).contains("OuterClass", "OuterClass$InnerClass");
        }

        @Test
        public void インナークラスのメソッドも読み込めていること() throws Exception {
            // exercise
            loader.load();

            // verify
            TargetPackage pkg = exporter.firstPackage();
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
            loader.load();

            // verify
            assertThat(exporter.list)
                    .extracting(param -> param.pkg.getPackageName().toString())
                    .contains("foo", "foo.bar", "", "foo.fizz");
        }

        @Test
        public void 各階層のクラスが読み込まれている() throws Exception {
            // exercise
            loader.load();

            // verify
            TargetPackage rootPackage = exporter.find(PackageName.ROOT).orElseThrow(Exception::new);
            rootPackage.find(new TypeName("RootClass")).orElseThrow(Exception::new);

            TargetPackage fooPackage = exporter.find(new PackageName("foo")).orElseThrow(Exception::new);
            fooPackage.find(new TypeName("FooClass")).orElseThrow(Exception::new);

            TargetPackage fooBarPackage = exporter.find(new PackageName("foo.bar")).orElseThrow(Exception::new);
            fooBarPackage.find(new TypeName("BarClass")).orElseThrow(Exception::new);

            TargetPackage fooFizzPackage = exporter.find(new PackageName("foo.fizz")).orElseThrow(Exception::new);
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