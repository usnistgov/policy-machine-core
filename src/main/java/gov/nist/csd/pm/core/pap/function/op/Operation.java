package gov.nist.csd.pm.core.pap.function.op;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.Function;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract sealed class Operation<R> extends Function<R> permits AdminOperation, ResourceOperation{

    public static final FormalParameter<String> NAME_PARAM = new FormalParameter<>("name", STRING_TYPE);
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));

    public Operation(String name, List<FormalParameter<?>> parameters) {
        super(name, parameters);
    }

    /**
     * Checks if the given user can perform this operation with the given args.
     * @param pap The PAP object used to query the access state of the policy.
     * @param userCtx The user trying to execute the operation.
     * @param args The args passed to the operation.
     * @throws PMException If there is an error checking access.
     */
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        // check privileges for node args
        Map<NodeFormalParameter, NodeArg<?>> nodeArgs = getNodeArgs(args.getMap());
        for (Map.Entry<NodeFormalParameter, NodeArg<?>> entry : nodeArgs.entrySet()) {
            NodeFormalParameter nodeFormalParameter = entry.getKey();
            NodeArg<?> value = entry.getValue();

            checkPrivileges(pap, userCtx, nodeFormalParameter, value);
        }

        // check privileges for node list args
        Map<NodeListFormalParameter, List<NodeArg<?>>> nodeListArgs = getNodeListArgs(args.getMap());
        for (Map.Entry<NodeListFormalParameter, List<NodeArg<?>>> entry : nodeListArgs.entrySet()) {
            NodeListFormalParameter nodeListFormalParameter = entry.getKey();
            List<NodeArg<?>> values = entry.getValue();

            checkPrivileges(pap, userCtx, nodeListFormalParameter, values);
        }
    }

    private void checkPrivileges(PAP pap, UserContext userCtx, NodeFormalParameter nodeFormalParameter, NodeArg<?> nodeArg) throws PMException {
        TargetContext targetCtx = new TargetContext(nodeArg.getId(pap));
        AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
        nodeFormalParameter.getReqCap().check(userCtx, targetCtx, privs);
    }

    private void checkPrivileges(PAP pap, UserContext userCtx, NodeListFormalParameter nodeListFormalParameter, List<NodeArg<?>> nodeArgs) throws PMException {
        for (NodeArg<?> nodeArg : nodeArgs) {
            TargetContext targetCtx = new TargetContext(nodeArg.getId(pap));
            AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
            nodeListFormalParameter.getReqCap().check(userCtx, targetCtx, privs);
        }
    }

    private Map<NodeFormalParameter, NodeArg<?>> getNodeArgs(Map<FormalParameter<?>, Object> map) {
        Map<NodeFormalParameter, NodeArg<?>> nodeArgs = new HashMap<>();
        for (Entry<FormalParameter<?>, Object> entry : map.entrySet()) {
            FormalParameter<?> formalParameter = entry.getKey();
            Object value = entry.getValue();

            if (formalParameter instanceof NodeFormalParameter nodeFormalParameter) {
                nodeArgs.put(nodeFormalParameter, (NodeArg<?>)value);
            }
        }

        return nodeArgs;
    }

    private Map<NodeListFormalParameter, List<NodeArg<?>>> getNodeListArgs(Map<FormalParameter<?>, Object> map) {
        Map<NodeListFormalParameter, List<NodeArg<?>>> nodeArgs = new HashMap<>();
        for (Entry<FormalParameter<?>, Object> entry : map.entrySet()) {
            FormalParameter<?> formalParameter = entry.getKey();
            Object value = entry.getValue();

            if (formalParameter instanceof NodeListFormalParameter nodeListFormalParameter) {
                nodeArgs.put(nodeListFormalParameter, (List<NodeArg<?>>) value);
            }
        }

        return nodeArgs;
    }

}
