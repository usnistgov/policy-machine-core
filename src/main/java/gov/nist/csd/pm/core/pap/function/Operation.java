package gov.nist.csd.pm.core.pap.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.List;
import java.util.Map;

public abstract sealed class Operation<R> extends Function<R> permits AdminOperation, ResourceOperation{

    public static final FormalParameter<String> NAME_PARAM = new FormalParameter<>("name", STRING_TYPE);
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));

    public Operation(String name, Type<R> returnType, List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }

    /**
     * Checks if the given user can perform this operation with the given args.
     * @param pap The PAP object used to query the access state of the policy.
     * @param userCtx The user trying to execute the operation.
     * @param args The args passed to the operation.
     * @throws PMException If there is an error checking access.
     */
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        List<FormalParameter<?>> formalParameters = getFormalParameters();
        for (FormalParameter<?> formalParameter : formalParameters) {
            switch (formalParameter) {
                case NodeIdFormalParameter nodeIdFormalParameter ->
                    check(pap, userCtx, nodeIdFormalParameter, args.get(nodeIdFormalParameter));
                case NodeIdListFormalParameter nodeIdListFormalParameter -> {
                    for (long id : args.get(nodeIdListFormalParameter)) {
                        check(pap, userCtx, nodeIdListFormalParameter, id);
                    }
                }
                case NodeNameFormalParameter nodeNameFormalParameter ->
                    check(pap, userCtx, nodeNameFormalParameter, pap.query().graph().getNodeId(args.get(nodeNameFormalParameter)));
                case NodeNameListFormalParameter nodeNameListFormalParameter -> {
                    for (String name : args.get(nodeNameListFormalParameter)) {
                        check(pap, userCtx, nodeNameListFormalParameter, pap.query().graph().getNodeId(name));
                    }
                }
                case null, default -> {}
            }
        }
    }

    private void check(PAP pap, UserContext userCtx, NodeFormalParameter<?> nodeFormalParameter, long id) throws PMException {
        TargetContext targetCtx = new TargetContext(id);
        AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
        check(pap.query().graph(), userCtx, targetCtx, nodeFormalParameter.getReqCap(), privs);
    }

    private void check(GraphQuery graphQuery,
                       UserContext user,
                       TargetContext target,
                       RequiredCapabilities reqCap,
                       AccessRightSet userPrivileges) throws PMException {
        AccessRightSet reqCaps = reqCap.getReqCaps();
        if(userPrivileges.containsAll(reqCaps)) {
            return;
        }

        throw UnauthorizedException.of(graphQuery, user, target, userPrivileges, reqCaps);
    }
}
