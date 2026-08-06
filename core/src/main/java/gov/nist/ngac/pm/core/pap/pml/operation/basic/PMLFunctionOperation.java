package gov.nist.ngac.pm.core.pap.pml.operation.basic;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.Function;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for functions defined in PML, carrying the {@link PMLOperationSignature} and
 * {@link ExecutionContext} a {@link PMLOperation} needs.
 *
 * @param <T> the function's return type
 */
public abstract class PMLFunctionOperation<T> extends Function<T> implements PMLOperation {

	public static final FormalParameter<String> NODE_NAME_PARAM = new FormalParameter<>("node_name", STRING_TYPE);

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

	/**
	 * Returns the execution context this operation is currently running under.
	 *
	 * @throws IllegalArgumentException if no context has been set yet
	 */
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
