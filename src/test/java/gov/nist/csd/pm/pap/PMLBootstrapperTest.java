package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PMLBootstrapperTest {

    @Test
    void test() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        String input = """
                const read = 'read'
                const write = 'write'
                set resource access rights [read, write]
                
                create pc 'pc1'
                create ua 'ua1' in ['pc1']
                create oa 'oa1' in ['pc1']
                
                associate 'ua1' and 'oa1' with [read]
                
                create user 'u1' in ['ua1']
                """;

        pap.bootstrap(new PMLBootstrapper(new UserContext("u1"), input));

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("ua1"));
        assertTrue(pap.graph().nodeExists("oa1"));
        assertTrue(pap.graph().nodeExists("u1"));
    }

}