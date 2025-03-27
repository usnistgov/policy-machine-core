package gov.nist.csd.pm.pap.pml.executable.function;

import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.type.Type;
import java.util.List;

public class PMLStmtsFunction extends PMLStmtsRoutine {
    public PMLStmtsFunction(String name,
                            Type returnType,
                            List<PMLFormalArg> formalArgs,
                            PMLStatementBlock statements) {
        super(name, returnType, formalArgs, statements);
    }
}
