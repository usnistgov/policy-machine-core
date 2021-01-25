package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.AssignToEvent;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsNodeContainedInExecutorTest {

    private TestUtil.TestContext testCtx;

    @BeforeEach
    void setUp() throws PMException {
        testCtx = TestUtil.getTestCtx();
    }

    @Test
    void TestExec() throws PMException {
        IsNodeContainedInExecutor executor = new IsNodeContainedInExecutor();

        EventContext eventContext = new AssignToEvent(new UserContext(testCtx.getU1().getName(), "1234"), testCtx.getOa1(), testCtx.getO1());
        PDP pdp = testCtx.getPdp();
        Function function = new Function(executor.getFunctionName(),
                Arrays.asList(
                        new Arg(
                                new Function("get_node", Arrays.asList(new Arg("o1"), new Arg("O")))
                        ),
                        new Arg(
                                new Function("get_node", Arrays.asList(new Arg("oa1"), new Arg("OA")))
                        )
                )
        );
        UserContext superUser = new UserContext("super");
        boolean isContained = executor.exec(pdp.withUser(superUser).getGraph(), pdp.withUser(superUser).getProhibitions(),
                pdp.withUser(superUser).getObligations(),
                eventContext, function, new FunctionEvaluator());
        assertTrue(isContained);

        function = new Function(executor.getFunctionName(),
                Arrays.asList(
                        new Arg(
                                new Function("get_node", Arrays.asList(new Arg("u1"), new Arg("U")))
                        ),
                        new Arg(
                                new Function("get_node", Arrays.asList(new Arg("oa1"), new Arg("OA")))
                        )
                )
        );
        isContained = executor.exec(pdp.withUser(superUser).getGraph(), pdp.withUser(superUser).getProhibitions(),
                pdp.withUser(superUser).getObligations(),
                eventContext, function, new FunctionEvaluator());
        assertFalse(isContained);
    }
}
