package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;


import java.util.List;

public class Env extends PMLBasicFunction {

	public static final FormalParameter<String> KEY_PARAM = new FormalParameter<>("key", STRING_TYPE);

	public Env() {
		super(
				"env",
				STRING_TYPE,
				List.of(KEY_PARAM)
		);
	}

	@Override
	public Object execute(PAP pap, Args args) throws PMException {
		return System.getenv(args.get(KEY_PARAM));
	}
}
