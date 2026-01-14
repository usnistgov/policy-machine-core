package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
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

public abstract class Function<R> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final String name;
    protected final List<FormalParameter<?>> parameters;

    public Function(String name, List<FormalParameter<?>> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Execute the function with the given arguments and PAP object. This method allows for modification and querying of
     * the underlying policy.
     *
     * @param pap  The PAP object.
     * @param args The arguments passed to the function execution.
     * @return The function return value.
     */
    public abstract R execute(PAP pap, Args args) throws PMException;

    public Args validateAndPrepareArgs(Map<String, Object> rawArgs) {
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
        Set<String> missing = new HashSet<>(expectedKeys);
        missing.removeAll(actualKeys);
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("missing required arguments for function '%s': %s", name, missing));
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

    public String getName() {
        return name;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Function<?> function)) {
            return false;
        }
        return Objects.equals(name, function.name) && Objects.equals(parameters, function.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters);
    }
}
