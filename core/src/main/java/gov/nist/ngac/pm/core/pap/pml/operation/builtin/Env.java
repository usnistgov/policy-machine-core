package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

public class Env extends PMLFunctionOperation<String> {

	public static final FormalParameter<String> KEY_PARAM = new FormalParameter<>("key", STRING_TYPE);

	public Env() {
		super(
				"env",
				STRING_TYPE,
				List.of(KEY_PARAM)
		);
	}

	@Override
    public String execute(Args args) throws PMException {
		return System.getenv(args.get(KEY_PARAM));
	}
}
