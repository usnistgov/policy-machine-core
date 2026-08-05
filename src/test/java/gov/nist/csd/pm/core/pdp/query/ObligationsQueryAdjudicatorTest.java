package gov.nist.csd.pm.core.pdp.query;

import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.csd.pm.core.util.TestPAP;
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
