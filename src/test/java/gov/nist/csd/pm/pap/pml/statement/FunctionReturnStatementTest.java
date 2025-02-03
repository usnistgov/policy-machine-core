package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

class FunctionReturnStatementTest {

    @Test
    void testReturnValueIsUnwrapped() throws PMException {
        String pml = """
                operation f1() string {
                    return f2()
                }
                
                operation f2() string {
                    return "test"
                }
                
                create policy class f1()
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);
    }

}