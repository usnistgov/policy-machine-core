package gov.nist.csd.pm.core.pap.pml.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PMLFunctionWrapperTest {

    @Test
    void testMultipleLayersOfWrappedFunctions() throws PMException {
        String pml = """
            operation op4() string {
               return "op4"
            }
            
            create pc op1(op2(op3(op4())))
            """;

        MemoryPAP pap = new MemoryPAP();
        OperationsModification operations = pap.modify().operations();

        FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);

        operations.createAdminOperation(new Operation<>("op1", List.of(a)) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {}

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return args.get(a);
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        });

        operations.createAdminOperation(new Operation<>("op2", List.of(a)) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {}

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return args.get(a);
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        });

        operations.createAdminOperation(new Operation<>("op3", List.of(a)) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {}

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return args.get(a);
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        });

        pap.executePML(new UserContext(1), pml);

        assertTrue(pap.query().graph().nodeExists("op4"));
    }

}