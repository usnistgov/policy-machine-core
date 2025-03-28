package gov.nist.csd.pm.pap.pml.function.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public class Env extends PMLOperation {

	public static final PMLFormalArg KEY_ARG = new PMLFormalArg("key", Type.string());

	public Env() {
		super(
				"env",
				Type.string(),
				List.of(KEY_ARG)
		);
	}

	@Override
	public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {

	}

	@Override
	public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
		String value = System.getenv(actualArgs.get(KEY_ARG).getStringValue());
		return new StringValue(value);
	}
}
