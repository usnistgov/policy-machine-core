package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import org.junit.jupiter.api.BeforeEach;

public abstract class PAPTestInitializer {

    protected PAP pap;

    public abstract PAP initializePAP() throws PMException;

    @BeforeEach
    void setup() throws PMException {
        pap = initializePAP();
    }
}
