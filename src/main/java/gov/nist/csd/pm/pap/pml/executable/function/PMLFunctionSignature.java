package gov.nist.csd.pm.pap.pml.executable.function;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;

public class PMLFunctionSignature extends PMLExecutableSignature {

	public PMLFunctionSignature(String functionName,
								Type returnType,
								List<PMLFormalArg> formalArgs) {
		super(functionName, returnType, formalArgs);
	}

	@Override
	public String toFormattedString(int indentLevel) {
		return toString("function", indentLevel);
	}
}
