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

package gov.nist.ngac.pm.core.pap.operation.reqcap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.ngac.pm.core.pap.query.GraphQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A {@link RequiredPrivilege} satisfied when the acting user holds the required access rights on every
 * node a parameter resolves to.
 */
public final class RequiredPrivilegeOnParameter extends RequiredPrivilege {

    private final NodeFormalParameter<?> param;

    public RequiredPrivilegeOnParameter(NodeFormalParameter<?> param, AccessRightSet required) {
        super(required);
        this.param = param;
    }

    public RequiredPrivilegeOnParameter(NodeFormalParameter<?> param, AdminAccessRight adminAccessRight) {
        this(param, new AccessRightSet(adminAccessRight));
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        List<Long> nodeIds = resolveNodeIds(pap.query().graph(), args);
        for (long id : nodeIds) {
            if (!hasRequiredPrivileges(pap, userCtx, id, getRequired())) {
                return false;
            }
        }

        return true;
    }

    private List<Long> resolveNodeIds(GraphQuery graph, Args args) throws PMException {
        return switch (param) {
            case NodeIdFormalParameter p -> List.of(args.get(p));
            case NodeIdListFormalParameter p -> args.get(p);
            case NodeNameFormalParameter p -> List.of(graph.getNodeId(args.get(p)));
            case NodeNameListFormalParameter p -> {
                List<Long> ids = new ArrayList<>();
                for (String name : args.get(p)) {
                    ids.add(graph.getNodeId(name));
                }
                yield ids;
            }
        };
    }

    private boolean hasRequiredPrivileges(PAP pap, UserContext userCtx, long id, AccessRightSet required) throws PMException {
        TargetContext targetCtx = NodeTargetContext.of(id);
        AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
        return !privs.isEmpty() && privs.containsAll(required);
    }

    /**
     * Returns the parameter this requirement checks privileges against.
     */
    public NodeFormalParameter<?> param() {
        return param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequiredPrivilegeOnParameter that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(param, that.param);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), param);
    }
}
