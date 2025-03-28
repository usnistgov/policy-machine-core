package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;

public interface FunctionDefinitionStatement extends PMLStatement {

    PMLFunctionSignature getSignature();

}
