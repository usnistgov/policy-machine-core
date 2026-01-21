package gov.nist.csd.pm.core.pap.pml.statement;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

class ReturnStatementTest {

    @Test
    void testReturnValueIsUnwrapped() throws PMException {
        String pml = """
                adminop f1() string {
                    return f2()
                }
                
                adminop f2() string {
                    return "test"
                }
                
                create PC f1()
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);
    }

}