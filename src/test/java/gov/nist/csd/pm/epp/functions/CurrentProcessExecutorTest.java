package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentProcessExecutorTest {

    private TestUtil.TestContext testCtx;

    @BeforeEach
    void setUp() throws PMException {
        testCtx = TestUtil.getTestCtx();
    }

    @Test
    void TestExec() throws PMException {
        CurrentProcessExecutor executor = new CurrentProcessExecutor();

        EventContext eventContext = new AssignEvent(testCtx.getO1(), testCtx.getOa1());
        String user = testCtx.getU1().getName();
        String process = "1234";
        PDP pdp = testCtx.getPdp();
        Function function = new Function(executor.getFunctionName(), null);

        Prohibition.Subject result = executor.exec(eventContext, user, process, pdp, function, new FunctionEvaluator());

        assertNotNull(result);
        assertEquals("1234", result.getSubject());
        assertEquals(Prohibition.Subject.Type.PROCESS, result.getSubjectType());
    }
}