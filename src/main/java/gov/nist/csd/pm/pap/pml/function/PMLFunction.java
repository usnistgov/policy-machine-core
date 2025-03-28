package gov.nist.csd.pm.pap.pml.function;

import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.List;

public abstract class PMLFunction extends AdminFunction<Value> {

	public static final PMLFormalArg NODE_NAME_ARG = new PMLFormalArg("nodeName", Type.string());

	private final Type returnType;
	private final List<PMLFormalArg> pmlFormalArgs;
	private final PMLFunctionSignature signature;
	protected ExecutionContext ctx;

	public PMLFunction(String name, Type returnType, List<PMLFormalArg> formalArgs) {
		super(name, new ArrayList<>(formalArgs));

		this.returnType = returnType;
		this.pmlFormalArgs = formalArgs;
		this.signature = new PMLFunctionSignature(name, returnType, formalArgs);
	}

	public PMLFunction(String name, Type returnType) {
		super(name, new ArrayList<>());

		this.returnType = returnType;
		this.pmlFormalArgs = new ArrayList<>();
		this.signature = new PMLFunctionSignature(name, returnType, new ArrayList<>());
	}

	public List<PMLFormalArg> getPmlFormalArgs() {
		return pmlFormalArgs;
	}

	public PMLFunctionSignature getSignature() {
		return signature;
	}

	public Type getReturnType() {
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
