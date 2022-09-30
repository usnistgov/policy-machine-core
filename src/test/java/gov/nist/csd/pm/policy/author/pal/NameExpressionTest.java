package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

public class NameExpressionTest {

    @Test
    void testNameExpression() throws PMException {
        String pal = """
                create pc pc1;
                """;
        PAP pap = new MemoryPAP();
        pap.compileAndExecutePAL(new UserContext(SUPER_USER), pal);
        assertTrue(pap.graph().nodeExists("pc1"));

        pal = """
                create oa oa1 assign to pc1;
                """;
        pap.compileAndExecutePAL(new UserContext(SUPER_USER), pal);
        assertTrue(pap.graph().nodeExists("oa1"));
        assertEquals(Arrays.asList("pc1"), pap.graph().getParents("oa1"));

        pal = """
                let parent = 'oa1';
                create oa oa2 assign to parent;
                """;
        pap.compileAndExecutePAL(new UserContext(SUPER_USER), pal);
        assertTrue(pap.graph().nodeExists("oa1"));
        assertTrue(pap.graph().nodeExists("oa2"));
        assertFalse(pap.graph().nodeExists("parent"));
        assertEquals(Arrays.asList("oa1"), pap.graph().getParents("oa2"));


        pal = """
                create obligation obl1 {}
                """;
        pap.compileAndExecutePAL(new UserContext(SUPER_USER), pal);
        assertDoesNotThrow(() -> pap.obligations().get("obl1"));

        pal = """
                create prohibition pro1 deny ua super_ua access rights *a on intersection of oa1, !oa2;
                """;
        pap.compileAndExecutePAL(new UserContext(SUPER_USER), pal);
        assertDoesNotThrow(() -> pap.prohibitions().get("pro1"));
    }
}
