package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.events.ObjectAccessEvent;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.*;

class CreateNodeExecutorTest {
    private TestUtil.TestContext testCtx;

    @BeforeEach
    void setUp() throws PMException {
        testCtx = TestUtil.getTestCtx();
    }

    @Test
    void TestExec() throws PMException {
        CreateNodeExecutor executor = new CreateNodeExecutor();

        EventContext eventContext = null;
        PDP pdp = testCtx.getPdp();
        Function function =
                new Function(
                        executor.getFunctionName(),
                        Arrays.asList(
                                new Arg("oa1"),
                                new Arg("OA"),
                                new Arg("testNode"),
                                new Arg("OA"),
                                new Arg(new Function("to_props", Arrays.asList(new Arg("k=v"))))));

        Node n = executor.exec(new UserContext("super"), eventContext, pdp, function, new FunctionEvaluator());

        assertNotNull(n);
        assertEquals("testNode", n.getName());
        assertEquals(OA, n.getType());
        assertNotNull(n.getProperties());
        assertEquals("v", n.getProperties().get("k"));
    }
}