package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParentOfAssignExecutorTest {

    private TestUtil.TestContext testCtx;

    @BeforeEach
    void setUp() throws PMException {
        testCtx = TestUtil.getTestCtx();
    }

    @Test
    void TestExec() throws PMException {
        ParentOfAssignExecutor executor = new ParentOfAssignExecutor();

        EventContext eventContext = new AssignEvent(new UserContext(testCtx.getU1().getName()), testCtx.getO1(), testCtx.getOa1());
        PDP pdp = testCtx.getPdp();
        Function function = new Function(executor.getFunctionName(), null);

        Node node = executor.exec(new UserContext("super"), eventContext, pdp, function, new FunctionEvaluator());

        assertNotNull(node);
        assertEquals(testCtx.getOa1(), node);
    }
}