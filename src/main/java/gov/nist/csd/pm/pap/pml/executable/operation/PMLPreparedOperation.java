package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PreparedOperation;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Map;

public class PMLPreparedOperation extends PreparedOperation<Value> {

    public PMLPreparedOperation(PMLOperation op, Map<String, Object> operands) {
        super(op, operands);
    }

    @Override
    public Value execute(PAP pap) throws PMException {
        return getOp().execute(pap, getOperands());
    }
}
