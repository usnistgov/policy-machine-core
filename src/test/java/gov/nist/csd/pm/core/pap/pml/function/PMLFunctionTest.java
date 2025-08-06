package gov.nist.csd.pm.core.pap.pml.function;

import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

public class PMLFunctionTest {

    @Test
    void testFunctionsPersistedAsOperations() throws PMException {
        String pml = """
            function test() string {
                return "test"
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.executePML(new UserContext(0), pml);

        assertTrue(memoryPAP.query().operations().getAdminOperationNames().contains("test"));

        pml = """
            create PC test()
            """;
        memoryPAP.executePML(new UserContext(0), pml);

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

}
