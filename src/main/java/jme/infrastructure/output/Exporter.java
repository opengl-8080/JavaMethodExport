package jme.infrastructure.output;

import jme.domain.target.pkg.TargetPackage;

public interface Exporter {

    void export(TargetPackage pkg);
}
