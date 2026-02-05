package gov.nist.csd.pm.core.pap.pml.operation.basic;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.operation.Function;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.ArrayList;
import java.util.List;

public abstract class PMLFunctionOperation<T> extends Function<T> implements PMLOperation {

	public static final FormalParameter<String> NODE_NAME_PARAM = new FormalParameter<>("nodeName", STRING_TYPE);

	private final Type<T> returnType;
	private final List<FormalParameter<?>> pmlFormalParameters;
	private final PMLOperationSignature signature;
	protected ExecutionContext ctx;

	public PMLFunctionOperation(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters) {
		super(name, returnType, new ArrayList<>(formalParameters));

		this.returnType = returnType;
		this.pmlFormalParameters = formalParameters;
		this.signature = new PMLOperationSignature(OperationType.FUNCTION, name, returnType, formalParameters, List.of());
	}

	public PMLFunctionOperation(String name, Type<T> returnType) {
		super(name, returnType, new ArrayList<>());

		this.returnType = returnType;
		this.pmlFormalParameters = new ArrayList<>();
		this.signature = new PMLOperationSignature(OperationType.FUNCTION, name, returnType, new ArrayList<>(), List.of());
	}

	public List<FormalParameter<?>> getPmlFormalArgs() {
		return pmlFormalParameters;
	}

	public PMLOperationSignature getSignature() {
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
