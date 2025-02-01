package gov.nist.csd.pm.impl.memory.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTest;
import gov.nist.csd.pm.pap.id.RandomIdGenerator;
import gov.nist.csd.pm.pap.modification.*;
import gov.nist.csd.pm.pap.query.*;
import gov.nist.csd.pm.util.TestIdGenerator;

public class MemoryPAPTest extends PAPTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryAccessQuerierTest extends AccessQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryGraphModifierTest extends GraphModifierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryGraphQuerierTest extends GraphQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryObligationsModifierTest extends ObligationsModifierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryObligationsQuerierTest extends ObligationsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }

}

class MemoryOperationsModifierTest extends OperationsModifierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryOperationsQueryTest extends OperationsQuerierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryProhibitionsModifierTest extends ProhibitionsModifierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryProhibitionsQuerierTest extends ProhibitionsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}

class MemoryRoutinesModifierTest extends RoutinesModifierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}
