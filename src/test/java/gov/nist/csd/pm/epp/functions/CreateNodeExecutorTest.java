package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        UserContext superUser = new UserContext("super");
        Node n = executor.exec(pdp.withUser(superUser).getGraph(), pdp.withUser(superUser).getProhibitions(),
                pdp.withUser(superUser).getObligations(),
                eventContext, function, new FunctionEvaluator());

        assertNotNull(n);
        assertEquals("testNode", n.getName());
        assertEquals(OA, n.getType());
        assertNotNull(n.getProperties());
        assertEquals("v", n.getProperties().get("k"));
    }
}
