package jme.infrastructure.output;

import jme.domain.target.method.MethodBody;
import jme.domain.target.method.MethodName;
import jme.domain.target.method.MethodSignature;
import jme.domain.target.method.ParameterType;
import jme.domain.target.method.ParameterTypes;
import jme.domain.target.method.TargetMethod;
import jme.domain.target.pkg.PackageName;
import jme.domain.target.pkg.TargetPackage;
import jme.domain.target.type.TargetType;
import jme.domain.target.type.TargetTypes;
import jme.domain.target.type.TypeName;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class ExporterTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private File rootDir;
    private ExporterImpl exporter;

    @Before
    public void setUp() throws Exception {
        this.rootDir = this.tmpDir.getRoot();
        this.exporter = new ExporterImpl(this.rootDir);
    }

    @Test
    public void ルートパッケージは出力されない() throws Exception {
        // setup
        TargetPackage pkg = createPackage(PackageName.ROOT.toString());

        // exercise
        exporter.export(pkg);

        // verify
        assertThat(this.rootDir.list()).isEmpty();
    }

    @Test
    public void ルートではないパッケージは出力される() throws Exception {
        // setup
        TargetPackage pkg = createPackage("foo");

        // exercise
        exporter.export(pkg);

        // verify
        File foo = new File(this.rootDir, "foo");
        assertThat(foo).isDirectory();
    }

    @Test
    public void パッケージの階層も出力される() throws Exception {
        // setup
        TargetPackage pkg = createPackage("foo.bar");

        // exercise
        exporter.export(pkg);

        // verify
        File bar = new File(this.rootDir, "foo/bar");
        assertThat(bar).isDirectory();
    }

    @Test
    public void クラス名のフォルダが出力される_ルートパッケージ() throws Exception {
        // setup
        TargetPackage pkg = createPackage(PackageName.ROOT.toString());
        addClass(pkg, "FooClass");

        // exercise
        exporter.export(pkg);

        // verify
        File bar = new File(this.rootDir, "FooClass");
        assertThat(bar).isDirectory();
    }

    @Test
    public void クラス名のフォルダが出力される_サブパッケージ() throws Exception {
        // setup
        TargetPackage pkg = createPackage("foo.bar");
        addClass(pkg, "FooClass");

        // exercise
        exporter.export(pkg);

        // verify
        File bar = new File(this.rootDir, "foo/bar/FooClass");
        assertThat(bar).isDirectory();
    }

    @Test
    public void メソッド名のファイルが出力される() throws Exception {
        // setup
        TargetPackage pkg = createPackage("foo.bar");
        TargetType fooClass = addClass(pkg, "FooClass");

        TargetMethod method = createMethod("method", "メソッドボディ", "int", "String");
        fooClass.add(method);

        // exercise
        exporter.export(pkg);

        // verify
        File bar = new File(this.rootDir, "foo/bar/FooClass/method(int_String).java");
        assertThat(bar)
                .usingCharset("UTF-8")
                .isFile()
                .hasContent("メソッドボディ");
    }

    private static TargetPackage createPackage(String packageName) {
        return new TargetPackage(new PackageName(packageName));
    }

    private static TargetType addClass(TargetPackage pkg, String className) {
        TargetType fooClass = new TargetType(new TypeName(className));
        TargetTypes types = new TargetTypes();
        types.add(fooClass);
        pkg.addAll(types);

        return fooClass;
    }

    private static TargetMethod createMethod(String name, String body, String... types) {
        List<ParameterType> typeList = Arrays.stream(types).map(ParameterType::new).collect(Collectors.toList());
        ParameterTypes parameters = new ParameterTypes(typeList);

        MethodName methodName = new MethodName(name);
        MethodSignature signature = new MethodSignature(methodName, parameters);

        MethodBody methodBody = new MethodBody(body);
        return new TargetMethod(signature, methodBody);
    }
}