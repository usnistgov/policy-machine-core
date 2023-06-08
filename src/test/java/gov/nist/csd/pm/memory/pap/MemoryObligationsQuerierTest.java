package gov.nist.csd.pm.memory.pap;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.ObligationsQuerierTest;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.pap.query.ObligationsQuery;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryObligationsQuerierTest extends ObligationsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }

}