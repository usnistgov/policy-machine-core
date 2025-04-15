package gov.nist.csd.pm.pap.pml.function.basic;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PMLBasicFunction extends AdminFunction<Object, Args> implements PMLFunction {

	public static final FormalParameter<String> NODE_NAME_PARAM = new FormalParameter<>("nodeName", STRING_TYPE);

	private final Type<?> returnType;
	private final List<FormalParameter<?>> pmlFormalParameters;
	private final PMLBasicFunctionSignature signature;
	protected ExecutionContext ctx;

	public PMLBasicFunction(String name, Type<?> returnType, List<FormalParameter<?>> formalParameters) {
		super(name, new ArrayList<>(formalParameters));

		this.returnType = returnType;
		this.pmlFormalParameters = formalParameters;
		this.signature = new PMLBasicFunctionSignature(name, returnType, formalParameters);
	}

	public PMLBasicFunction(String name, Type<?> returnType) {
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

	public Type<?> getReturnType() {
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
	protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
		return new Args(argsMap);
	}
}
