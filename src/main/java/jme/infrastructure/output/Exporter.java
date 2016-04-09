package jme.infrastructure.output;

import jme.domain.target.pkg.TargetPackage;

import java.io.File;

public interface Exporter {

    void exportClassInfo(TargetPackage pkg, File dir);
}
