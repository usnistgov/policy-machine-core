package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract sealed class Function<T, R> implements Serializable
    permits Operation, Routine, BasicFunction, QueryFunction {

    private static final long serialVersionUID = 1L;
    protected final String name;
    protected final Type<R> returnType;
    protected final List<FormalParameter<?>> parameters;

    public Function(String name, Type<R> returnType, List<FormalParameter<?>> parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    /**
     * Execute the function with the given arguments and PAP object. This method allows for modification and querying of
     * the underlying policy.
     *
     * @param t  The object to execute on.
     * @param args The arguments passed to the function execution.
     * @return The function return value.
     */
    public abstract R execute(T t, Args args) throws PMException;

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

    public String getName() {
        return name;
    }

    public Type<R> getReturnType() {
        return returnType;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Function<?, ?> function)) {
            return false;
        }
        return Objects.equals(name, function.name) && Objects.equals(parameters, function.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters);
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
