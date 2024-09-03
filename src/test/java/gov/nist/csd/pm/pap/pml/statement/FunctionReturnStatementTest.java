package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

class FunctionReturnStatementTest {

    @Test
    void testReturnValueIsUnwrapped() throws PMException {
        String pml = "operation f1() string {\n" +
                "                    return f2()\n" +
                "                }\n" +
                "                \n" +
                "                operation f2() string {\n" +
                "                    return \"test\"\n" +
                "                }\n" +
                "                \n" +
                "                create policy class f1()";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(), pml);
    }

}