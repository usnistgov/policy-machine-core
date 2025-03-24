package gov.nist.csd.pm.pap.pml.executable.function;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public abstract class PMLFunction extends Operation<Value> {
	public PMLFunction(String name, List<String> allOperandsNames, List<String> nodeOperandNames) {
		super(name, allOperandsNames, nodeOperandNames);
	}

	public PMLFunction(String name, List<String> allOperandsNames) {
		super(name, allOperandsNames);
	}

	public PMLFunction(String name) {
		super(name);
	}

	@Override
	public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

	}
}
