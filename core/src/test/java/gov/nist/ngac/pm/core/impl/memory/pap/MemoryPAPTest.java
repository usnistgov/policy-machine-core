/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.impl.memory.pap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.PAPTest;
import gov.nist.ngac.pm.core.pap.modification.GraphModifierTest;
import gov.nist.ngac.pm.core.pap.modification.ObligationsModifierTest;
import gov.nist.ngac.pm.core.pap.modification.OperationsModifierTest;
import gov.nist.ngac.pm.core.pap.modification.ProhibitionsModifierTest;
import gov.nist.ngac.pm.core.pap.query.AccessQuerierTest;
import gov.nist.ngac.pm.core.pap.query.GraphQuerierTest;
import gov.nist.ngac.pm.core.pap.query.ObligationsQuerierTest;
import gov.nist.ngac.pm.core.pap.query.OperationsQuerierTest;
import gov.nist.ngac.pm.core.pap.query.ProhibitionsQuerierTest;
import gov.nist.ngac.pm.core.pap.query.RoutinesQuerierTest;
import gov.nist.ngac.pm.core.util.TestIdGenerator;

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
