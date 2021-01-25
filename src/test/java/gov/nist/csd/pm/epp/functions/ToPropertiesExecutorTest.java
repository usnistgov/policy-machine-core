package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToPropertiesExecutorTest {

    private TestUtil.TestContext testCtx;

    @BeforeEach
    void setUp() throws PMException {
        testCtx = TestUtil.getTestCtx();
    }

    @Test
    void TestExec() throws PMException {
        ToPropertiesExecutor executor = new ToPropertiesExecutor();

        EventContext eventContext = null;
        PDP pdp = testCtx.getPdp();
        Function function = new Function(executor.getFunctionName(), Arrays.asList(new Arg("k=v"), new Arg("k1=v1"), new Arg("k2=v2")));

        UserContext superUser = new UserContext("super");
        Map props = executor.exec(pdp.withUser(superUser).getGraph(), pdp.withUser(superUser).getProhibitions(),
                pdp.withUser(superUser).getObligations(),
                eventContext, function, new FunctionEvaluator());

        assertNotNull(props);
        assertEquals(3, props.size());
        assertEquals("v", props.get("k"));
        assertEquals("v1", props.get("k1"));
        assertEquals("v2", props.get("k2"));
    }
}
