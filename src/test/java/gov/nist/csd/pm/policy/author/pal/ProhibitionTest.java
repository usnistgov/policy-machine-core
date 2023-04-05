package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.memory.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PALDeserializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_POLICY_CLASS;
import static org.junit.jupiter.api.Assertions.*;

class ProhibitionTest {

    @Test
    void testCreateProhibition() throws PMException {
        String input = """
                set resource access rights ['read', 'write'];
                create pc 'pc1';
                create oa 'oa1' in ['pc1'];
                create ua 'ua1' in ['pc1'];
                create u 'u1' in ['ua1'];
                create u 'u2' in ['ua1'];
                associate 'ua1' and 'oa1' with ['read', 'write'];
                
                create prohibition 'pro1'
                deny user 'u1'
                access rights [create_policy_class, 'write']
                on union of ['oa1'];
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));

        Prohibition prohibition = pap.getProhibition("pro1");
        assertEquals("pro1", prohibition.getLabel());
        assertEquals(new ProhibitionSubject("u1", ProhibitionSubject.Type.USER), prohibition.getSubject());
        assertEquals(new AccessRightSet(CREATE_POLICY_CLASS, "write"), prohibition.getAccessRightSet());
        assertFalse(prohibition.isIntersection());
        assertEquals(List.of(new ContainerCondition("oa1", false)), prohibition.getContainers());

        MemoryPolicyReviewer reviewer = new MemoryPolicyReviewer(pap);
        AccessRightSet accessRights = reviewer.getAccessRights(new UserContext("u1"), "oa1");
        assertEquals(new AccessRightSet("read"), accessRights);
        accessRights = reviewer.getAccessRights(new UserContext("u2"), "oa1");
        assertEquals(new AccessRightSet("read", "write"), accessRights);
    }
}
