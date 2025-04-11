package gov.nist.csd.pm.pap.pml.function.basic;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PMLBasicFunction extends AdminFunction<Object, MapArgs> implements PMLFunction {

	public static final ObjectMapper objectMapper = new ObjectMapper();
	public static final FormalParameter<String> NODE_NAME_ARG = new FormalParameter<>("nodeName", STRING_TYPE);

	private final ArgType<?> returnType;
	private final List<FormalParameter<?>> pmlFormalParameters;
	private final PMLBasicFunctionSignature signature;
	protected ExecutionContext ctx;

	public PMLBasicFunction(String name, ArgType<?> returnType, List<FormalParameter<?>> formalParameters) {
		super(name, new ArrayList<>(formalParameters));

		this.returnType = returnType;
		this.pmlFormalParameters = formalParameters;
		this.signature = new PMLBasicFunctionSignature(name, returnType, formalParameters);
	}

	public PMLBasicFunction(String name, ArgType<?> returnType) {
		super(name, new ArrayList<>());

		this.returnType = returnType;
		this.pmlFormalParameters = new ArrayList<>();
		this.signature = new PMLBasicFunctionSignature(name, returnType, new ArrayList<>());
	}

	public List<FormalParameter<?>> getPmlFormalArgs() {
		return pmlFormalParameters;
	}

	public PMLFunctionSignature getSignature() {
		return signature;
	}

	public ArgType<?> getReturnType() {
		return returnType;
	}

	public ExecutionContext getCtx() {
		if (ctx == null) {
			throw new IllegalArgumentException("execution context has not been set");
		}

		return ctx;
	}

	public void setCtx(ExecutionContext ctx) {
		this.ctx = ctx;
	}

	@Override
	protected MapArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
		return new MapArgs(argsMap);
	}
}
