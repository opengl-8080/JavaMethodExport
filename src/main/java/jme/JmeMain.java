package jme;

import jme.domain.target.pkg.TargetPackages;
import jme.infrastructure.input.SourceLoader;
import jme.infrastructure.output.Exporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
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

    private void execute() {
        SourceLoader loader = new SourceLoader();
        loader.setBaseDir(Paths.get(this.inDir));

        TargetPackages packages = loader.load();

        Exporter exporter = new Exporter(new File(this.outDir));
        exporter.export(packages);
    }
}
