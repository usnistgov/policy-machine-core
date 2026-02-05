package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * RequiredCapability maps a operation formal parameter to the access rights required to satisfy this capability.
 */
public class RequiredCapability {

    private final Map<NodeFormalParameter<?>, AccessRightSet> reqCap;

    public RequiredCapability() {
        reqCap = new HashMap<>();
    }

    public RequiredCapability(Map<NodeFormalParameter<?>, AccessRightSet> reqCap) {
        this.reqCap = reqCap;
    }

    public RequiredCapability(NodeFormalParameter<?> param, AccessRightSet arset) {
        this.reqCap = new HashMap<>();

        this.reqCap.put(param, arset);
    }

    public RequiredCapability(NodeFormalParameter<?> param1, AccessRightSet arset1,
                              NodeFormalParameter<?> param2, AccessRightSet arset2) {
        this.reqCap = new HashMap<>();

        this.reqCap.put(param1, arset1);
        this.reqCap.put(param2, arset2);
    }

    public RequiredCapability(NodeFormalParameter<?> param1, AccessRightSet arset1,
                              NodeFormalParameter<?> param2, AccessRightSet arset2,
                              NodeFormalParameter<?> param3, AccessRightSet arset3) {
        this.reqCap = new HashMap<>();

        this.reqCap.put(param1, arset1);
        this.reqCap.put(param2, arset2);
        this.reqCap.put(param3, arset3);
    }

    /**
     * Checks if the given user and args satisfies this RequiredCapability.
     * @param pap the PAP object used to access the policy information.
     * @param userCtx the user performing the operation.
     * @param args the args passed to the operation.
     * @return true if this RequiredCapability is satisfied.
     * @throws PMException if there is an error checking if the user has the required privileges.
     */
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        for (Entry<? extends NodeFormalParameter<?>, AccessRightSet> entry : reqCap.entrySet()) {
            if (hasRequiredPrivilegesForParam(pap, userCtx, args, entry.getKey(), entry.getValue())) {
                return true;
            }
        }

        return false;
    }

    private boolean hasRequiredPrivilegesForParam(PAP pap, UserContext userCtx, Args args,
                                                  NodeFormalParameter<?> formalParameter,
                                                  AccessRightSet required) throws PMException {
        List<Long> nodeIds = resolveNodeIds(pap.query().graph(), args, formalParameter);
        for (long id : nodeIds) {
            if (!hasRequiredPrivileges(pap, userCtx, id, required)) {
                return false;
            }
        }
        return !nodeIds.isEmpty();
    }

    private List<Long> resolveNodeIds(GraphQuery graph, Args args,
                                      NodeFormalParameter<?> formalParameter) throws PMException {
        return switch (formalParameter) {
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
        TargetContext targetCtx = new TargetContext(id);
        AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
        return privs.containsAll(required);
    }
}
