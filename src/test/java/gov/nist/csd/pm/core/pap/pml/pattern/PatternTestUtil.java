package gov.nist.csd.pm.core.pap.pml.pattern;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateRuleStatement;
import java.util.List;

public class PatternTestUtil {

    public static CreateRuleStatement compileTestCreateRuleStatement(String pml) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();
        List<PMLStatement<?>> pmlStatements = pmlCompiler.compilePML(pml);
        CreateObligationStatement stmt = (CreateObligationStatement)pmlStatements.getFirst();
        return stmt.getRuleStmts().getFirst();
    }

}
