package gov.nist.csd.pm.core.pap.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract sealed class Operation<R> implements Serializable permits AdminOperation, ResourceOperation,
    QueryOperation, Routine, Function {

    private static final long serialVersionUID = 1L;
    public static final FormalParameter<String> NAME_PARAM = new FormalParameter<>("name", STRING_TYPE);
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));

    protected final String name;
    protected final Type<R> returnType;
    protected final List<FormalParameter<?>> parameters;
    protected final List<RequiredCapability> requiredCapabilities;

    public Operation(String name, Type<R> returnType, List<FormalParameter<?>> parameters, List<RequiredCapability> requiredCapabilities) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.requiredCapabilities = requiredCapabilities;
    }

    public Operation(String name, Type<R> returnType, List<FormalParameter<?>> parameters,
                     RequiredCapability requiredCapability, RequiredCapability... requiredCapabilities) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;

        this.requiredCapabilities = new ArrayList<>();
        this.requiredCapabilities.add(requiredCapability);
        this.requiredCapabilities.addAll(List.of(requiredCapabilities));
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

    public List<RequiredCapability> getRequiredCapabilities() {
        return requiredCapabilities;
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
     * @throws UnauthorizedException if the user does not satisfy the required capabilities of the operation.
     * @throws PMException If there is an error checking access.
     */
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        if (requiredCapabilities.isEmpty()) {
            return;
        }

        for (RequiredCapability reqCap : requiredCapabilities) {
            if (reqCap.isSatisfied(pap, userCtx, args)) {
                return;
            }
        }

        throw UnauthorizedException.of(pap.query().graph(), userCtx, getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation<?> operation = (Operation<?>) o;
        return Objects.equals(name, operation.name) && Objects.equals(returnType, operation.returnType)
            && Objects.equals(parameters, operation.parameters) && Objects.equals(requiredCapabilities,
            operation.requiredCapabilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, parameters, requiredCapabilities);
    }

    private Args validateAndPrepareArgs(Map<String, Object> rawArgs, boolean checkMissing) {
        Map<String, FormalParameter<?>> paramMap = parameters.stream()
            .collect(Collectors.toMap(FormalParameter::getName, java.util.function.Function.identity()));

        checkUnexpectedArgs(rawArgs.keySet(), paramMap.keySet());
        if (checkMissing) {
            checkMissingArgs(rawArgs.keySet(), paramMap.keySet());
        }

        return new Args(buildTypeSafeArgs(rawArgs, paramMap));
    }

    private void checkUnexpectedArgs(Set<String> actualKeys, Set<String> expectedKeys) {
        Set<String> unexpected = new HashSet<>(actualKeys);
        unexpected.removeAll(expectedKeys);
        if (!unexpected.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("unexpected args provided for function '%s': %s", name, unexpected));
        }
    }

    private void checkMissingArgs(Set<String> actualKeys, Set<String> expectedKeys) {
        Set<String> missing = new HashSet<>(expectedKeys);
        missing.removeAll(actualKeys);
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("missing required arguments for function '%s': %s", name, missing));
        }
    }

    private Map<FormalParameter<?>, Object> buildTypeSafeArgs(Map<String, Object> rawArgs,
                                                              Map<String, FormalParameter<?>> paramMap) {
        Map<FormalParameter<?>, Object> argsWithFormalParams = new HashMap<>();
        for (Entry<String, Object> entry : rawArgs.entrySet()) {
            FormalParameter<?> param = paramMap.get(entry.getKey());
            Object value = entry.getValue();
            validateArgType(entry.getKey(), param, value);
            argsWithFormalParams.put(param, value);
        }

        return argsWithFormalParams;
    }

    private void validateArgType(String argName, FormalParameter<?> param, Object value) {
        if (value != null && !param.getType().isCastableTo(Type.resolveTypeOfObject(value))) {
            throw new IllegalArgumentException(
                String.format("Invalid type for argument '%s'. Expected %s but got %s",
                    argName, param.getType().getClass().getSimpleName(),
                    Type.resolveTypeOfObject(value).getClass().getSimpleName()));
        }
    }
}
