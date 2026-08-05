package gov.nist.ngac.pm.core.pap.pml.pattern;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.PMLCompiler;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import java.util.List;

public class PatternTestUtil {

    public static CreateObligationStatement compileTestCreateObligationStatement(PAP pap, String pml) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();
        List<PMLStatement<?>> pmlStatements = pmlCompiler.compilePML(pap, pml);
        return (CreateObligationStatement)pmlStatements.getFirst();
    }

}
