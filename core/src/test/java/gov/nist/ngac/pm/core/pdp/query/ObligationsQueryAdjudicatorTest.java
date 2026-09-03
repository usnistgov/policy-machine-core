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

package gov.nist.ngac.pm.core.pdp.query;

import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObligationsQueryAdjudicatorTest {

    PAP pap;
    ObligationsQueryAdjudicator adjudicator;

    @BeforeEach
    void setup() throws PMException {
        pap = new TestPAP();

        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]

                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]

                create u "u1" in ["ua1"]
                """);

        adjudicator = new ObligationsQueryAdjudicator(pap, NodeUserContext.of("u1"));
    }

    @Test
    void testFilterObligationsPropagatesPMExceptionInsteadOfRuntimeException() {
        Obligation obligation = new Obligation(NodeUserContext.of(123456789L), "ob1", null, null);

        assertThrows(NodeDoesNotExistException.class, () ->
            adjudicator.filterObligations(new ArrayList<>(List.of(obligation))));
    }
}
