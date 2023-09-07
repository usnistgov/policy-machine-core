package gov.nist.csd.pm.policy.model.obligation;

import com.google.gson.Gson;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperUserBootstrapper;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.events.graph.AssignToEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void testResponseExecutionWithUserDefinedAndBuiltinPML() throws PMException {
        String pml = """
                create pc 'pc1'
                create oa 'oa1' in ['pc1']
                create ua 'ua1' in ['pc1']
                create u 'u1' in ['ua1']
                
                associate 'ua1' and POLICY_CLASSES_OA with [create_policy_class]
                const x = "hello world"
                function createX() {
                    create policy class x
                }
                
                create obligation 'obl1' {
                    create rule 'rule1'
                    when any user
                    performs ['assign_to']
                    on 'oa1'
                    do(ctx) {
                        createX()
                    }
                }
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.bootstrap(new SuperUserBootstrapper());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        MemoryPDP pdp = new MemoryPDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.getEventProcessor().processEvent(new EventContext(new UserContext("u1"), "oa1", new AssignToEvent("o1", "oa1")));
        assertTrue(pap.graph().nodeExists("hello world"));
    }

}