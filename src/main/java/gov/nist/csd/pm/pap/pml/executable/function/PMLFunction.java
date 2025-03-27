package gov.nist.csd.pm.pap.pml.executable.function;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public abstract class PMLFunction extends PMLOperation {

	public PMLFunction(String name, Type returnType, List<PMLFormalArg> formalArgs) {
		super(name, returnType, formalArgs);
	}

	public PMLFunction(String name, Type returnType) {
		super(name, returnType);
	}

	@Override
	public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {

	}
}
