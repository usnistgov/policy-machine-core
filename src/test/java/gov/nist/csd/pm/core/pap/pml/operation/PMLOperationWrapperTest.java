package gov.nist.csd.pm.core.pap.pml.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLOperationWrapperTest {

    @Test
    void testMultipleLayersOfWrappedOperations() throws PMException {
        String pml = """
            adminop op4() string {
               return "op4"
            }
            
            create pc op1(op2(op3(op4())))
            """;

        MemoryPAP pap = new MemoryPAP();
        OperationsModification operations = pap.modify().operations();

        FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);

        operations.createOperation(new AdminOperation<>("op1", STRING_TYPE, List.of(a), List.of()) {
            
            @Override
            public String execute(PAP pap, Args args) throws PMException {
                return args.get(a);
            }

        });

        operations.createOperation(new AdminOperation<>("op2", STRING_TYPE, List.of(a), List.of()) {
            
            @Override
            public String execute(PAP pap, Args args) throws PMException {
                return args.get(a);
            }

        });

        operations.createOperation(new AdminOperation<>("op3", STRING_TYPE, List.of(a), List.of()) {
            
            @Override
            public String execute(PAP pap, Args args) throws PMException {
                return args.get(a);
            }

        });

        pap.executePML(new UserContext(1), pml);

        assertTrue(pap.query().graph().nodeExists("op4"));
    }

}