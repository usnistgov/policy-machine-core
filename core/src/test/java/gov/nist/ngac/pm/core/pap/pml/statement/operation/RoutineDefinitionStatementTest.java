package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class RoutineDefinitionStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                routine routine1(string a) {
                    create PC a
                    create OA "oa1" in [a]
                }""";
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), pml);
        Routine<?> routine1 = (Routine<?>) pap.query().operations().getOperation("routine1");
        assertEquals(pml, routine1.toString());
    }

}