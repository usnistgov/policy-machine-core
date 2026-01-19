package gov.nist.csd.pm.core.impl.memory.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTest;
import gov.nist.csd.pm.core.pap.modification.GraphModifierTest;
import gov.nist.csd.pm.core.pap.modification.ObligationsModifierTest;
import gov.nist.csd.pm.core.pap.modification.OperationsModifierTest;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModifierTest;
import gov.nist.csd.pm.core.pap.query.AccessQuerierTest;
import gov.nist.csd.pm.core.pap.query.GraphQuerierTest;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerierTest;
import gov.nist.csd.pm.core.pap.query.OperationsQuerierTest;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuerierTest;
import gov.nist.csd.pm.core.pap.query.RoutinesQuerierTest;
import gov.nist.csd.pm.core.util.TestIdGenerator;

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

class MemoryRoutinesMQueirierTest extends RoutinesQuerierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP()
                .withIdGenerator(new TestIdGenerator());
    }
}
