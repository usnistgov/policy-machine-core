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
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

/**
 * A PML built-in query that returns the caller's privileges on a list of nodes.
 */
public class SelfAccessComputePrivilegesBatch extends QueryOperation<List<List<String>>> {

    private static final FormalParameter<List<String>> NODE_NAMES_PARAM =
        new FormalParameter<>("node_names", ListType.of(STRING_TYPE));

    public SelfAccessComputePrivilegesBatch() {
        super("self_compute_privileges_batch", ListType.of(ListType.of(STRING_TYPE)),
            List.of(NODE_NAMES_PARAM), List.of());
    }

    @Override
    public List<List<String>> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        List<String> nodeNames = args.get(NODE_NAMES_PARAM);
        List<TargetContext> targets = new ArrayList<>(nodeNames.size());
        for (String name : nodeNames) {
            targets.add(NodeTargetContext.of(query.graph().getNodeId(name)));
        }
        List<AccessRightSet> results = query.access().self(userCtx).computePrivileges(targets);
        List<List<String>> ret = new ArrayList<>(results.size());
        for (AccessRightSet ars : results) {
            ret.add(new ArrayList<>(ars));
        }
        return ret;
    }
}
