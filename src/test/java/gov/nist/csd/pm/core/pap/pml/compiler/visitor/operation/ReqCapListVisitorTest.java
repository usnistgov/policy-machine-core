package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

class ReqCapListVisitorTest {

    @Test
    void testCallFunctionInReqCap() throws PMException {
        String pml = """
            @reqcap({
                if true {
                    require ["admin:graph:assignment:ascendant:create"] on [PM_ADMIN_BASE_OA]
                }
            })
            adminop op1() {
            
            }
            
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            associate "ua1" and PM_ADMIN_BASE_OA with ["*"]
            """;
        TestPAP testPAP = new TestPAP();
        testPAP.executePML(null, pml);

        assertDoesNotThrow(() -> testPAP.query()
            .operations()
            .getOperation("op1")
            .canExecute(testPAP, new UserContext(id("u1")), new Args()));
    }

}