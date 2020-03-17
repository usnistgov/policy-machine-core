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

class CurrentUserToDenySubjectExecutorTest {

    private TestUtil.TestContext testCtx;

    @BeforeEach
    void setUp() throws PMException {
        testCtx = TestUtil.getTestCtx();
    }

    @Test
    void TestExec() throws PMException {
        CurrentUserToDenySubjectExecutor executor = new CurrentUserToDenySubjectExecutor();

        EventContext eventContext = new AssignEvent(testCtx.getO1(), testCtx.getOa1());
        String user = testCtx.getU1().getName();
        String process = "1234";
        PDP pdp = testCtx.getPdp();
        Function function = new Function(executor.getFunctionName(), null);

        Prohibition.Subject subject = executor.exec(eventContext, user, process, pdp, function, new FunctionEvaluator());

        assertNotNull(subject);
        assertEquals(user, subject.getSubject());
        assertEquals(Prohibition.Subject.Type.USER, subject.getSubjectType());
    }
}