package gov.nist.csd.pm.pap.pml.executable.function;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;
import java.util.Map;

public class PMLFunctionSignature extends PMLExecutableSignature {
	public PMLFunctionSignature(String functionName, Type returnType, List<String> operands, Map<String, Type> operandTypes) {
		super(functionName, returnType, operands, operandTypes);
	}

	@Override
	public String toFormattedString(int indentLevel) {
		String argsStr = serializeFormalArgs();

		String indent = indent(indentLevel);
		return String.format(
				"%s%s %s(%s) %s",
				indent,
				"function",
				functionName,
				argsStr,
				returnType.isVoid() ? "" : returnType.toString() + " "
		);
	}
}
