package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequiredPrivilegeOnParameter implements RequiredPrivilege {

    private final NodeFormalParameter<?> param;
    private final AccessRightSet required;

    public RequiredPrivilegeOnParameter(NodeFormalParameter<?> param, AccessRightSet required) {
        this.param = param;
        this.required = required;
    }

    public RequiredPrivilegeOnParameter(NodeFormalParameter<?> param, AdminAccessRight adminAccessRight) {
        this(param, new AccessRightSet(adminAccessRight));
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        List<Long> nodeIds = resolveNodeIds(pap.query().graph(), args);
        for (long id : nodeIds) {
            if (!hasRequiredPrivileges(pap, userCtx, id, required)) {
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

    private boolean hasRequiredPrivileges(PAP pap, UserContext userCtx, long id, AccessRightSet required) throws
                                                                                                          PMException {
        TargetContext targetCtx = new TargetContext(id);
        AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
        return privs.containsAll(required);
    }

    public NodeFormalParameter<?> param() {
        return param;
    }

    public AccessRightSet required() {
        return required;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (RequiredPrivilegeOnParameter) obj;
        return Objects.equals(this.param, that.param) &&
            Objects.equals(this.required, that.required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(param, required);
    }

    @Override
    public String toString() {
        return "RequiredPrivilegeOnParameter[" +
            "param=" + param + ", " +
            "required=" + required + ']';
    }

}
