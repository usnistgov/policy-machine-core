/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
 * Base class for functions defined in PML.
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
