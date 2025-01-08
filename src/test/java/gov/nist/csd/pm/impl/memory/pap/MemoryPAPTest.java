package gov.nist.csd.pm.impl.memory.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTest;
import gov.nist.csd.pm.pap.modification.*;
import gov.nist.csd.pm.pap.query.*;

public class MemoryPAPTest extends PAPTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryAccessQuerierTest extends AccessQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryGraphModifierTest extends GraphModifierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryGraphQuerierTest extends GraphQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryObligationsModifierTest extends ObligationsModifierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryObligationsQuerierTest extends ObligationsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }

}

class MemoryOperationsModifierTest extends OperationsModifierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryOperationsQueryTest extends OperationsQuerierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryProhibitionsModifierTest extends ProhibitionsModifierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryProhibitionsQuerierTest extends ProhibitionsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}

class MemoryRoutinesModifierTest extends RoutinesModifierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}
