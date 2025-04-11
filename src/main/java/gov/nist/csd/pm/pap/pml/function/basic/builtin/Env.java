package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;



import java.util.List;

public class Env extends PMLBasicFunction {

	public static final FormalParameter<String> KEY_ARG = new FormalParameter<>("key", STRING_TYPE);

	public Env() {
		super(
				"env",
				STRING_TYPE,
				List.of(KEY_ARG)
		);
	}

	@Override
	public Object execute(PAP pap, MapArgs args) throws PMException {
		return System.getenv(args.get(KEY_ARG));
	}
}
