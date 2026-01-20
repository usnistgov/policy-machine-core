package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLBasicOperation;
import java.util.List;

public class Env extends PMLBasicOperation<String> {

	public static final FormalParameter<String> KEY_PARAM = new FormalParameter<>("key", STRING_TYPE);

	public Env() {
		super(
				"env",
				STRING_TYPE,
				List.of(KEY_PARAM)
		);
	}

	@Override
	protected String execute(Args args) throws PMException {
		return System.getenv(args.get(KEY_PARAM));
	}
}
