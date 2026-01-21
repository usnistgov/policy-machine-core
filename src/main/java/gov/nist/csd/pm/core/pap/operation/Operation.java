package gov.nist.csd.pm.core.pap.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract sealed class Operation<R> implements Serializable permits AdminOperation, ResourceOperation, QueryOperation, Routine,
    Function {

    private static final long serialVersionUID = 1L;
    public static final FormalParameter<String> NAME_PARAM = new FormalParameter<>("name", STRING_TYPE);
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));

    protected final String name;
    protected final Type<R> returnType;
    protected final List<FormalParameter<?>> parameters;

    public Operation(String name, Type<R> returnType, List<FormalParameter<?>> parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    /**
     * Execute the function with the given arguments and PAP object. This method allows for modification and querying of
     * the underlying policy.
     *
     * @param pap  The PAP object to execute on.
     * @param args The arguments passed to the function execution.
     * @return The function return value.
     */
    public abstract R execute(PAP pap, Args args) throws PMException;

    public String getName() {
        return name;
    }

    public Type<R> getReturnType() {
        return returnType;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return parameters;
    }

    /**
     * Convert the given map of raw args to an Arg object with type checking on arg values.
     * @param rawArgs The raw args to validate and prepare.
     * @return An Args object.
     */
    public Args validateAndPrepareArgs(Map<String, Object> rawArgs) {
        return validateAndPrepareArgs(rawArgs, true);
    }

    /**
     * Convert the given map of raw args to an Arg object with type checking on arg values. This method allows for only
     * a subset of the expected args to be included but will throw an exception for unrecognized args.
     * @param rawArgs The raw args to validate and prepare.
     * @return An Args object.
     */
    public Args validateAndPrepareSubsetArgs(Map<String, Object> rawArgs) {
        return validateAndPrepareArgs(rawArgs, false);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation<?> operation)) {
            return false;
        }
        return Objects.equals(name, operation.name) && Objects.equals(returnType, operation.returnType)
            && Objects.equals(parameters, operation.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, parameters);
    }

    private void check(PAP pap, UserContext userCtx, NodeFormalParameter<?> nodeFormalParameter, long id) throws PMException {
        TargetContext targetCtx = new TargetContext(id);
        AccessRightSet privs = pap.query().access().computePrivileges(userCtx, targetCtx);
        check(pap.query().graph(), userCtx, targetCtx, nodeFormalParameter.getAccessRights(), privs);
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

    private Args validateAndPrepareArgs(Map<String, Object> rawArgs, boolean checkMissing) {
        Map<String, FormalParameter<?>> paramMap = parameters.stream()
            .collect(Collectors.toMap(FormalParameter::getName, java.util.function.Function.identity()));

        Set<String> expectedKeys = paramMap.keySet();
        Set<String> actualKeys = rawArgs.keySet();

        // check for unexpected args
        Set<String> unexpected = new HashSet<>(actualKeys);
        unexpected.removeAll(expectedKeys);
        if (!unexpected.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("unexpected args provided for function '%s': %s", name, unexpected));
        }

        // check for missing args
        if (checkMissing) {
            Set<String> missing = new HashSet<>(expectedKeys);
            missing.removeAll(actualKeys);
            if (!missing.isEmpty()) {
                throw new IllegalArgumentException(
                    String.format("missing required arguments for function '%s': %s", name, missing));
            }
        }

        // build type safe map
        Map<FormalParameter<?>, Object> argsWithFormalParams = new HashMap<>();

        for (Map.Entry<String, Object> entry : rawArgs.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Type<?> valueType = Type.resolveTypeOfObject(value);
            FormalParameter<?> param = paramMap.get(key);

            if (value != null && !param.getType().isCastableTo(valueType)) {
                throw new IllegalArgumentException(
                    String.format("Invalid type for argument '%s'. Expected %s but got %s",
                        key, param.getType().getClass().getSimpleName(), valueType.getClass().getSimpleName()));
            }

            argsWithFormalParams.put(param, value);
        }

        return new Args(argsWithFormalParams);
    }
}
