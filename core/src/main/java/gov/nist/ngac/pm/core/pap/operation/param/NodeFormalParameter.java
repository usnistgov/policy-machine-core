package gov.nist.ngac.pm.core.pap.operation.param;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;

/**
 * Base class for formal parameters that reference one or more graph nodes, by id or by name, singular or
 * as a list — used by {@link gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter} to
 * know which arguments to check privileges against.
 *
 * @param <T> the parameter's Java type
 */
public abstract sealed class NodeFormalParameter<T> extends FormalParameter<T>
	permits NodeIdFormalParameter, NodeIdListFormalParameter, NodeNameFormalParameter, NodeNameListFormalParameter {

	public NodeFormalParameter(String name, Type<T> type) {
		super(name, type, true);
	}
}
