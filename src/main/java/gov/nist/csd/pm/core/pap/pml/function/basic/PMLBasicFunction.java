package gov.nist.csd.pm.core.pap.pml.function.basic;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.BasicFunction;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import java.util.ArrayList;
import java.util.List;

public abstract class PMLBasicFunction<T> extends BasicFunction<T> implements PMLFunction {

	public static final FormalParameter<String> NODE_NAME_PARAM = new FormalParameter<>("nodeName", STRING_TYPE);

	private final Type<T> returnType;
	private final List<FormalParameter<?>> pmlFormalParameters;
	private final PMLBasicFunctionSignature signature;
	protected ExecutionContext ctx;

	public PMLBasicFunction(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters) {
		super(name, returnType, new ArrayList<>(formalParameters));

		this.returnType = returnType;
		this.pmlFormalParameters = formalParameters;
		this.signature = new PMLBasicFunctionSignature(name, returnType, formalParameters);
	}

	public PMLBasicFunction(String name, Type<T> returnType) {
		super(name, returnType, new ArrayList<>());

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

	public Type<T> getReturnType() {
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
}
