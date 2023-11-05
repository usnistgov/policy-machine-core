package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.events.graph.AssignToEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void testResponseExecutionWithUserDefinedAndBuiltinPML() throws PMException {
        String pml = """
                create pc "pc1"
                create oa "oa1" assign to ["pc1"]
                create ua "ua1" assign to ["pc1"]
                create u "u1" assign to ["ua1"]
                
                associate "ua1" and POLICY_CLASS_TARGETS with [create_policy_class]
                const x = "hello world"
                function createX() {
                    create policy class x
                }
                
                create obligation "obl1" {
                    create rule "rule1"
                    when any user
                    performs ["assign_to"]
                    on ["oa1"]
                    do(ctx) {
                        createX()
                    }
                }
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.getEventProcessor().processEvent(new EventContext(new UserContext("u1"), "oa1", new AssignToEvent("o1", "oa1")));
        assertTrue(pap.graph().nodeExists("hello world"));
    }

}