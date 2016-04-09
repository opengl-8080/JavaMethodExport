package jme;

import jme.infrastructure.input.SourceLoader;
import jme.infrastructure.output.ExporterImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@SpringBootApplication
public class JmeMain {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JmeMain.class);
        app.setBannerMode(Banner.Mode.OFF);

        try (ConfigurableApplicationContext ctx = app.run(args)) {
            JmeMain jme = ctx.getBean(JmeMain.class);
            jme.execute();
        }
    }

    @Value("${in}")
    private String inDir;
    @Value("${out}")
    private String outDir;
    @Value("${charset}")
    private String charset;

    private void execute() {
        _charset = Charset.forName(this.charset);

        SourceLoader loader = new SourceLoader();
        loader.setBaseDir(Paths.get(this.inDir));

        ExporterImpl exporter = new ExporterImpl(new File(this.outDir));
        loader.setExporter(exporter);

        loader.load();
    }

    private static Charset _charset = StandardCharsets.UTF_8;

    public static Charset getCharset() {
        return _charset;
    }
}
