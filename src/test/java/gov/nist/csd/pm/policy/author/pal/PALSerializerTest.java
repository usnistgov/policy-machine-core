package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class PALSerializerTest {

    private static final String input = """
            set resource access rights read, write;
            create policy class 'pc1';
            set properties of 'pc1' to {'k':'v'};
            create oa 'oa1' in 'pc1';
            set properties of 'oa1' to {'k1':'v1'};
            create ua 'ua1' in 'pc1';
            associate 'ua1' and 'oa1' with read, write;
            create prohibition 'p1' deny user attribute 'ua1' access rights read on union of !'oa1';
            create obligation 'obl1' {
                create rule 'rule1'
                when any user
                performs 'event1', 'event2'
                do(evtCtx) {
                    let event = evtCtx['event'];
                    if equals(event, 'event1') {
                        create policy class 'e1';
                    } else if equals(event, 'event2') {
                        create policy class 'e2';
                    }
                }
            }
            """;
    private static final String expected = """
            set resource access rights read, write;
            create policy class 'pc1';
            set properties of 'pc1' to {'k': 'v'};
            create user attribute 'pc1_base_UA' in 'pc1';
            create object attribute 'pc1_base_OA' in 'pc1';
            create object attribute 'pc1_pc_rep' in 'pc1';
            create object attribute 'oa1' in 'pc1';
            set properties of 'oa1' to {'k1': 'v1'};
            create user attribute 'ua1' in 'pc1';
            associate 'ua1' and 'oa1' with read, write;       
            create prohibition 'p1' deny user attribute 'ua1' access rights read on union of !'oa1';           
            create obligation 'obl1' {create rule 'rule1' when any user performs 'event1', 'event2' on any policy element do (evtCtx) {let event = evtCtx['event'];if equals(event, 'event1') {create policy class 'e1';} else if equals(event, 'event2') {create policy class 'e2';} }}""";

    @Test
    void testSerialize() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        PALExecutor palExecutor = new PALExecutor(pap);
        palExecutor.compileAndExecutePAL(new UserContext(SUPER_USER), input);
        String actual = new PALSerializer(pap).toPAL();
        assertEquals(expected, actual);
    }

}