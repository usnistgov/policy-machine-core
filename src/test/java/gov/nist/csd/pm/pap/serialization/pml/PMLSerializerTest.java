package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.util.PolicyEquals;
import org.junit.jupiter.api.Test;

class PMLSerializerTest {

    String input = """
            set resource access rights ["read", "write", "execute"]
            
            create policy class "pc1"
            set properties of "pc1" to {"k":"v"}
            create oa "oa1" assign to ["pc1"]
            set properties of "oa1" to {"k1":"v1", "k2":"v2"}
            create ua "ua1" assign to ["pc1"]
            create u "u1" assign to ["ua1"]
            
            associate "ua1" and "oa1" with ["read", "write"]
            
            create o "o1" assign to ["oa1"]
            
            create prohibition "p1" deny user attribute "ua1" access rights ["read"] on union of [!"oa1"]
            create obligation "obl1" {
                create rule "rule1"
                when any user
                performs ["event1", "event2"]
                do(evtCtx) {
                    event := evtCtx["event"]
                    if event == "event1" {
                        create policy class "e1"
                    } else if event == "event2" {
                        create policy class "e2"
                    }
                    
                    create prohibition "p1"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of [!"oa1"]
                }
            }
            const testConst = "hello world"
            function testFunc() {
                create pc "pc1"
            }
            """;

    @Test
    void testSerialization() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        UserContext userContext = new UserContext("u1");
        pap.deserialize(userContext, input, new PMLDeserializer());

        pap.graph().createObjectAttribute("test-oa", "pc1");
        pap.graph().assign(AdminPolicy.policyClassTargetName("pc1"), "test-oa");

        String expected = input + " create object attribute \"test-oa\" assign to [\"pc1\"]\n" + "assign \"pc1:target\" to [\"test-oa\"]";

        PAP testPAP = new PAP(new MemoryPolicyStore());
        testPAP.deserialize(userContext, expected, new PMLDeserializer());

        PolicyEquals.assertPolicyEquals(pap, testPAP);
    }

}