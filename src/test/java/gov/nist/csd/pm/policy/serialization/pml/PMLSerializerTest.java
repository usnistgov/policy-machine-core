package gov.nist.csd.pm.policy.serialization.pml;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.util.PolicyEquals;
import org.junit.jupiter.api.Test;

class PMLSerializerTest {

    String input =
            "            set resource access rights [\"read\", \"write\", \"execute\"]\n" +
            "            \n" +
            "            create policy class \"pc1\"\n" +
            "            set properties of \"pc1\" to {\"k\":\"v\"}\n" +
            "            create oa \"oa1\" assign to [\"pc1\"]\n" +
            "            set properties of \"oa1\" to {\"k1\":\"v1\", \"k2\":\"v2\"}\n" +
            "            create ua \"ua1\" assign to [\"pc1\"]\n" +
            "            create u \"u1\" assign to [\"ua1\"]\n" +
            "            \n" +
            "            associate \"ua1\" and \"oa1\" with [\"read\", \"write\"]\n" +
            "            \n" +
            "            create o \"o1\" assign to [\"oa1\"]\n" +
            "            \n" +
            "            create prohibition \"p1\" deny user attribute \"ua1\" access rights [\"read\"] on " +
            "union of [!\"oa1\"]\n" +
            "            create obligation \"obl1\" {\n" +
            "                create rule \"rule1\"\n" +
            "                when any user\n" +
            "                performs [\"event1\", \"event2\"]\n" +
            "                do(evtCtx) {\n" +
            "                    event := evtCtx[\"event\"]\n" +
            "                    if event == \"event1\" {\n" +
            "                        create policy class \"e1\"\n" +
            "                    } else if event == \"event2\" {\n" +
            "                        create policy class \"e2\"\n" +
            "                    }\n" +
            "                    \n" +
            "                    create prohibition \"p1\"\n" +
            "                    deny user attribute \"ua1\"\n" +
            "                    access rights [\"read\"]\n" +
            "                    on union of [!\"oa1\"]\n" +
            "                }\n" +
            "            }\n" +
            "            const testConst = \"hello world\"\n" +
            "            function testFunc() {\n" +
            "                create pc \"pc1\"\n" +
            "            }";

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