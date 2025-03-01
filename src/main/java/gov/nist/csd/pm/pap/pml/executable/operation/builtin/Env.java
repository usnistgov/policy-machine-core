package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class Env extends PMLOperation {

	public Env() {
		super(
				"env",
				Type.string(),
				List.of("key"),
				Map.of("key", Type.string())
		);
	}

	@Override
	public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

	}

	@Override
	public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
		return new StringValue(System.getenv(operands.get("key").toString()));
	}
}
