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

package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

/**
 * A PML built-in query that returns the caller's denied privileges on a node.
 */
public class SelfAccessComputeDeniedPrivileges extends QueryOperation<List<String>> {

    private static final NodeNameFormalParameter NODE_NAME_PARAM =
        new NodeNameFormalParameter("node_name");

    public SelfAccessComputeDeniedPrivileges() {
        super("self_compute_denied_privileges", ListType.of(STRING_TYPE), List.of(NODE_NAME_PARAM), List.of());
    }

    @Override
    public List<String> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        long nodeId = query.graph().getNodeId(nodeName);
        return new ArrayList<>(query.access().self(userCtx).computeDeniedPrivileges(NodeTargetContext.of(nodeId)));
    }
}
