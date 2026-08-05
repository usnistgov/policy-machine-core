package gov.nist.ngac.pm.core.pdp.query;

import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphQueryAdjudicatorTest {

    PAP pap;
    GraphQueryAdjudicator adjudicator;

    @BeforeEach
    void setup() throws PMException {
        pap = new TestPAP();

        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]

                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]

                create u "u1" in ["ua1"]
                """);

        adjudicator = new GraphQueryAdjudicator(pap, NodeUserContext.of("u1"));
    }

    @Test
    void testFilterNodesPropagatesPMExceptionInsteadOfRuntimeException() {
        assertThrows(NodeDoesNotExistException.class, () ->
            adjudicator.filterNodes(new ArrayList<>(List.of(123456789L)), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST));
    }
}
